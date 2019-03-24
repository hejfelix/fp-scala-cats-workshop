import cats.effect.Effect
import random.RandomClient
import cats.implicits._

class PiCalculator[F[_]: Effect](sampleSize: Int, randomClient: RandomClient[F]) {

  private def getRandomNumber(min: Double, max: Double): F[Double] =
    randomClient.get(min, max).map(_.unsafeBody.right.get)

  private def requestRandomNumbers(n: Int): F[List[Double]] =
    List
      .fill(n)((0d, 1d))
      .traverse(
        (getRandomNumber _).tupled
      )

  def calcPi: F[Double] =
    requestRandomNumbers(sampleSize)
      .map(xs => xs.zip(xs.tail))
      .map(_.count {
        case (x, y) => math.sqrt(x * x + y * y) <= 1
      })
      .map(4 * _.toDouble / sampleSize)

}
