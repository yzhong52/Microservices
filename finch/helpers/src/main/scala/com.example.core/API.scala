package com.example.core

import java.time.LocalDate

object API {

  case class Book(
    bookId: Int,
    title: String,
    author: String,
    publishedDate: LocalDate
  )

}
