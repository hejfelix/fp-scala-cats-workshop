package random
import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, Timer}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
class RandomServer[F[_]: ConcurrentEffect: ContextShift: Timer](
    hostName: String,
    port: Int
) {

  private val api            = new RandomApi(basePath = "random")
  private val implementation = new Implementation[F](api)

  val run: F[ExitCode] = BlazeServerBuilder[F]
    .bindHttp(port, hostName)
    .withHttpApp(implementation.service.orNotFound)
    .serve
    .compile
    .lastOrError

}
