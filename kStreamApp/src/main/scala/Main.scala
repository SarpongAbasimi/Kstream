import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.KStream

import java.util.Properties

object Main {

  def main(args: Array[String]): Unit = {
    val builder: StreamsBuilder = new StreamsBuilder()

    val stream: KStream[String, String] = builder.stream("source_topic")

    stream.foreach((key, value) => {
      println(s"(DSL) key:$key -> value:$value")
    })

    val config = new Properties()
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream")
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass)
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass)


    val topology = builder.build()
    val kafStreams = new KafkaStreams(topology, config)

    kafStreams.start()

  }
}
