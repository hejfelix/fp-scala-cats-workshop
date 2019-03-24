package random

import cats.data.NonEmptyList
import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes

import scala.util.Random
import tapir.server.http4s._

class Implementation[F[_]: Effect: ContextShift](api: RandomApi)
    extends SwaggerUiRoute[F](api, title = "Random", version = "1.0") {

  def service: HttpRoutes[F] =
    NonEmptyList
      .of(api.getEndpoint.toRoutes(random _), docsRoute)
      .reduceK

  private def random(min: Double, max: Double): F[Either[String, Double]] =
    Sync[F].delay((min + Random.nextDouble() * (max - min)).asRight)

}
