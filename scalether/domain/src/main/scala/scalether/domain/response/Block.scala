package scalether.domain.response

import java.math.BigInteger

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.Address
import scalether.domain.json.CirceCodec._

case class Block(number: BigInteger,
                 hash: Word,
                 parentHash: Word,
                 nonce: String,
                 sha3Uncles: String,
                 logsBloom: String,
                 transactionsRoot:String,
                 stateRoot: String,
                 miner: Address,
                 difficulty: BigInteger,
                 totalDifficulty: BigInteger,
                 extraData: Binary,
                 size: BigInteger,
                 gasLimit: BigInteger,
                 gasUsed: BigInteger,
                 transactions: List[Word],
                 timestamp: BigInteger)
object Block {
  implicit def decoder: Decoder[Block] =
    deriveDecoder
}