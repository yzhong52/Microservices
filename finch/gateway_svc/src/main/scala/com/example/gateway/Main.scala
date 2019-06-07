package com.example.gateway

import cats.effect.IO
import com.example.core.API.{AuthHeader, AuthResponse, Book}
import com.twitter.finagle.http.{Method, Request, Response}
import com.twitter.finagle.{Failure, Http, Service}
import com.twitter.io.Buf
import com.twitter.util.{Await, Future}
import io.circe._
import io.circe.generic.auto._
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._


object Main extends App {

  // TODO: Yuchen - configure the port here
  def authClient: Service[Request, Response] = Http.client.newService("auth.local.zone:8081")

  def authRequest(authToken: String): Request = {
    val request = Request("").method(Method.Get)
    request.headerMap.add(AuthHeader, authToken)
    request
  }

  def checkAuth(authToken: String): Future[AuthResponse] = authClient(authRequest(authToken)).map { response =>
    val buffer = new String(Buf.ByteArray.Owned.extract(response.content), "UTF-8")
    parser.decode[AuthResponse](buffer) match {
      case Left(failure) => sys.error(s"Invalid JSON :( $failure $buffer")
      case Right(authResponse) => authResponse
    }
  }

  // TODO: Yuchen - configure the port here
  def booksClient: Service[Request, Response] = Http.client.newService("book.local.zone:8082")

  def fetchBook(bookId: Int): Future[Book] = booksClient(Request(s"/api/v1/book/$bookId")).map { response =>
    val buffer = new String(Buf.ByteArray.Owned.extract(response.content), "UTF-8")
    parser.decode[Book](buffer) match {
      case Left(failure) => sys.error(s"Invalid JSON :( $failure $buffer")
      case Right(book) => book
    }
  }

  val basePath = "api" :: "v1"

  def getBooks2: Endpoint[IO, Book] = get(basePath :: "book" :: path[Int] :: get(header(AuthHeader))) { (bookId: Int, token: String) =>
    val result: Future[Output[Book]] = checkAuth(token).flatMap { access =>
      if (access.ok1) {
        Console.out.println(s"fetching book info for bookId $bookId")
        fetchBook(bookId)
      }
      else {
        Console.out.println("Hell world. ")
        Future.exception(Failure.rejected("Invalid access key"))
      }
    }.map(Ok)

    Console.out.println(result)
    result
  }

  def service: Service[Request, Response] = Bootstrap
    .serve[Application.Json](getBooks2)
    .toService

  Await.ready(Http.server.serve(":8083", service))
}
