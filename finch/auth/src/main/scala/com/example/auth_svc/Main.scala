package com.example.auth_svc

import cats.effect.IO
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.finch.catsEffect._
import io.finch.circe._

object Main extends App {
  def secretAuthToken = "SUPERSECUREAUTTHTOKEN"

  def authHeader = "Authorization"

  case class AuthResponse(ok: Boolean)

  def authenticate: Endpoint[IO, AuthResponse] = get(header(authHeader)) { token: String =>
    if (token == secretAuthToken) Ok(AuthResponse(true))
    else Ok(AuthResponse(false))
  }.handle {
    case e: Error.NotPresent =>
      Console.err.print(s"Missing header $authHeader $e")
      Ok(AuthResponse(false))
  }

  def service: Service[Request, Response] = Bootstrap
    .serve[Application.Json](authenticate)
    .toService

  Await.ready(Http.server.serve(":8080", service))
}
