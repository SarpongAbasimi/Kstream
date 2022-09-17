package config.kafka

import cats.MonadThrow
import cats.implicits._
import ciris._

final case class Kafka(bootstrapServer: String, topic: String)

object Kafka {
  def load[F[_]: MonadThrow]: ConfigValue[F, Kafka] = (
    env("BOOTSTRAP_SERVER")
      .default("localhost:9092"),
    env("TOPIC").default("source_topic")
  )
    .parMapN(Kafka(_, _))
}
