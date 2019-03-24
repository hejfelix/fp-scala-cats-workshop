package random

import tapir.MediaType.TextPlain

class RandomApi(basePath: String) {

  import tapir._

  lazy val getEndpoint: Endpoint[(Double, Double), String, Double, Nothing] =
    endpoint.get
      .in(basePath)
      .in(query[Double](name = "from"))
      .in(query[Double](name = "to"))
      .out(body[Double, TextPlain])
      .errorOut(stringBody)
      .description("Get a random number")

}
