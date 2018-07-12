package scalether.domain.request

import io.circe.syntax._
import io.daonomic.rpc.domain.Word
import org.scalatest.FlatSpec

final class LogFilterSpec extends FlatSpec {
  "encoder" should "serialise simple LogFilter" in {
    val filter = LogFilter(Word("0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d"))
    val result = filter.asJson.noSpaces
    assert(result == "{\"topics\":[\"0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d\"],\"address\":[],\"fromBlock\":\"latest\",\"toBlock\":\"latest\"}")
  }

  it should "serialise or LogFilter" in {
    val filter = LogFilter(topics = List(TopicFilter.or(Word("0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d"), Word("0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d"))))
    val result = filter.asJson.noSpaces
    assert(result == "{\"topics\":[[\"0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d\",\"0x5742ce6d6b60075574d7aca76464bc56ccc67f0edcab8ab1b0caa30cbf79056d\"]],\"address\":[],\"fromBlock\":\"latest\",\"toBlock\":\"latest\"}")
  }
}
