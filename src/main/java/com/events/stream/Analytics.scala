package com.events.stream

import org.apache.spark.sql.hive._
import org.apache.spark.{SparkConf, SparkContext}

object Analytics {
  def main(args: Array[String]) {
    val config = new SparkConf().setAppName(Config.module).setMaster("local[2]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")
    val hiveContext = new HiveContext(sc)

    println("All events")
    hiveContext.sql("SELECT action, count(*) as count FROM " + Config.module + " group by action" ).show()

    println("click events")
    hiveContext.sql(" SELECT action, first(page), count(*) as count FROM " + Config.module + " where action = 'click' GROUP BY page sort by count desc").show()

    println("Most frequent hosts")
    hiveContext.sql("SELECT action, first(page), count(*) as count FROM " + Config.module + " GROUP BY action sort by count desc").show()
  }
}