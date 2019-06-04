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


lazy val gateway = (project in file("gateway"))
  .settings(
    name := "gateway",
    commonSettings
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)


lazy val books = (project in file("books"))
  .settings(
    name := "books",
    commonSettings
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)


lazy val auth = (project in file("auth"))
  .settings(
    name := "auth",
    commonSettings
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)

