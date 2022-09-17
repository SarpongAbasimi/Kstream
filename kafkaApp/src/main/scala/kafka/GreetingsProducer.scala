package kafka
import cats.effect.kernel.Async

case class GreetingsProducer[F[_]: Async, V](
    private val kProducer: KafkaProducerClass[F, V]
) {
  def publish(key: String, message: V): F[Unit] =
    kProducer.send(key, message)
}
