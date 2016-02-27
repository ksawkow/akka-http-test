package com.ksawkow.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import com.ksawkow.akka.service.{Actors, CompanyActor}
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

trait BaseTest extends WordSpecLike with MustMatchers with BeforeAndAfterAll with BeforeAndAfter with ScalaFutures

sealed class AutoCleaningTestKit(_system: ActorSystem) extends TestKit(_system) with BaseTest {

  override protected def afterAll() {
    TestKit.shutdownActorSystem(_system)
  }
}

class BaseActorTest(_system: ActorSystem) extends AutoCleaningTestKit(_system) with ImplicitSender {

  def this() = this(ActorSystem("test-system"))
}


sealed trait EndpointTest extends BaseTest with ScalatestRouteTest with RequestBuilding

class BaseEndpointTest(_system: ActorSystem) extends AutoCleaningTestKit(_system) with EndpointTest with Actors {

  def this() = this(ActorSystem())

  override implicit val system = _system
  override implicit val materializer = ActorMaterializer()

  override val companyActor = system.actorOf(CompanyActor.props())
}
