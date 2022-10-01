package topology
import models.Greetings
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.KStream
import org.apache.kafka.streams.kstream.Consumed
import serde.CustomSerde
import models.Greetings.{GoodAfternoon, GoodMorning}
import org.apache.kafka.streams.scala.serialization.Serdes

object GreetingsTopology {
  def build: Topology = {
    val streamsBuilder: StreamsBuilder = new StreamsBuilder()

    implicit val consumeForKV: Consumed[String, Greetings] =
      Consumed.`with`(
        Serdes.stringSerde,
        CustomSerde.create[Greetings]
      )

    val kStream: KStream[String, Greetings] = streamsBuilder
      .stream[String, Greetings](
        "source_topic"
      )

    val filteredStream = kStream.filterNot((_, value: Greetings) => {
      value match {
        case GoodMorning(to) if to.toLowerCase == "chris" | to.toLowerCase == "ben" =>
          println(s"Skipping ${value.toString} with name $to"); true
        case GoodAfternoon(_) => println(s"Skipping GoodAfternoon"); true
        case _                => println(s"Bingo 🎉"); false
      }
    })

    filteredStream.foreach { (k, v) =>
      println(s"The key is  $k and value is $v")
    }

    streamsBuilder.build()
  }
}
