package scalether.domain.json

import java.math.BigInteger

import cats.implicits._
import io.circe.{Decoder, DecodingFailure, Encoder}
import io.circe.syntax._

object CirceCodec {
  // This is the hacky part; Circe already defines a Decoder and Encoder for BigInteger
  // Because it's found via companion search we can override it by providing our own imlementation
  // here, but I think it would be nicer to wrap a newtype around BigInteger; that would
  // obviously be a fairly intrusive change though.
  implicit def bigIntegerDecoder: Decoder[BigInteger] =
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

  implicit def bigIntegerEncoder: Encoder[BigInteger] =
    Encoder.instance(value => s"0x${value.toString(16)}".asJson)
}