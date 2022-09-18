package kafka

import cats.effect.Async
import cats.effect.Resource
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
import org.typelevel.log4cats.Logger

trait Producer[F[_], V] {
  def send(key: String, message: V): Resource[F, F[ProducerResult[Unit, String, V]]]
}

case class KafkaProducerClass[F[_]: Async, V](config: Kafka)(implicit
    encoder: Encoder[V],
    logger: Logger[F]
) extends Producer[F, V] {

  private[kafka] implicit val keySerializer: Serializer[F, String] =
    Serializer.lift[F, String](value =>
      Async[F]
        .pure(value.getBytes("UTF-8"))
    )
  private[kafka] implicit def valueSerializer: Serializer[F, V] =
    Serializer[F, String]
      .contramap[V](value => encoder(value).noSpaces)

  private[kafka] val producerSettings: ProducerSettings[F, String, V] =
    ProducerSettings(
      keySerializer = Serializer[F, String],
      valueSerializer = Serializer[F, V]
    )
      .withBootstrapServers(config.bootstrapServer)
      .withAcks(Acks.One)

  override def send(
      key: String,
      message: V
  ): Resource[F, F[ProducerResult[Unit, String, V]]] =
    for {
      kafkaProducer <- KafkaProducer.resource[F, String, V](producerSettings)
      producerResultF <- Resource.eval(
        kafkaProducer
          .produce(
            ProducerRecords.one(
              ProducerRecord[String, V](config.topic, key, message)
            )
          )
      )
    } yield producerResultF
}
