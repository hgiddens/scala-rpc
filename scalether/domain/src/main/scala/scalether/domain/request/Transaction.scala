package scalether.domain.request

import java.math.BigInteger

import cats.implicits._
import io.circe.{Encoder, Json}
import io.circe.syntax._
import io.daonomic.rpc.domain.Binary
import scalether.domain.Address
import scalether.domain.json.CirceCodec._

case class Transaction(to: Address = null,
                       from: Address = null,
                       gas: BigInteger = null,
                       gasPrice: BigInteger = null,
                       value: BigInteger = null,
                       data: Binary = new Binary(Array()),
                       nonce: BigInteger = null) {
  assert(data != null)
}
object Transaction {
  implicit def encoder: Encoder[Transaction] =
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
}