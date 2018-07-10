package io.daonomic.bitcoin.rpc.core

import java.math.BigInteger

import cats.implicits._
import io.daonomic.bitcoin.rpc.domain.{Block, Transaction}
import io.daonomic.cats.MonadThrowable
import io.daonomic.rpc.{JsonConverter, RpcHttpClient, RpcTransport}
import shapeless.HNil

import scala.language.higherKinds

class Bitcoind[F[_]](transport: RpcTransport[F])
                    (implicit me: MonadThrowable[F])
  extends RpcHttpClient[F](new JsonConverter(), transport) {

  def help: F[String] =
    exec("help", HNil)

  def getBlockCount: F[BigInteger] =
    exec("getblockcount", HNil)

  def getNewAddress: F[String] =
    exec("getnewaddress", HNil)

  def generate(amount: Int): F[List[String]] =
    exec("generate", amount :: HNil)

  def sendToAddress(to: String, amount: Double): F[String] =
    exec("sendtoaddress", to :: amount :: HNil)

  def getRawTransaction(txid: String, verbose: Boolean = false): F[Transaction] =
    exec("getrawtransaction", txid :: verbose :: HNil)

  def importAddress(address: String, label: String = "", rescan: Boolean = false, p2sh: Boolean = false): F[Unit] =
    execOption[String]("importaddress", address :: label :: rescan :: p2sh :: HNil).map(_ => ())

  def getBlockHash(blockNumber: BigInteger): F[String] =
    exec("getblockhash", blockNumber :: HNil)

  def getBlockSimple(hash: String): F[Block[String]] =
    get(s"/rest/block/notxdetails/$hash.json")

  def getBlockFull(hash: String): F[Block[Transaction]] =
    get(s"/rest/block/$hash.json")
}
