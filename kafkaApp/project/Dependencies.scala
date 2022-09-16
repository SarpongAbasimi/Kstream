import sbt._

object Dependencies {

  private val fs2KafkaVersion   = "3.0.0-M8"
  private val cirisVersion      = "2.3.3"
  private val catsEffectVersion = "3.3.12"
  private val catsVersion       = "2.7.0"
  private val circeVersion      = "0.14.1"
  private val http4sVersion     = "0.23.15"

  val fs2Kafka   = "com.github.fd4s" %% "fs2-kafka"   % fs2KafkaVersion
  val ciris      = "is.cir"          %% "ciris"       % cirisVersion
  val catsEffect = "org.typelevel"   %% "cats-effect" % catsEffectVersion
  val cats       = "org.typelevel"   %% "cats-core"   % catsVersion

  val listOfCirceLibraries = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser",
    "io.circe" %% "circe-generic-extras"
  ).map(_ % circeVersion)

  val listOfHttp4sLibraries = Seq(
    "org.http4s" %% "http4s-dsl",
    "org.http4s" %% "http4s-ember-server",
    "org.http4s" %% "http4s-ember-client",
    "org.http4s" %% "http4s-circe"
  ).map(_ % http4sVersion)
}
