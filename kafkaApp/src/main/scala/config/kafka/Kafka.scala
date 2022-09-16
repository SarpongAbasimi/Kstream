package config.kafka

import cats.MonadThrow
import ciris._

final case class Kafka(bootstrapServer: String)

object Kafka {
  def load[F[_]: MonadThrow]: ConfigValue[F, Kafka] = (
    env("BOOTSTRAP_SERVER")
      .default("localhost:9092"),
    )
    .map(Kafka(_))
}
