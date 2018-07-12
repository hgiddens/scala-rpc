package scalether.core

import java.math.BigInteger

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.parser.parse
import io.circe.syntax._
import io.daonomic.rpc.JsonConverter
import org.scalacheck.Gen
import org.scalatest.{Assertion, FlatSpec, FlatSpecLike}
import org.scalatest.prop.GeneratorDrivenPropertyChecks._
import scalether.core.EthereumCirceCodec._
import scalether.core.json.EthereumJacksonModule
import scalether.domain.response
import scalether.domain.response.{Block, Log, TransactionReceipt}
import scalether.test.Generators._

import scala.util.Try

trait CirceCodecSpec { _: FlatSpecLike =>
  protected final def converter = new JsonConverter(new EthereumJacksonModule)

  protected final def checkSerialisation[A <: AnyRef: Encoder](generator: Gen[A]): Assertion =
    forAll(generator) { a =>
      val circe = a.asJson
      val jackson = parse(converter.toJson(a))
      assert(Right(circe) == jackson)
    }

  protected final def checkDeserialisation[A <: AnyRef: Decoder: Encoder: Manifest](generator: Gen[A]): Unit = {
    def parseWithJackson(json: String): Either[Throwable, A] =
      Try(converter.fromJson(json)).toEither
    def parseWithCirce(json: String): Either[Throwable, A] =
      parse(json).right.flatMap(_.as[A])

    forAll(generator) { a =>
      val viaJackson = converter.toJson(a)
      val viaCirce = a.asJson.noSpaces

      assert(parseWithCirce(viaJackson).contains(a))
      assert(parseWithJackson(viaJackson).contains(a))
      assert(parseWithCirce(viaCirce).contains(a))
      assert(parseWithJackson(viaCirce).contains(a))
    }
  }

  protected final def checkSer[A <: AnyRef: Encoder](name: String, generator: Gen[A]): Unit = {
    name should "be serialised by Circe and Jackson identically" in checkSerialisation(generator)
  }

  protected final def checkSerDes[A <: AnyRef: Decoder: Encoder: Manifest](name: String, generator: Gen[A]): Unit = {
    name should "be serialised by Circe and Jackson identically" in checkSerialisation(generator)
    it should "be deserialised by Circe and Jackson identically" in checkDeserialisation(generator)
  }

  protected final implicit def blockEncoder: Encoder[Block] = deriveEncoder
  protected final implicit def logEncoder: Encoder[Log] = deriveEncoder
  protected final implicit def responseTransactionEncoder: Encoder[response.Transaction] = deriveEncoder
  protected final implicit def transactionReceiptEncoder: Encoder[TransactionReceipt] = deriveEncoder
}

final class EthereumCirceCodecSpec extends FlatSpec with CirceCodecSpec {
  "BigInteger" should "be serialised like jackson does it" in {
    forAll(bigInteger) { bigInteger =>
      val circe = bigInteger.asJson.noSpaces
      val jackson = converter.toJson(bigInteger)
      assert(circe == jackson)
    }
  }

  it should "be round tripped through the decoder" in {
    forAll(bigInteger) { bigInteger =>
      assert(bigInteger.asJson.as[BigInteger] == Right(bigInteger))
    }
  }

  checkSerDes("Address", address)
  checkSerDes("Binary", binary)
  checkSerDes("Block", block)
  checkSer("LogFilter", logFilter)
  checkSer("request.Transaction", requestTransaction)
  checkSerDes("response.Transaction", responseTransaction)
  checkSer("TopicFilter", topicFilter)
  checkSerDes("TransactionReceipt", transactionReceipt)
  checkSerDes("Word", word)
}
