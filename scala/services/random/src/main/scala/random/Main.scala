package random
import cats.effect._

object Main extends IOApp {

  private val hostName = "127.0.0.1"
  private val port     = 8080

  private val server = new RandomServer[IO](hostName, port)

  override def run(args: List[String]): IO[ExitCode] = {
    val _ = args // We don't need these
    server.run
  }

}
