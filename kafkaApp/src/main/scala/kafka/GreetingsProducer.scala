package kafka
import config.kafka.Kafka

case class GreetingsProducer[F[_], V](kafkaConfig: Kafka) extends Producer[F, V] {
  override def send(message: V): F[Unit] = ???

}
