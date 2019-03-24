import cats.effect._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

object Main extends IOApp {

  private val hostName = "127.0.0.1"
  private val port = 8080

  private val api = new Api(basePath = "random")
  private val implementation = new Implementation[IO](api)

  private val runServer = BlazeServerBuilder[IO]
    .bindHttp(port, hostName)
    .withHttpApp(implementation.service.orNotFound)
    .serve
    .compile
    .lastOrError

  override def run(args: List[String]): IO[ExitCode] = {
    val _ = args // We don't need these
    runServer
  }

}
