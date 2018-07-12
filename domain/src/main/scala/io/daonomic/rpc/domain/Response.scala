package io.daonomic.rpc.domain

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Response[T](id: Long,
                       result: Option[T],
                       error: Option[Error])
object Response {
  implicit def decoder[T: Decoder]: Decoder[Response[T]] =
    deriveDecoder
}