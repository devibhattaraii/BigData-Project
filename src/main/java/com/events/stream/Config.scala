package com.events.stream

object Config {
  def module = "EVENTS";
  def table = "CREATE TABLE IF NOT EXISTS " + module + " (action STRING, datetime STRING, page STRING, user STRING)"
}
