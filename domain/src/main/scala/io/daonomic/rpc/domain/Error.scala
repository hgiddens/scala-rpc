package io.daonomic.rpc.domain

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Error(code: Int, message: String, data: Option[String] = None) {
  def fullMessage: String = message + data.map(": " + _).getOrElse("")
}

object Error {
  val default: Error = new Error(0, "No result received")

  implicit def decoder: Decoder[Error] =
    deriveDecoder
}
