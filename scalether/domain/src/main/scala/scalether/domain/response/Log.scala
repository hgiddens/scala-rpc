package scalether.domain.response

import java.math.BigInteger

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.Address
import scalether.domain.json.CirceCodec._

case class Log(logIndex: BigInteger,
               transactionIndex: BigInteger,
               transactionHash: Word,
               blockHash: Word,
               blockNumber: BigInteger,
               address: Address,
               data: Binary,
               topics: List[Word],
               `type`: String)
object Log {
  implicit def decoder: Decoder[Log] =
    deriveDecoder
}