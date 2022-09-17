package routes
import org.http4s._
import org.http4s.dsl.Http4sDsl
import cats.effect.Concurrent
import models.Greetings
import org.http4s.circe.jsonOf
import org.http4s.circe.jsonEncoderOf

object Routes {
  def greet[F[_]: Concurrent]: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._

    implicit val greetingEntityDecoder: EntityDecoder[F, Greetings] = jsonOf[F, Greetings]
    implicit val greetingEntityEncode: EntityEncoder[F, Greetings]  = jsonEncoderOf[F, Greetings]

    HttpRoutes.of[F] { case req @ POST -> Root / "greet" =>
      req.decodeStrict[Greetings](Created(_))
    }
  }
}
