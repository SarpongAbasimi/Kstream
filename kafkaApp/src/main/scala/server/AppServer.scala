package server

import cats.effect.std.Console
import cats.effect.{Async, Resource}
import config.kafka.AppConfig
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._
import kafka.{GreetingsProducer, KafkaProducerClass}
import org.http4s.server.Server
import routes.Routes
import models.Greetings

object AppServer {
  def resource[F[_]: Async: Console](config: AppConfig): Resource[F, Server] = for {
    _ <- Resource.eval(Console[F].println(s"Starting server 🚀 with config $config"))

    kafkaProducer: KafkaProducerClass[F, Greetings] =
      KafkaProducerClass[F, Greetings](
        config.kafkaConfig
      )
    greetProducer: GreetingsProducer[F, Greetings] =
      GreetingsProducer[F, Greetings](kafkaProducer)

    server <- EmberServerBuilder
      .default[F]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(
        Routes
          .greet[F](greetProducer)
          .orNotFound
      )
      .build
  } yield server
}
