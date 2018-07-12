package scalether.core

import io.circe.Encoder
import io.circe.generic.semiauto._
import org.scalatest.FlatSpec
import scalether.core.ParityCirceCodec._
import scalether.domain.response.parity.{Action, ActionResult, Trace}
import scalether.test.Generators._

final class ParityCirceCodecSpec extends FlatSpec with CirceCodecSpec {
  private implicit def actionEncoder: Encoder[Action] = deriveEncoder
  private implicit def actionResultEncoder: Encoder[ActionResult] = deriveEncoder
  private implicit def traceEncoder: Encoder[Trace] = deriveEncoder

  checkSerDes("Action", action)
  checkSerDes("ActionResult", actionResult)
  checkSerDes("Trace", trace)
}
