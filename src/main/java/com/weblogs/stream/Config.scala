package com.weblogs.stream

object Config {
  def module = "WEBLOGS";
  def table = "CREATE TABLE IF NOT EXISTS " + Config.module + " (ip STRING, datetime STRING, type STRING, page STRING, status STRING)"

}
