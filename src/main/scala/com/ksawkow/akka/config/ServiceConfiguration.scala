package com.ksawkow.akka.config

import com.typesafe.config.ConfigFactory

trait ServiceConfiguration {

  private val config = ConfigFactory.load()

  def getMongoUri =
    config.getString("company-service-mongodb.uri")
}
