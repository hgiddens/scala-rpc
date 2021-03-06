package scalether.transaction

import cats.Id
import io.daonomic.blockchain.poller.ids.implicits._
import io.daonomic.cats.implicits._
import io.daonomic.rpc.domain.Word
import scalether.core.IdEthereum
import scalether.domain.response.TransactionReceipt

class IdTransactionPoller(ethereum: IdEthereum) extends TransactionPoller[Id](ethereum) {
  override def waitForTransaction(txHash: Word): TransactionReceipt =
    super.waitForTransaction(txHash)
}
