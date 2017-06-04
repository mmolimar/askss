package com.github.mmolimar.asks.streaming

import java.util.UUID

import com.github.mmolimar.askss.common.implicits._
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}


object RsvpStreaming extends App with LazyLogging {

  val filter = config.getString("spark.filter").toLowerCase.split(",").toList
  val ssc = new StreamingContext(buildSparkConfig, Seconds(5))

  //TODO
  kafkaStream(ssc)
    .map(_.value())
    .map(_.toEvent)
    .filter(rsvp => {
      filter.exists(rsvp.event.get.event_name.contains(_))
    })
    .print()

  ssc.start()
  ssc.awaitTermination()

  def buildSparkConfig(): SparkConf = {
    new SparkConf()
      .setMaster(config.getString("spark.master"))
      .setAppName("RsvpStreaming")
      .set("spark.streaming.ui.retainedBatches", "5")
      .set("spark.streaming.backpressure.enabled", "true")
      .set("spark.sql.parquet.compression.codec", "snappy")
      .set("spark.sql.parquet.mergeSchema", "true")
      .set("spark.sql.parquet.binaryAsString", "true")
  }

  def kafkaStream(ssc: StreamingContext): InputDStream[ConsumerRecord[String, String]] = {
    val topics = Set(config.getString("kafka.topic"))

    val kafkaParams = Map[String, Object](
      "metadata.broker.list" -> config.getString("kafka.brokerList"),
      "enable.auto.commit" -> config.getBoolean("kafka.autoCommit").toString,
      "auto.offset.reset" -> config.getString("kafka.autoOffset"),
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> config.getString("kafka.brokerList"),
      ConsumerConfig.GROUP_ID_CONFIG -> s"consumer-${UUID.randomUUID}",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer]
    )

    val consumerStrategy = ConsumerStrategies.Subscribe[String, String](topics, kafkaParams)
    KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent, consumerStrategy)
  }

}