package scalether.core

import java.math.BigInteger

import cats.implicits._
import io.circe.{Decoder, DecodingFailure, Encoder, Json}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax._
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.request.{LogFilter, OrTopicFilter, SimpleTopicFilter, TopicFilter}
import scalether.domain.{Address, request, response}
import scalether.domain.response.{Block, Log, TransactionReceipt}
import scalether.util.Hex

trait EthereumCirceCodec {
  private val trimmedStringDecoder =
    Decoder.instance(_.as[String]).map(_.trim)

  private def prefixedBytesEncoder[A](f: A => Array[Byte]) =
    Encoder.instance((bytes: Array[Byte]) => Hex.prefixed(bytes).asJson).contramap(f)

  final implicit def addressDecoder: Decoder[Address] =
    trimmedStringDecoder.map(Address(_))

  final implicit def addressEncoder: Encoder[Address] =
    prefixedBytesEncoder(_.bytes)

  // This is the hacky part; Circe already defines a Decoder and Encoder for BigInteger
  // Because it's found via companion search we can override it by providing our own imlementation
  // here, but I think it would be nicer to wrap a newtype around BigInteger; that would
  // obviously be a fairly intrusive change though.
  final implicit def bigIntegerDecoder: Decoder[BigInteger] =
    Decoder.instance { c =>
      // See BigIntegerHexDeserializer
      // This doesn't have the single value array decoding stuff as it doesn't appear to be used.
      val asNumber = Decoder.decodeJavaBigInteger(c)
      val asString = c.as[String].flatMap { text =>
        try {
          Right(scalether.domain.implicits.stringToBigInteger(text.trim))
        } catch {
          case _: IllegalArgumentException =>
            Left(DecodingFailure(s"${text.trim} not a valid representation", c.history))
        }
      }
      asNumber <+> asString
    }

  final implicit def bigIntegerEncoder: Encoder[BigInteger] =
    Encoder.instance(value => s"0x${value.toString(16)}".asJson)

  final implicit def binaryDecoder: Decoder[Binary] =
    trimmedStringDecoder.map(Binary(_))

  final implicit def binaryEncoder: Encoder[Binary] =
    prefixedBytesEncoder(_.bytes)

  final implicit def blockDecoder: Decoder[Block] =
    deriveDecoder

  final implicit def logDecoder: Decoder[Log] =
    deriveDecoder

  final implicit def logFilterEncoder: Encoder[LogFilter] =
    deriveEncoder

  final implicit def requestTransactionEncoder: Encoder[request.Transaction] =
    Encoder.instance { tx =>
      def optionalProperty[A: Encoder](name: String, a: A): Option[(String, Json)] =
        Option(a).map(_.asJson).tupleLeft(name)
      val properties = ("data", tx.data.asJson) +: Seq(
        optionalProperty("to", tx.to),
        optionalProperty("from", tx.from),
        optionalProperty("gas", tx.gas),
        optionalProperty("gasPrice", tx.gasPrice),
        optionalProperty("value", tx.value),
        optionalProperty("nonce", tx.nonce)
      ).flatten
      Json.obj(properties: _*)
    }

  final implicit def responseTransactionDecoder: Decoder[response.Transaction] =
    deriveDecoder

  final implicit def topicFilterEncoder: Encoder[TopicFilter] =
    Encoder.instance {
      case SimpleTopicFilter(word) => word.toString.asJson
      case OrTopicFilter(words) => words.map(_.toString).asJson
    }

  final implicit def transactionReceiptDecoder: Decoder[TransactionReceipt] =
    deriveDecoder

  final implicit def wordDecoder: Decoder[Word] =
    trimmedStringDecoder.map(Word(_))

  final implicit def wordEncoder: Encoder[Word] =
    prefixedBytesEncoder(_.bytes)
}
object EthereumCirceCodec extends EthereumCirceCodec