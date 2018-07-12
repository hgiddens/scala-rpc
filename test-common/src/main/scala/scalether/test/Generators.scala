package scalether.test

import java.math.BigInteger

import io.daonomic.rpc.domain.{Binary, Word}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import scalether.domain.{Address, request, response}
import scalether.domain.request.{LogFilter, OrTopicFilter, SimpleTopicFilter, TopicFilter}
import scalether.domain.response.parity.{Action, ActionResult, Trace}
import scalether.domain.response.{Block, Log, TransactionReceipt}

// TODO: I think this should be using arbitraries rather than generators but whatever
object Generators {
  def action: Gen[Action] = for {
    callType <- arbitrary[String]
    from <- address
    gas <- bigInteger
    input <- arbitrary[String]
    to <- address
    value <- bigInteger
  } yield Action(callType, from, gas, input, to, value)

  def actionResult: Gen[ActionResult] = for {
    gasUsed <- bigInteger
    output <- arbitrary[String]
    address <- address
  } yield ActionResult(gasUsed, output, address)

  def address: Gen[Address] = Gen.listOfN(20, arbitrary[Byte])
    .map(_.toArray)
    .map(Address.apply)

  def bigInteger: Gen[BigInteger] = arbitrary[BigInt].map(_.bigInteger)

  def binary: Gen[Binary] = arbitrary[Array[Byte]].map(Binary(_))

  def block: Gen[Block] = for {
    number <- bigInteger
    hash <- word
    parentHash <- word
    nonce <- arbitrary[String]
    sha3Uncles <- arbitrary[String]
    logsBloom <- arbitrary[String]
    transactionsRoot <- arbitrary[String]
    stateRoot <- arbitrary[String]
    miner <- address
    difficulty <- bigInteger
    totalDifficulty <- bigInteger
    extraData <- binary
    size <- bigInteger
    gasLimit <- bigInteger
    gasUsed <- bigInteger
    transactions<- Gen.listOf(word)
    timestamp <- bigInteger
  } yield Block(number, hash, parentHash, nonce, sha3Uncles, logsBloom, transactionsRoot, stateRoot, miner, difficulty, totalDifficulty, extraData, size, gasLimit, gasUsed, transactions, timestamp)

  def log: Gen[Log] = for {
    logIndex <- bigInteger
    transactionIndex <- bigInteger
    transactionHash <- word
    blockHash <- word
    blockNumber <- bigInteger
    address <- address
    data <- binary
    topics <- Gen.listOf(word)
    typ <- arbitrary[String]
  } yield Log(logIndex, transactionIndex, transactionHash, blockHash, blockNumber, address, data, topics, typ)

  def logFilter: Gen[LogFilter] = for {
    topics <- Gen.listOf(topicFilter)
    address <- Gen.listOf(address)
    fromBlock <- arbitrary[String]
    toBlock <- arbitrary[String]
  } yield LogFilter(topics, address, fromBlock, toBlock)

  def requestTransaction: Gen[request.Transaction] = for {
    to <- Gen.option(address)
    from <- Gen.option(address)
    gas <- Gen.option(bigInteger)
    gasPrice <- Gen.option(bigInteger)
    value <- Gen.option(bigInteger)
    data <- binary
    nonce <- Gen.option(bigInteger)
  } yield request.Transaction(to.orNull, from.orNull, gas.orNull, gasPrice.orNull, value.orNull, data, nonce.orNull)

  def responseTransaction: Gen[response.Transaction] = for {
    hash <- word
    nonce <- bigInteger
    blockHash <- word
    blockNumber <- bigInteger
    creates <- address
    transactionIndex <- bigInteger
    from <- address
    to <- address
    value <- bigInteger
    gasPrice <- bigInteger
    gas <- bigInteger
    input <- binary
  } yield response.Transaction(hash, nonce, blockHash, blockNumber, creates, transactionIndex, from, to, value, gasPrice, gas, input)

  def topicFilter: Gen[TopicFilter] = {
    val simple = word.map(SimpleTopicFilter)
    val or = Gen.listOf(word).map(OrTopicFilter(_))
    Gen.oneOf(simple, or)
  }

  def trace: Gen[Trace] = for {
    action <- action
    blockHash <- word
    blockNumber <- bigInteger
    result <- actionResult
    subtraces <- arbitrary[Int]
    traceAddress <- Gen.listOf(arbitrary[Int])
    transactionHash <- word
    transactionPosition <- arbitrary[Int]
    error <- arbitrary[String]
    typ <- arbitrary[String]
  } yield Trace(action, blockHash, blockNumber, result, subtraces, traceAddress, transactionHash, transactionPosition, error, typ)

  def transactionReceipt: Gen[TransactionReceipt] = for {
    transactionHash <- word
    transactionIndex <- bigInteger
    blockHash <- word
    blockNumber <- bigInteger
    cumulativeGasUsed <- bigInteger
    gasUsed <- bigInteger
    contractAddress <- address
    status <- bigInteger
    from <- address
    to <- address
    logs <- Gen.listOf(log)
  } yield TransactionReceipt(transactionHash, transactionIndex, blockHash, blockNumber, cumulativeGasUsed, gasUsed, contractAddress, status, from, to, logs)

  def word: Gen[Word] = Gen.listOfN(32, arbitrary[Byte])
    .map(_.toArray)
    .map(Word.apply)
}
