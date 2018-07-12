package scalether.core

import java.math.BigInteger

import io.circe.syntax._
import io.daonomic.cats.MonadThrowable
import io.daonomic.rpc.RpcTransport
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.json.CirceCodec._
import scalether.domain.request._
import scalether.domain.response.{Block, Log, TransactionReceipt}
import scalether.domain.{Address, response}

import scala.language.higherKinds

class Ethereum[F[_]](transport: RpcTransport[F])
                    (implicit me: MonadThrowable[F])
  extends EthereumRpcClient[F](transport) {

  def web3ClientVersion(): F[String] =
    exec("web3_clientVersion")

  def web3Sha3(data: String): F[String] =
    exec("web3_sha3", data.asJson)

  def netVersion(): F[String] =
    exec("net_version")

  def netListening(): F[Boolean] =
    exec("net_listening")

  def ethBlockNumber(): F[BigInteger] =
    exec("eth_blockNumber")

  def ethGetBlockByHash(hash: Word): F[Block] =
    exec("eth_getBlockByHash", hash.asJson, false.asJson)

  def ethGetBlockByNumber(number: BigInteger): F[Block] =
    exec("eth_getBlockByNumber", number.asJson, false.asJson)

  def ethCall(transaction: Transaction, defaultBlockParameter: String): F[Binary] =
    exec("eth_call", transaction.asJson, defaultBlockParameter.asJson)

  def ethEstimateGas(transaction: Transaction, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_estimateGas", transaction.asJson, defaultBlockParameter.asJson)

  def ethSendTransaction(transaction: Transaction): F[Word] =
    exec("eth_sendTransaction", transaction.asJson)

  def ethSendRawTransaction(transaction: Binary): F[Word] =
    exec("eth_sendRawTransaction", transaction.asJson)

  def ethGetTransactionReceipt(hash: Word): F[Option[TransactionReceipt]] =
    execOption("eth_getTransactionReceipt", hash.asJson)

  def ethGetTransactionByHash(hash: Word): F[Option[response.Transaction]] =
    execOption("eth_getTransactionByHash", hash.asJson)

  def netPeerCount(): F[BigInteger] =
    exec("net_peerCount")

  def ethGetTransactionCount(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getTransactionCount", address.asJson, defaultBlockParameter.asJson)

  def ethGetBalance(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getBalance", address.asJson, defaultBlockParameter.asJson)

  def ethGasPrice(): F[BigInteger] =
    exec("eth_gasPrice")

  def ethGetLogs(filter: LogFilter): F[List[Log]] =
    exec("eth_getLogs", filter.asJson)

  def ethNewFilter(filter: LogFilter): F[BigInteger] =
    exec("eth_newFilter", filter.asJson)

  def ethGetFilterChanges(id: BigInteger): F[List[Log]] =
    exec("eth_getFilterChanges", id.asJson)

  def ethGetCode(address: Address, defaultBlockParameter: String): F[Binary] =
    exec("eth_getCode", address.asJson, defaultBlockParameter.asJson)
}
