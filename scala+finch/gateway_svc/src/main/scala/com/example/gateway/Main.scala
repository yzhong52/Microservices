package com.example.gateway

import cats.effect.IO
import com.example.core.API.{AuthHeader, AuthResponse, Book}
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.io.Buf
import com.twitter.util.{Await, Future}
import io.circe._
import io.circe.generic.auto._
import io.finch.Error.NotPresent
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

object Main extends App {

  // TODO: Yuchen - configure the port here
  def authClient: Service[Request, Response] = Http.client.newService("auth-service.default.svc.cluster.local:80")

  def authRequest(authToken: String): Request = {
    val request = Request("").method(Method.Get)
    request.headerMap.add(AuthHeader, authToken)
    request
  }

  def extractToken: Endpoint[IO, String] = get(header(AuthHeader)) { token: String =>
    Ok(token)
  }.handle {
    case e: NotPresent =>
      Console.err.println(s"Missing header $AuthHeader $e")
      Forbidden(new Exception(s"Missing header $AuthHeader"))
  }

  def checkAuth(authToken: String): Future[AuthResponse] = authClient(authRequest(authToken)).map(parse[AuthResponse])

  // TODO: Yuchen - configure the port here
  def booksClient: Service[Request, Response] = Http.client.newService("books-service.default.svc.cluster.local:80")

  def parse[T: Decoder](response: Response): T = {
    val buffer = new String(Buf.ByteArray.Owned.extract(response.content), "UTF-8")
    parser.decode[T](buffer) match {
      case Left(failure) => sys.error(s"Invalid JSON :( $failure -->'$buffer'<--")
      case Right(value) => value
    }
  }

  def fetchBook(bookId: Int): Future[Book] = booksClient(Request(s"/api/v1/book/$bookId")).map(parse[Book])

  val basePath = "api" :: "v1"

  def getBooks: Endpoint[IO, Book] = get(basePath :: "book" :: path[Int] :: extractToken) { (bookId: Int, token: String) =>
    checkAuth(token).flatMap { access =>
      if (access.ok) {
        Console.out.println(s"fetching book info for bookId $bookId")
        fetchBook(bookId).map(Ok)
      }
      else {
        Future.value(Forbidden(new Exception("Access Denied")))
      }
    }
  }

  def service: Service[Request, Response] = Bootstrap
    .serve[Application.Json](getBooks)
    .toService

  Await.ready(Http.server.serve(":8080", service))
}
