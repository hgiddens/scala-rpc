package scalether.core

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import scalether.domain.response.parity.{Action, ActionResult, Trace}

object ParityCirceCodec extends EthereumCirceCodec {
  implicit def actionDecoder: Decoder[Action] =
    deriveDecoder

  implicit def actionResultDecoder: Decoder[ActionResult] =
    deriveDecoder

  implicit def traceDecoder: Decoder[Trace] =
    deriveDecoder
}
