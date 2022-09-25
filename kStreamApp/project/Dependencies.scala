import sbt._

object Dependencies {
  private val kafkaStreamsVersion = "3.2.3"
  private val circeVersion        = "0.14.1"

  val kafkaStream = Seq(
    "org.apache.kafka" %% "kafka-streams-scala",
    "org.apache.kafka"  % "kafka-streams"
  ).map(_ % kafkaStreamsVersion)

  val listOfCirceLibraries = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-generic-extras"
  ).map(_ % circeVersion)
}
