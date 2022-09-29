package serde

import io.circe.parser.decode
import io.circe.{Decoder, Encoder}
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.scala.serialization.{Serdes => ScalaSedes}
import java.nio.charset.StandardCharsets

object CustomSerde {
  def create[T >: Null: Encoder: Decoder]: Serde[T] = {
    val serializer: T => Array[Byte] = data =>
      Encoder
        .apply[T]
        .apply(data)
        .noSpaces
        .getBytes

    val deserializer: Array[Byte] => Option[T] = bytesArray =>
      decode[T](new String(bytesArray, StandardCharsets.UTF_8)) match {
        case Left(_) =>
          None
        case Right(value) =>
          Some(value)
      }
    ScalaSedes.fromFn[T](serializer, deserializer)
  }
}
