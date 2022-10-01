package topology
import models.Greetings
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.scala.StreamsBuilder
import org.apache.kafka.streams.scala.kstream.{Branched, KStream}
import org.apache.kafka.streams.kstream.{Consumed, Named}
import serde.CustomSerde
import models.Greetings.{GoodAfternoon, GoodEvening, GoodMorning}
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
        case GoodMorning(to)
            if to.toLowerCase
              == "chris" |
              to.toLowerCase == "ben" =>
          true
        case GoodAfternoon(_) => true
        case _                => false
      }
    })

    val theBranch = filteredStream
      .split(
        Named.as(
          "Greet-"
        )
      )
      .branch(
        (_, v) =>
          v match {
            case GoodMorning(to) if to.toLowerCase.startsWith("s") => true
            case _                                                 => false
          },
        Branched.as("GoodMorning")
      )
      .branch(
        (_, v) =>
          v match {
            case GoodEvening(to) if to.toLowerCase.startsWith("v") => true
            case _                                                 => false
          },
        Branched.as("GoodEvening")
      )
      .defaultBranch(Branched.as("Others"))

    // Getting data for each branch
    val getBranchGoodMorning: Option[KStream[String, Greetings]] =
      theBranch.get("Greet-GoodMorning")

    val getBranchEvening: Option[KStream[String, Greetings]] =
      theBranch.get("Greet-GoodEvening")

    val getOthers: Option[KStream[String, Greetings]] =
      theBranch.get("Greet-Others")

    // Printing data from branch
    getBranchGoodMorning.collect { case value =>
      value.foreach { (k, v) =>
        println("Learning about Branches in KafkaStream! -> getBranchGoodMorning")
        println(s"The key is $k and the value is ${v}")
      }
    }

    getBranchEvening.collect { case value =>
      value.foreach { (k, v) =>
        println("Learning about Branches in KafkaStream! -> getBranchEvening")
        println(s"The key is $k and the value is ${v}")
      }
    }

    getOthers.collect { case value =>
      value.foreach { (k, v) =>
        println("Learning about Branches in KafkaStream! -> getOthers")
        println(s"The key is $k and the value is ${v}")
      }
    }

    streamsBuilder.build()
  }
}
