package io.daonomic.bitcoin.rpc.domain

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Block[T](hash: String,
                    confirmations: Long,
                    strippedsize: Long,
                    size: Long,
                    weight: Long,
                    height: Long,
                    version: Long,
                    versionHex: String,
                    merkleroot: String,
                    tx: List[T],
                    time: Long,
                    mediantime: Long,
                    nonce: Long,
                    bits: String,
                    difficulty: Double,
                    chainwork: String,
                    previousblockhash: String,
                    nextblockhash: String)
object Block {
  implicit def decoder[T: Decoder]: Decoder[Block[T]] =
    deriveDecoder
}