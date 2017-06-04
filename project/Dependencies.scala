import Library._
import sbt._

object Version {
  final val Scala = "2.11.11"

  final val Akka = "2.4.11.2"
  final val Spark = "1.6.2"
  final val Kafka = "0.10.1.1"

  final val TypesafeConfig = "1.2.1"

  final val ScalaLogging = "3.5.0"
  final val Slf4J = "1.7.25"
  final val Logback = "1.2.2"

  final val ScalaTest = "3.0.3"
}

object Library {

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % Version.Akka
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Version.Akka
  val akkaHttpCore = "com.typesafe.akka" %% "akka-http-core" % Version.Akka
  val akkaHttpSpray = "com.typesafe.akka" %% "akka-http-spray-json-experimental" % Version.Akka

  val spark = "org.apache.spark" %% "spark-core" % Version.Spark % "provided"
  val sparkStreaming = "org.apache.spark" %% "spark-streaming" % Version.Spark
  val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka" % Version.Spark
  val kafka = "org.apache.kafka" %% "kafka" % Version.Kafka
  val kafkaClients = "org.apache.kafka" %% "kafka-clients" % Version.Kafka
  val sparkKafka = "com.stratio.receiver" % "spark-kafka" % "0.2.0"

  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % Version.ScalaLogging
  val logback = "ch.qos.logback" % "logback-classic" % Version.Logback
  val slf4j = "org.slf4j" % "slf4j-api" % Version.Slf4J

  val typesafeConfig = "com.typesafe" % "config" % Version.TypesafeConfig

  val scalaTest = "org.scalatest" %% "scalatest" % Version.ScalaTest

}

object Resolvers {

  val resolvers = Seq(
    Resolver sonatypeRepo "public",
    Resolver typesafeRepo "releases")

}

trait Dependencies {

  val scalaVersionUsed = Version.Scala

  val commonResolvers = Resolvers.resolvers

  val commonDeps = Seq(typesafeConfig, scalaLogging, slf4j, logback, akkaHttpSpray)

  val eventsDeps = Seq(akkaActor, akkaSlf4j, akkaHttpCore)

  val sparkDeps = Seq(spark, sparkStreaming, kafka, sparkKafka)

  val testDeps = Seq(scalaTest)

}