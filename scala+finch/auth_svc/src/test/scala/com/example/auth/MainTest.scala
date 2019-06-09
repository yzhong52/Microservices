package com.example.auth

import com.example.core.API
import com.example.core.API.AuthResponse
import io.finch._
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  private val path = "/"

  test("Authenticate success") {
    val input = Input.get(path).withHeaders((API.AuthHeader, Main.secretAuthToken))
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(true))
  }

  test("Authenticate false") {
    val input = Input.get(path).withHeaders((API.AuthHeader, "Fake Token"))
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(false))
  }

  test("Missing Authenticate header") {
    val input = Input.get(path)
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(false))
  }
}
