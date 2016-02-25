package com.sap.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.testkit.{ImplicitSender, TestKit}
import com.sap.akka.service.{Actors, CompanyActor}
import com.typesafe.config.ConfigFactory
import org.scalatest._

trait BaseTest extends WordSpecLike with MustMatchers

class BaseActorTest
  extends TestKit(ActorSystem("test-system", ConfigFactory.parseString( """akka.loggers = ["akka.testkit.TestEventListener"]""")))
  with BaseTest with ImplicitSender {
}

class BaseEndpointTest extends BaseActorTest with ScalatestRouteTest with RequestBuilding with Actors {
  override val companyActor = system.actorOf(CompanyActor.props())
}
