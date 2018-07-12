package scalether.domain.response.parity

import java.math.BigInteger

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.daonomic.rpc.domain.Word
import scalether.domain.Address
import scalether.domain.json.CirceCodec._

case class Trace(action: Action,
                 blockHash: Word,
                 blockNumber: BigInteger,
                 result: ActionResult,
                 subtraces: Int,
                 traceAddress: List[Int],
                 transactionHash: Word,
                 transactionPosition: Int,
                 error: String,
                 `type`: String)
object Trace {
  implicit def decoder: Decoder[Trace] =
    deriveDecoder
}

case class Action(callType: String, from: Address, gas: BigInteger, input: String, to: Address, value: BigInteger)
object Action {
  implicit def decoder: Decoder[Action] =
    deriveDecoder
}

case class ActionResult(gasUsed: BigInteger, output: String, address: Address)
object ActionResult {
  implicit def decoder: Decoder[ActionResult] =
    deriveDecoder
}