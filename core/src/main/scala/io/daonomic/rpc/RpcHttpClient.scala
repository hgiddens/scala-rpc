package io.daonomic.rpc

import cats.implicits._
import io.circe.{Decoder, Json}
import io.circe.parser.parse
import io.circe.syntax._
import io.daonomic.cats.MonadThrowable
import io.daonomic.rpc.domain.{Error, Request, Response, StatusAndBody}
import org.slf4j.{Logger, LoggerFactory}

import scala.language.higherKinds

class RpcHttpClient[F[_]](transport: RpcTransport[F])(implicit me: MonadThrowable[F]) {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  def get[T: Decoder](url: String): F[T] = {
    if (logger.isDebugEnabled()) {
      logger.debug(s"get $url")
    }
    transport.get(url).flatMap(response => parseResponse(response, url))
  }

  def exec[T: Decoder](method: String, params: Json*): F[T] = {
    execOption[T](method, params: _*).flatMap {
      case Some(v) => me.pure(v)
      case None => me.raiseError(new RpcCodeException(Error.default))
    }
  }

  def execOption[T: Decoder](method: String, params: Json*): F[Option[T]] = {
    execute[T](Request(1, method, params: _*)).flatMap {
      response =>
        response.error match {
          case Some(r) => me.raiseError(new RpcCodeException(r))
          case None => me.pure(response.result)
        }
    }
  }

  private def execute[T: Decoder](request: Request): F[Response[T]] = {
    val requestJson = request.asJson.noSpaces
    if (logger.isDebugEnabled) {
      logger.debug(s"request=$requestJson")
    }
    transport.post("", requestJson)
      .flatMap(resp => parseResponse(resp, requestJson))
  }

  private def parseResponse[T: Decoder](response: StatusAndBody, request: String): F[T] = {
    if (logger.isDebugEnabled()) {
      logger.debug(s"response=${response.body}")
    }

    def wrapError[A](e: Throwable): Throwable =
      new IllegalArgumentException(s"unable to parse response json. http status code=${response.code} request=$request response=${response.body}", e)

    me.fromEither(for {
      json <- parse(response.body).left.map(wrapError)
      t <- json.as[T].left.map(wrapError)
    } yield t)
  }
}
