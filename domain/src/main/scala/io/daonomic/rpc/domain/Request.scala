package io.daonomic.rpc.domain

import io.circe.{Encoder, Json}
import io.circe.generic.semiauto._

case class Request(id: Long,
                   method: String,
                   params: List[Json],
                   jsonrpc: String = "2.0")

object Request {
  def apply(id: Long, method: String, params: Json*): Request =
    new Request(id, method, params.toList)

  implicit def encoder: Encoder[Request] =
    deriveEncoder
}
