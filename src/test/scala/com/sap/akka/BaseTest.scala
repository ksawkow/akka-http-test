package com.sap.akka

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

trait BaseTest extends WordSpecLike with Matchers with ScalatestRouteTest {
}
