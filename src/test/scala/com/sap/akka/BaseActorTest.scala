package com.sap.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import com.sap.akka.service.{Actors, CompanyActor}
import org.scalatest._

trait BaseTest extends WordSpecLike with MustMatchers

class BaseActorTest extends TestKit(ActorSystem("test-system")) with BaseTest with ImplicitSender {
}

class BaseEndpointTest extends BaseTest with ScalatestRouteTest with RequestBuilding with Actors {

  override implicit val system: ActorSystem = ActorSystem("tests-company-service")
  override implicit val materializer = ActorMaterializer()
  override val companyActor = system.actorOf(CompanyActor.props())
}
