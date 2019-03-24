package random
import com.softwaremill.sttp.{Response, SttpBackend, Uri}
import tapir.client.sttp._

class RandomClient[F[_]](baseUri: Uri)(implicit backend: SttpBackend[F, _]) {

  private val basePath = "random"
  private val randomApi = new RandomApi(basePath)

  def get(min: Double, max: Double): F[Response[Either[String, Double]]] =
    randomApi.getEndpoint.toSttpRequest(baseUri).apply(min, max).send()

}
