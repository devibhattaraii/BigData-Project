package com.weblogs.stream

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.hive._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent

import java.util.Properties

object Main {
  val kafkaServer = "localhost:9093"

  val props = new Properties()
  props.put("bootstrap.servers", kafkaServer)
  props.put("acks", "all")
  props.put("key.serializer", classOf[StringSerializer])
  props.put("value.serializer", classOf[StringSerializer])

  val producer = new KafkaProducer[String, String](props)
  val config = new SparkConf().setAppName(Config.module).setMaster("local[2]")

  val kafkaParams = Map(
    "bootstrap.servers" -> kafkaServer,
    "group.id" -> "spark-streaming-notes",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "auto.offset.reset" -> "latest"
  )

  def main(args: Array[String]) {
    val sc = new SparkContext(config)
    //  sc.setLogLevel("ERROR")
    val ssc = new StreamingContext(sc, Seconds(3))
    val hiveContext = new HiveContext(sc)
    hiveContext.sql(Config.table)

    val topics = Array(Config.module).toSet
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)
    )

    stream.map(r => r.value()).foreachRDD(r => processRDD(r, sc))

    ssc.start()
    ssc.awaitTermination()
  }

  def sendEvent(message: String) = {
    val key = java.util.UUID.randomUUID().toString()
    producer.send(new ProducerRecord[String, String]("weblog_out", key, message))
    System.out.println("Sent event with key: '" + key + "' and message: '" + message + "'\n")
  }


  def processRDD(rdd: RDD[String], sc: SparkContext) {
    val values = rdd.flatMap(e => e.split(",")).collect()

    // Ensuring that the RDD is not empty and each array has 4 values after split
    // and then check that the last value is numeric which represent that the request done successfully and contains status code
    if (!values.isEmpty && values.length == 4) {
      if (isNumeric(values(3))) {
        val ip = values(0)
        val dateTime = values(1)
        val requestType = values(2).split(" ")(0)
        val requestPage = values(2).split(" ")(1)
        val responseStatus = values(3)
        val str = ip + "," + dateTime + "," + requestType + "," + requestPage + "," + responseStatus
        println(str)
        saveLogToHive(sc, ip, dateTime, requestType, requestPage, responseStatus)
        sendEvent(str)
      }
    }
  }

  def isNumeric(input: String): Boolean = input.forall(_.isDigit)

  def saveLogToHive(sc: SparkContext, ip: String, datetime: String, reqType: String, page: String, status: String) {
    println("============== Start Saving Log Record  ================")
    val hiveContext = new HiveContext(sc)
    val sql: String = "select '" + ip + "' as ip, '" + datetime + "' as datetime, '" + reqType + "' as type, '" + page + "' as page, '" + status + "' as status"
    val data = hiveContext.sql(sql)
    data.write.mode("append").format("hive").saveAsTable(Config.module)
    println("============== Record Saved Successfully =================")
  }
}