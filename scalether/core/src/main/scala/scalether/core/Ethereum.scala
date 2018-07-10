package scalether.core

import java.math.BigInteger

import io.daonomic.cats.MonadThrowable
import io.daonomic.rpc.RpcTransport
import io.daonomic.rpc.domain.{Binary, Word}
import scalether.domain.request.{LogFilter, Transaction}
import scalether.domain.response.{Block, Log, TransactionReceipt}
import scalether.domain.{Address, response}
import shapeless.HNil

import scala.language.higherKinds

class Ethereum[F[_]](transport: RpcTransport[F])
                    (implicit me: MonadThrowable[F])
  extends EthereumRpcClient[F](transport) {

  def web3ClientVersion(): F[String] =
    exec("web3_clientVersion", HNil)

  def web3Sha3(data: String): F[String] =
    exec("web3_sha3", data :: HNil)

  def netVersion(): F[String] =
    exec("net_version", HNil)

  def netListening(): F[Boolean] =
    exec("net_listening", HNil)

  def ethBlockNumber(): F[BigInteger] =
    exec("eth_blockNumber", HNil)

  def ethGetBlockByHash(hash: Word): F[Block] =
    exec("eth_getBlockByHash", hash :: false :: HNil)

  def ethGetBlockByNumber(number: BigInteger): F[Block] =
    exec("eth_getBlockByNumber", number :: false :: HNil)

  def ethCall(transaction: Transaction, defaultBlockParameter: String): F[Binary] =
    exec("eth_call", transaction :: defaultBlockParameter :: HNil)

  def ethEstimateGas(transaction: Transaction, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_estimateGas", transaction :: defaultBlockParameter :: HNil)

  def ethSendTransaction(transaction: Transaction): F[Word] =
    exec("eth_sendTransaction", transaction :: HNil)

  def ethSendRawTransaction(transaction: Binary): F[Word] =
    exec("eth_sendRawTransaction", transaction :: HNil)

  def ethGetTransactionReceipt(hash: Word): F[Option[TransactionReceipt]] =
    execOption("eth_getTransactionReceipt", hash :: HNil)

  def ethGetTransactionByHash(hash: Word): F[Option[response.Transaction]] =
    execOption("eth_getTransactionByHash", hash :: HNil)

  def netPeerCount(): F[BigInteger] =
    exec("net_peerCount", HNil)

  def ethGetTransactionCount(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getTransactionCount", address :: defaultBlockParameter :: HNil)

  def ethGetBalance(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getBalance", address :: defaultBlockParameter :: HNil)

  def ethGasPrice(): F[BigInteger] =
    exec("eth_gasPrice", HNil)

  def ethGetLogs(filter: LogFilter): F[List[Log]] =
    exec("eth_getLogs", filter :: HNil)

  def ethNewFilter(filter: LogFilter): F[BigInteger] =
    exec("eth_newFilter", filter :: HNil)

  def ethGetFilterChanges(id: BigInteger): F[List[Log]] =
    exec("eth_getFilterChanges", id :: HNil)

  def ethGetCode(address: Address, defaultBlockParameter: String): F[Binary] =
    exec("eth_getCode", address :: defaultBlockParameter :: HNil)
}
