package io.daonomic.rpc.domain

import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import scalether.util.Hex

case class Word(bytes: Array[Byte]) extends Bytes {
  assert(bytes.length == 32)

  def toBinary: Binary = Binary(bytes)
}

object Word {
  def apply(hex: String): Word =
    Word(Hex.toBytes(hex))

  def apply(binary: Binary): Word =
    Word(binary.bytes)

  implicit def decoder: Decoder[Word] =
    Decoder.instance(_.as[String]).map(_.trim).map(Word(_))

  implicit def encoder: Encoder[Word] =
    Encoder.instance { bytes: Array[Byte] =>
      Hex.prefixed(bytes).asJson
    }.contramap(_.bytes)
}
