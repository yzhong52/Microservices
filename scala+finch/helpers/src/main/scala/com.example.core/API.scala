package com.example.core

import java.time.LocalDate

object API {
  val AuthHeader = "Authorization"

  case class AuthResponse(ok: Boolean)

  case class Book(
    bookId: Int,
    title: String,
    author: String,
    publishedDate: LocalDate
  )

}
