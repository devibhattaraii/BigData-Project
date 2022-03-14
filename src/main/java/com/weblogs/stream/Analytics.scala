package com.weblogs.stream

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.hive._

object Analytics {
  def main(args: Array[String]) {
    val config = new SparkConf().setAppName(Config.module).setMaster("local[2]")
    val sc = new SparkContext(config)
    sc.setLogLevel("ERROR")
    val hiveContext = new HiveContext(sc)

    println("All pages")
    hiveContext.sql("SELECT page, type, count(*) as count FROM " + Config.module + " group by page, type" ).show()

    println("404 pages")
    hiveContext.sql(" SELECT page, first(datetime), count(*) as count FROM " + Config.module + " where status = 404 GROUP BY page sort by count desc").show()

    println("Internal Server Error pages ")
    hiveContext.sql(" SELECT page, first(datetime), count(*) as count FROM " + Config.module + " where status >= 500 GROUP BY page sort by count desc").show()

    println("Error pages")
    hiveContext.sql(" SELECT page, status, first(datetime), count(*) as count FROM " + Config.module + " where status > 400 GROUP BY page, status sort by count desc").show()

    println("Most frequent hosts")
    hiveContext.sql("SELECT ip, first(page), first(datetime), count(*) as count FROM " + Config.module + " GROUP BY ip sort by count desc").show()
  }
}