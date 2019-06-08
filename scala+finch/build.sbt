val finchVersion = "0.26.0"
val circeVersion = "0.10.1"
val scalatestVersion = "3.0.5"

ThisBuild / organization := "com.example"
ThisBuild / version := "latest"
ThisBuild / scalaVersion := "2.12.8"

lazy val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "com.github.finagle" %% "finchx-core" % finchVersion,
    "com.github.finagle" %% "finchx-circe" % finchVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  )
)

lazy val root = (project in file("."))
  .aggregate(gateway, books, auth)

lazy val helpers = (project in file("helpers"))
  .settings(
    name := "helpers",
    commonSettings
  )

lazy val gateway = (project in file("gateway_svc"))
  .settings(
    name := "gateway",
    libraryDependencies += "io.circe" %% "circe-parser" % circeVersion,
    commonSettings
  ).dependsOn(helpers)
  .enablePlugins(DockerPlugin, JavaAppPackaging)


lazy val books = (project in file("books_svc"))
  .settings(
    name := "books",
    commonSettings
  ).dependsOn(helpers)
  .enablePlugins(DockerPlugin, JavaAppPackaging)


lazy val auth = (project in file("auth_svc"))
  .settings(
    name := "auth",
    commonSettings
  ).dependsOn(helpers)
  .enablePlugins(DockerPlugin, JavaAppPackaging)

