package com.ksawkow.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

trait BaseTest extends WordSpecLike with MustMatchers with BeforeAndAfterAll with BeforeAndAfter with ScalaFutures with MockFactory

sealed class AutoCleaningTestKit(_system: ActorSystem) extends TestKit(_system) with BaseTest {

  implicit val materializer = ActorMaterializer()

  override protected def afterAll() {
    TestKit.shutdownActorSystem(_system)
  }
}

class BaseActorTest(_system: ActorSystem) extends AutoCleaningTestKit(_system) with ImplicitSender {

  def this() = this(ActorSystem("test-system", ConfigFactory.parseString( """akka.loggers = ["akka.testkit.TestEventListener"]""")))
}
