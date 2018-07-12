package io.daonomic.bitcoin.rpc.domain

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Transaction(txid: String,
                       hash: String,
                       version: Int,
                       size: Long,
                       vsize: Long,
                       locktime: Long,
                       vin: List[TransactionIn],
                       vout: List[TransactionOut],
                       confirmations: Long,
                       hex: String)
object Transaction {
  implicit def decoder: Decoder[Transaction] =
    deriveDecoder
}

case class TransactionIn(txid: String,
                         vout: Int,
                         sequence: Long,
                         scriptSig: ScriptSig,
                         txinwitness: List[String])
object TransactionIn {
  implicit def decoder: Decoder[TransactionIn] =
    deriveDecoder
}

case class ScriptSig(asm: String, hex: String)
object ScriptSig {
  implicit def decoder: Decoder[ScriptSig] =
    deriveDecoder
}

case class TransactionOut(value: Double,
                          n: Int,
                          scriptPubKey: ScriptPubKey)
object TransactionOut {
  implicit def decoder: Decoder[TransactionOut] =
    deriveDecoder
}

case class ScriptPubKey(asm: String,
                        hex: String,
                        reqSigs: Int,
                        `type`: String,
                        addresses: List[String])
object ScriptPubKey {
  implicit def decoder: Decoder[ScriptPubKey] =
    deriveDecoder
}