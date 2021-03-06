import sbt._

object Dependencies {
  val fullScalaVersion = "2.12.6"
  val jacksonVersion = "2.9.4"

  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.25"
  val reactor = "io.projectreactor" % "reactor-core" % "3.1.5.RELEASE"
  val reactorNetty = "io.projectreactor.ipc" % "reactor-netty" % "0.7.5.RELEASE"
  val cats = "org.typelevel" %% "cats-core" % "1.1.0"
  val springWebFlux = "org.springframework" % "spring-webflux" % "5.0.4.RELEASE"
  val scalajHttp = "org.scalaj" %% "scalaj-http" % "2.3.0"

  val jackson = "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion
  val jacksonScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.5"
  val mockito = "org.mockito" % "mockito-all" % "1.10.19"
  val logbackClassic = "ch.qos.logback" % "logback-classic" % "1.2.3"

  val bouncyCastle = "org.bouncycastle" % "bcprov-jdk15on" % "1.58"
  val junit = "junit" % "junit" % "4.11"

  val freemarker = "org.freemarker" % "freemarker" % "2.3.23"
  val beanutils = "commons-beanutils" % "commons-beanutils" % "1.9.3"
}
