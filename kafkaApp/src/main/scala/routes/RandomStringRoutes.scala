package routes

import cats.effect.Concurrent
import kafka.RandomStringProducer
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object RandomStringRoutes {
  def sayHello[F[_]: Concurrent](
      randomStringProducer: RandomStringProducer[F, String]
  ): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] { case req @ GET -> Root / name =>
      randomStringProducer
        .publish(
          name,
          s"Hello $name"
        ) *> Ok(s"Hello $name")
    }
  }
}
