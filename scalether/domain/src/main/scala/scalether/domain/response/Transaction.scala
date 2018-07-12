package scalether.domain.response

import java.math.BigInteger

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.Address
import scalether.domain.json.CirceCodec._

case class Transaction(hash: Word,
                       nonce: BigInteger,
                       blockHash: Word,
                       blockNumber: BigInteger,
                       transactionIndex: BigInteger,
                       from: Address,
                       to: Address,
                       value: BigInteger,
                       gasPrice: BigInteger,
                       gas: BigInteger,
                       input: Binary)
object Transaction {
  implicit def decoder: Decoder[Transaction] =
    deriveDecoder
}