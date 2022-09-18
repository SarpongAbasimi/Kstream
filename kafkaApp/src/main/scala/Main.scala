import cats.effect.{ExitCode, IO, IOApp, Resource}
import config.kafka.AppConfig
import server.AppServer
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp.Simple {
  implicit def logger: Logger[IO] = Slf4jLogger.getLogger[IO]
  override def run: IO[Unit] =
    (for {
      config <- Resource.eval(AppConfig.conf[IO].load[IO])
      app    <- AppServer.resource[IO](config)
    } yield app)
      .use(_ => IO.never)
      .as(ExitCode.Success)

}
