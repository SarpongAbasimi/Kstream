import Dependencies._

val root = (project in file("."))
  .settings(
    name := "kStreamApp",
    scalaVersion := "2.13.8",
    version := "0.1",
    libraryDependencies ++= Seq(
      kafkaStream
    )
  )