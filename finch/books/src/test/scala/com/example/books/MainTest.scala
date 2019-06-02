package com.example.books

import io.finch._
import org.scalatest.FunSuite

class MainTest extends FunSuite {
  test("healthcheck") {
    assert(Main.healthcheck(Input.get("/")).awaitValueUnsafe() == Some("OK"))
  }

  test("helloWorld") {
    assert(Main.helloWorld(Input.get("/hello")).awaitValueUnsafe() == Some(Main.Message("World")))
  }

  test("hello") {
    assert(Main.hello(Input.get("/hello/foo")).awaitValueUnsafe() == Some(Main.Message("foo")))
  }
}