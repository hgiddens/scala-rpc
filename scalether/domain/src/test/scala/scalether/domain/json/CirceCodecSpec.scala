package scalether.domain.json

import java.math.BigInteger

import io.circe.parser.parse
import io.circe.syntax._
import org.scalatest.FlatSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks._
import scalether.domain.json.CirceCodec._

final class CirceCodecSpec extends FlatSpec {
  "codec" should "be lawful" in {
    forAll { bigInt: BigInt =>
      val bigInteger = bigInt.bigInteger
      assert(bigInteger.asJson.as[BigInteger] == Right(bigInteger))
    }
  }

  "decoder" should "deserialise BigIntegers" in {
    val expected = new BigInteger("20000000000")
    assert(parse("\"0x4A817C800\"").right.flatMap(_.as[BigInteger]) == Right(expected))
  }

  "encoder" should "serialise BigIntegers" in {
    assert(BigInteger.valueOf(0).asJson.noSpaces == "\"0x0\"")
  }
}
