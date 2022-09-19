package kafka

import cats.effect.kernel.Async
import org.typelevel.log4cats.Logger
import cats.implicits._

case class RandomStringProducer[F[_]: Async, V](
    private val kProducer: KafkaProducerClass[F, V]
)(implicit logger: Logger[F]) {
  def publish(key: String, message: V): F[Unit] = {
    kProducer
      .send(key, message)
      .use(producerResultF =>
        for {
          producerResult <- producerResultF
          _ <- fs2.Stream
            .chunk(producerResult.records)
            .covary[F]
            .compile
            .toList
            .flatMap(recordMetas => {
              recordMetas.traverse { result =>
                val (records, _) = result
                logger.info(
                  s"""Message published to topic ${records.topic}
                     |with key ${records.key}""".stripMargin
                )
              }
            })
        } yield ()
      )
  }
}
