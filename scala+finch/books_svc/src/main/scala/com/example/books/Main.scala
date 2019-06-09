package com.example.books

import java.time.LocalDate

import cats.effect.IO
import com.example.core.API.Book
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

object Main extends App {
  val basePath = "api" :: "v1"

  def getBooks: Endpoint[IO, Book] = get(basePath :: "book" :: path[Int]) { bookId: Int =>
    val book = Book(
      bookId = bookId,
      title = "An Absolutely Remarkable Thing",
      author = "Hank Green",
      publishedDate = LocalDate.parse("2018-12-25"),
    )
    Ok(book)
  }

  def service: Service[Request, Response] = Bootstrap
    .serve[Application.Json](getBooks)
    .toService

  Await.ready(Http.server.serve(":8080", service))
}
