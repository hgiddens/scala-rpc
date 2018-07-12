package scalether.core

import io.daonomic.cats.MonadThrowable
import io.daonomic.rpc.{RpcHttpClient, RpcTransport}

import scala.language.higherKinds

class EthereumRpcClient[F[_]](transport: RpcTransport[F])
                          (implicit me: MonadThrowable[F])
  extends RpcHttpClient[F](transport)
