package com.example.gateway

import java.nio.charset.StandardCharsets

import cats.effect.IO
import com.example.core.API.Book
import com.twitter.finagle.Http.Client
import com.twitter.finagle.{Http, Service, http}
import com.twitter.finagle.http.{Request, Response}
import com.twitter.io.Buf
import com.twitter.util.{Await, Future, Return}
import io.circe.Decoder.Result
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._
import io.circe.generic.auto._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe.syntax._
import io.circe.parser.decode
import io.circe._
import io.circe.generic.semiauto._


object Main extends App {

  case class Message(hello: String)

  // TODO: Yuchen - configure the dns name here
  def authClient: Service[Request, Response] = Http.client.newService("auth.local.zone:8081")

  val request = http.Request(http.Method.Get, "/")
  request.host = "www.scala-lang.org"

  // TODO: Yuchen - configure the dns name here
  def booksClient: Service[Request, Response] = Http.client.newService("book.local.zone:8082")

  def fetchBook(bookId: Int): Future[Book] = booksClient(Request(s"/api/v1/book/$bookId")).map { repsponse =>
    val str = new String(Buf.ByteArray.Owned.extract(repsponse.content), "UTF-8")
    parser.decode[Book](str) match {
      case Left(failure) =>
        sys.error(s"Invalid JSON :( $failure")
      case Right(book) =>
        println(book)
        book
    }
  }

  def healthcheck: Endpoint[IO, String] = get(pathEmpty) {
    Ok("OK")
  }

  def helloWorld: Endpoint[IO, Message] = get("hello") {
    val book = Await.result(fetchBook(1))
    Console.err.println(book)
    Ok(Message("World"))
  }

  def hello: Endpoint[IO, Message] = get("hello" :: path[String]) { s: String =>
    Ok(Message(s))
  }

  def service: Service[Request, Response] = Bootstrap
    .serve[Text.Plain](healthcheck)
    .serve[Application.Json](helloWorld :+: hello)
    .toService

  Await.ready(Http.server.serve(":8083", service))
}
