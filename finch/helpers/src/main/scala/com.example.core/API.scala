package com.example.core

import java.time.LocalDate

object API {
  val AuthHeader = "Authorization"

  case class AuthResponse(ok1: Boolean)

  case class Book(
    bookId: Int,
    title: String,
    author: String,
    publishedDate: LocalDate
  )

}
