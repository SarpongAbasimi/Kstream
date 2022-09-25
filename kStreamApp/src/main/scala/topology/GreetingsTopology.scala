package topology
import org.apache.kafka.streams.{StreamsBuilder, Topology}
import org.apache.kafka.streams.kstream.KStream

object GreetingsTopology {
  def build: Topology = {
    val streamsBuilder: StreamsBuilder = new StreamsBuilder()

    val kStream: KStream[Array[Byte], Array[Byte]] = streamsBuilder.stream("source_topic")
    kStream.foreach { (k, v) =>
      println(s"Key is $k and value is $v")
    }
    streamsBuilder.build()
  }
}
