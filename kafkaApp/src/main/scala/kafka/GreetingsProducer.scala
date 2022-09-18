package kafka
import cats.effect.kernel.Async
import org.typelevel.log4cats.Logger
import cats.implicits._

case class GreetingsProducer[F[_]: Async, V](
    private val kProducer: KafkaProducerClass[F, V]
)(implicit logger: Logger[F]) {
  def publish(key: String, message: V): F[Unit] =
    kProducer.send(key, message).use(_ => Async[F].unit) >> logger.info(
      "Done£££££££££££££"
    )
}
