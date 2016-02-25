package com.sap.akka.http

import akka.actor.ActorDSL._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import com.sap.akka.BaseEndpointTest
import com.sap.akka.model.CompanyDetails
import com.sap.akka.service.CompanyActor.{CompanyPostOK, PostCompany}

class CompanyEndpointTest extends BaseEndpointTest {

  "CompanyEndpoint" must {

    "post company successfully" in {

      // given
      val companyActor = actor(new Act {
        become {
          case PostCompany(CompanyDetails("companyName", "description")) =>
            sender() ! Right(CompanyPostOK)
        }
      })

      val route = new CompanyEndpoint(companyActor).route
      val entity = """{ "name" : "company", "description" : "Our Company" }"""

      // when
      Post("/companies", HttpEntity(MediaTypes.`application/json`, entity)) ~> route ~> check {

        // then
        status mustBe StatusCodes.OK
      }
    }
  }
}
