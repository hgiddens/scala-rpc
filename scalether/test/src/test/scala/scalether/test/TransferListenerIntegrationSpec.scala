package scalether.test

import java.math.BigInteger

import io.daonomic.cats.implicits._
import io.daonomic.blockchain.block.{BlockListenService, BlockListenerImpl}
import io.daonomic.blockchain.poller.tries.implicits._
import io.daonomic.blockchain.state.VarState
import io.daonomic.blockchain.transfer
import io.daonomic.blockchain.transfer.{TransferListenService, TransferListener}
import io.daonomic.rpc.ManualTag
import io.daonomic.rpc.tries.ScalajHttpTransport
import org.scalatest.FlatSpec
import scalether.core.{Ethereum, Parity}
import scalether.listener.EthereumBlockchain

import scala.util.{Failure, Try}

class TransferListenerIntegrationSpec extends FlatSpec {
  val transport = new ScalajHttpTransport("http://ether-dev:8545")
  val ethereum = new Ethereum(transport)
  val parity = new Parity(transport)
  val blockchain = new EthereumBlockchain(ethereum, parity)

  "TranferListenService" should "listen for transfers" taggedAs ManualTag in {

    val transferListenService = new TransferListenService(blockchain, 2, TestTransferListener, new VarState[BigInteger, Try](None))
    val blockListenService = new BlockListenService(blockchain, new BlockListenerImpl(transferListenService), new VarState[BigInteger, Try](None))

    for (_ <- 1 to 100) {
      blockListenService.check() match {
        case Failure(th) => th.printStackTrace()
        case _ =>
      }
      Thread.sleep(1000)
    }
  }

  it should "notify about transfers in selected block" taggedAs ManualTag in {
    val transferListenService = new TransferListenService(blockchain, 1, TestTransferListener, new VarState[BigInteger, Try](None))

    val start = System.currentTimeMillis()
    transferListenService.fetchAndNotify(BigInteger.valueOf(5000099))(BigInteger.valueOf(5000099))
    println(s"took: ${System.currentTimeMillis() - start}ms")
  }
}

object TestTransferListener extends TransferListener[Try] {
  override def onTransfer(t: transfer.Transfer, confirmations: Int, confirmed: Boolean): Try[Unit] = Try {
    println(s"$t confirmations=$confirmations confirmed=$confirmed")
  }
}
