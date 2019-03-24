import cats.effect.{ExitCode, IO, IOApp}
import com.softwaremill.sttp.asynchttpclient.cats.AsyncHttpClientCatsBackend
import com.softwaremill.sttp.{SttpBackend, _}
import random.{RandomClient, RandomServer}

import scala.concurrent.duration.MILLISECONDS

object Main extends IOApp {

  private val baseUri = uri"127.0.0.1:8080"

  private implicit val clientBackend: SttpBackend[IO, Nothing] =
    AsyncHttpClientCatsBackend[IO]()

  private val randomServer: RandomServer[IO] = new RandomServer[IO](baseUri.host, port = 8080)
  private val randomClient: RandomClient[IO] = new RandomClient[IO](baseUri)
  private val piCalculator: PiCalculator[IO] = new PiCalculator[IO](sampleSize = 10000, randomClient = randomClient)

  private def time[T](sectionName: String)(section: IO[T]): IO[T] =
    for {
      start  <- timer.clock.monotonic(MILLISECONDS)
      result <- section
      end    <- timer.clock.monotonic(MILLISECONDS)
      _      <- IO.delay(println(s"Time for section $sectionName: ${end - start}ms"))
    } yield result

  override def run(args: List[String]): IO[ExitCode] = {
    val _ = (args)

    for {
      fiber    <- randomServer.run.start
      pi       <- time(sectionName = "pi")(piCalculator.calcPi)
      _        <- IO.delay(println(pi))
      exitCode <- fiber.join
    } yield exitCode

  }

}
