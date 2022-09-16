import Dependencies._

val root = (project in file(".")).settings(
  name := "kafkaApp",
  version := "0.1",
  scalaVersion := "2.13.8",
  libraryDependencies ++= Seq(
    fs2Kafka,
    ciris,
    catsEffect
  ) ++ listOfCirceLibraries ++ listOfHttp4sLibraries,
  Compile / unmanagedResourceDirectories += baseDirectory.value / "resources"
)
