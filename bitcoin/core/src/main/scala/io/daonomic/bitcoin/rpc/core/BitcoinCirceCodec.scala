package io.daonomic.bitcoin.rpc.core

import io.circe.Decoder
import io.circe.generic.semiauto._
import io.daonomic.bitcoin.rpc.domain._

// TODO: test
object BitcoinCirceCodec {
  implicit def blockDecoder[T: Decoder]: Decoder[Block[T]] =
    deriveDecoder

  implicit def scriptPubKeyDecoder: Decoder[ScriptPubKey] =
    deriveDecoder

  implicit def scriptSigDecoder: Decoder[ScriptSig] =
    deriveDecoder

  implicit def transactionDecoder: Decoder[Transaction] =
    deriveDecoder

  implicit def transactionInDecoder: Decoder[TransactionIn] =
    deriveDecoder

  implicit def transactionOutDecoder: Decoder[TransactionOut] =
    deriveDecoder
}
