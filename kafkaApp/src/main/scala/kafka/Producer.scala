package kafka

import cats.Monad
import cats.effect.std.Console
import cats.effect.{Async, Resource}
import config.kafka.Kafka
import fs2.kafka.{
  Acks,
  KafkaProducer,
  ProducerRecord,
  ProducerRecords,
  ProducerResult,
  ProducerSettings,
  Serializer
}
import io.circe.Encoder
import cats.implicits._

trait Producer[F[_], V] {
  def send(key: String, message: V): Resource[F, Unit]
}

object Producer {
  def impl[F[_]: Async: Console, V](
      config: Kafka
  )(implicit encoder: Encoder[V]): Producer[F, V] =
    new Producer[F, V] {

      implicit val keySerializer: Serializer[F, String] =
        Serializer.lift[F, String](value => Async[F].pure(value.getBytes("UTF-8")))
      implicit def valueSerializer: Serializer[F, V] =
        Serializer[F, String]
          .contramap[V](value => encoder(value).noSpaces)

      private[kafka] val producerSettings: ProducerSettings[F, String, V] =
        ProducerSettings(
          keySerializer = Serializer[F, String],
          valueSerializer = Serializer[F, V]
        )
          .withBootstrapServers(config.bootstrapServer)
          .withAcks(Acks.One)

      override def send(key: String, message: V): Resource[F, Unit] = for {
        kafkaProducer <- KafkaProducer.resource[F, String, V](producerSettings)
        _ <- Resource.pure(
          kafkaProducer
            .produce(
              ProducerRecords.one(
                ProducerRecord[String, V](config.topic, key, message)
              )
            )
            .flatten >>
            Console[F]
              .println(
                s"" +
                  s"Message published to topic: ${config.topic}"
              )
        )
      } yield ()

    }
}
