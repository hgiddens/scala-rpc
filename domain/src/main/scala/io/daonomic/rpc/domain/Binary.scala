package io.daonomic.rpc.domain

import io.circe.{Decoder, Encoder}
import io.circe.syntax._
import scalether.util.Hex

case class Binary(bytes: Array[Byte]) extends Bytes

object Binary {
  val empty: Binary = new Binary(Array())

  def apply(): Binary = empty

  def apply(bytes: Array[Byte]): Binary =
    if (bytes == null) new Binary(Array()) else new Binary(bytes)

  def apply(hex: String): Binary =
    if (hex == null) new Binary(Array()) else new Binary(Hex.toBytes(hex))

  implicit def decoder: Decoder[Binary] =
    Decoder.instance(_.as[String]).map(_.trim).map(Binary(_))

  implicit def encoder: Encoder[Binary] =
    Encoder.instance { bytes: Array[Byte] =>
      Hex.prefixed(bytes).asJson
    }.contramap(_.bytes)
}
