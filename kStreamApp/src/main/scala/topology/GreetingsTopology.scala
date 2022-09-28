package topology
import models.Greetings
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.scala.kstream.Consumed
import serde.CustomSerde

object GreetingsTopology {
  def build: Topology = {
    val streamsBuilder: StreamsBuilder = new StreamsBuilder()

    implicit val consumeForKV: Consumed[String, Greetings] =
      Consumed.`with`(
        CustomSerde.create[String],
        CustomSerde.create[Greetings]
      )

    val kStream: KStream[String, Greetings] = streamsBuilder
      .stream[String, Greetings](
        "source_topic"
      )

    kStream.foreach { (k, v) =>
      println(s"Key is $k and value is $v")
    }

    streamsBuilder.build()
  }
}
