package server

import cats.effect.Resource
import cats.effect.Async
import config.kafka.AppConfig
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._
import kafka.{GreetingsProducer, KafkaProducerClass, RandomStringProducer}
import org.http4s.server.Server
import routes.{GreetRoutes, RandomStringRoutes}
import models.Greetings
import org.typelevel.log4cats.Logger
import cats.implicits._

object AppServer {
  def resource[F[_]: Async](
      config: AppConfig
  )(implicit logger: Logger[F]): Resource[F, Server] = for {
    _ <- Resource.eval(logger.info(s"Starting server 🚀 with config $config"))

    kafkaProducerGreet: KafkaProducerClass[F, Greetings] =
      KafkaProducerClass[F, Greetings](
        config.kafkaConfig
      )

    kafkaProducerRandomString: KafkaProducerClass[F, String] =
      KafkaProducerClass[F, String](
        config.kafkaConfig
      )

    greetProducer: GreetingsProducer[F, Greetings] =
      GreetingsProducer[F, Greetings](kafkaProducerGreet)

    randomStringProducer: RandomStringProducer[F, String] =
      RandomStringProducer[F, String](kafkaProducerRandomString)

    routes = (
      GreetRoutes
        .greet[F](greetProducer) <+>
        RandomStringRoutes
          .sayHello[F](randomStringProducer)
    ).orNotFound

    server <- EmberServerBuilder
      .default[F]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8000")
      .withHttpApp(routes)
      .build
  } yield server
}
