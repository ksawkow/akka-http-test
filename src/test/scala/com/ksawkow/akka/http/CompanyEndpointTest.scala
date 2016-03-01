package com.ksawkow.akka.http

import akka.actor.ActorDSL._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.ksawkow.akka.model.CompanyDetails
import com.ksawkow.akka.service.CompanyActor.{CompanyPostOK, _}
import org.scalatest.{MustMatchers, WordSpecLike}

class CompanyEndpointTest extends WordSpecLike with MustMatchers with ScalatestRouteTest {


  "CompanyEndpoint" must {

    "post company successfully" in {

      // given
      val companyActor = actor(new Act {
        become {
          case PostCompany(CompanyDetails("nasa", "Our Company")) =>
            sender() ! Right(CompanyPostOK)
        }
      })
      val route = new CompanyEndpoint(companyActor).route
      val entity = """{ "name" : "nasa", "description" : "Our Company" }"""

      // when
      Post("/companies", HttpEntity(MediaTypes.`application/json`, entity)) ~> route ~> check {

        // then
        status mustBe StatusCodes.OK
      }
    }

    "get company successfully" in {

      // given
      val companyActor = actor(new Act {
        become {
          case GetCompany(name) ⇒
            sender() ! Right(GetCompanyResponse(CompanyDetails("sap", "SAP AG")))
        }
      })
      val route = new CompanyEndpoint(companyActor).route

      // when
      Get("/companies/sap") ~> route ~> check {

        // then
        status mustBe StatusCodes.OK
      }
    }
  }
}
