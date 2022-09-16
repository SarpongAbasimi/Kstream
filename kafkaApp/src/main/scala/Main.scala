import cats.effect.{ExitCode, IO, IOApp, Resource}
import config.kafka.AppConfig
import server.AppServer

object Main extends IOApp.Simple {
  override def run: IO[Unit] =
    (for {
      config <- Resource.eval(AppConfig.conf[IO].load[IO])
      app    <- AppServer.resource[IO](config)
    } yield app)
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
