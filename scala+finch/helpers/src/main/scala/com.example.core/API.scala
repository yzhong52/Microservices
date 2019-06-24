package com.example.core

import java.time.LocalDate

object API {
  val AuthHeader = "Authorization"

  case class AuthResponse(ok: Boolean)

  case class Book(
    book_id: String,
    title: String,
    author: String,
    published_date: LocalDate
  )
}
