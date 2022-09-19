import sbt._

object Dependencies {
  private val kafkaStreamsVersion = "3.2.3"


  val kafkaStream =  "org.apache.kafka" % "kafka-streams" % kafkaStreamsVersion
}
