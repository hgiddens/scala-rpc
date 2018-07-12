package scalether.domain.request

import io.circe.Encoder
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.daonomic.rpc.domain.Word
import scalether.domain.Address

import scala.annotation.varargs
import scala.language.implicitConversions

case class LogFilter(topics: List[TopicFilter] = Nil,
                     address: List[Address] = Nil,
                     fromBlock: String = "latest",
                     toBlock: String = "latest") {
  @varargs def address(address: Address*):LogFilter = copy(address = address.toList)

  def blocks(fromBlock: String, toBlock: String): LogFilter =
    this.copy(fromBlock = fromBlock, toBlock = toBlock)

}

object LogFilter {
  @varargs def apply(topics: TopicFilter*): LogFilter = LogFilter(topics.toList)

  implicit def encoder: Encoder[LogFilter] =
    deriveEncoder
}

sealed trait TopicFilter {

}

object TopicFilter {
  implicit def simple(word: Word): SimpleTopicFilter = SimpleTopicFilter(word)
  @varargs def or(word: Word*): OrTopicFilter = OrTopicFilter(word.toList)

  implicit def encoder: Encoder[TopicFilter] =
    Encoder.instance {
      case SimpleTopicFilter(word) => word.toString.asJson
      case OrTopicFilter(words) => words.map(_.toString).asJson
    }
}

case class SimpleTopicFilter(word: Word) extends TopicFilter

case class OrTopicFilter(words: List[Word]) extends TopicFilter

object OrTopicFilter {
  @varargs def apply(words: Word*): OrTopicFilter = OrTopicFilter(words.toList)
}