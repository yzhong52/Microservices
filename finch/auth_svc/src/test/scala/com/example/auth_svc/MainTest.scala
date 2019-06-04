package com.example.auth_svc

import com.example.auth_svc.Main.AuthResponse
import io.finch._
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  private val path = "/"

  test("Authenticate success") {
    val input = Input.get(path).withHeaders((Main.authHeader, Main.secretAuthToken))
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(true))
  }

  test("Authenticate false") {
    val input = Input.get(path).withHeaders((Main.authHeader, "Fake Token"))
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(false))
  }

  test("Missing Authenticate header") {
    val input = Input.get(path)
    assert(Main.authenticate(input).awaitValueUnsafe().get == AuthResponse(false))
  }
}
