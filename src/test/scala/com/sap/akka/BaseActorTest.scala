package com.sap.akka

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest._

trait BaseTest extends WordSpecLike with MustMatchers {
}

class BaseActorTest
  extends TestKit(ActorSystem("test-system", ConfigFactory.parseString( """akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with BaseTest with ImplicitSender {
}
