package com.example.helpers

import com.example.core.API.Book
import io.circe.generic.auto._
import io.circe.parser.decode
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  val jsonString =
    """{
         "author": "Hank Green",
         "book_id": "1",
         "published_date": "2018-12-25",
         "title": "An Absolutely Remarkable Thing"
       }
    """

  test("Decode book") {

    val book = decode[Book](jsonString)
    book match {
      case Left(failure) => sys.error(s"Invalid JSON :( $failure -->'$jsonString'<--")
      case Right(value) => assert(value.book_id == "1")
    }
  }
}
