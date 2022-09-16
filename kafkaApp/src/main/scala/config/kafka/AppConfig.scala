package config.kafka

import cats.MonadThrow
import ciris.ConfigValue

final case class AppConfig(
    kafkaConfig: Kafka
)

object AppConfig {
  def conf[F[_]: MonadThrow]: ConfigValue[F, AppConfig] =
    Kafka.load[F].map(AppConfig(_))
}
