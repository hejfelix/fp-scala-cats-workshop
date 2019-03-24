import cats.data.NonEmptyList
import cats.effect.{ContextShift, Effect}
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.Location
import org.http4s.server.staticcontent.WebjarService.{Config, WebjarAsset}
import org.http4s.server.staticcontent.webjarService
import org.http4s.{HttpRoutes, Uri}
import cats.implicits._
import tapir.openapi.circe.yaml._
import tapir.docs.openapi._

abstract class SwaggerUiRoute[F[_]: Effect: ContextShift](api: Api,
                                                          title: String,
                                                          version: String) {

  private val yamlDocAsString = api.getEndpoint.toOpenAPI(title, version).toYaml

  private val dsl = new Http4sDsl[F] {}

  def docsRoute: HttpRoutes[F] =
    NonEmptyList.of(webjarsRoute, docsRedirectRoute, openApiRoute).reduceK

  private def isAsset(asset: WebjarAsset): Boolean =
    List(".js", ".css", ".html").exists(asset.asset.endsWith)

  private val webjarsRoute: HttpRoutes[F] = webjarService[F](
    Config(
      blockingExecutionContext = scala.concurrent.ExecutionContext.global,
      filter = isAsset
    )
  )

  private def docsRedirectRoute: HttpRoutes[F] = {
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "docs" =>
        PermanentRedirect(
          Location(Uri.uri("/swagger-ui/3.20.9/index.html?url=/openapi.yml#/"))
        )
    }
  }

  private def openApiRoute: HttpRoutes[F] = {
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "openapi.yml" => Ok(yamlDocAsString)
    }
  }
}
