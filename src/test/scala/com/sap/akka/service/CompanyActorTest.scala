package com.sap.akka.service

import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import com.sap.akka.BaseActorTest
import com.sap.akka.model.CompanyDetails
import com.sap.akka.service.CompanyActor._

class CompanyActorTest extends BaseActorTest {

  "CompanyActor" when {

    "receives CreateCompany message" should {

      "create company" in {
        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! PostCompany(CompanyDetails("sap", "SAP AG"))

        // then
        expectMsg(CompanyPostOK)
      }

      "fail to create company if company's name is empty" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! PostCompany(CompanyDetails("", "IPM Poland Sp. z o.o."))

        // then
        expectMsgPF() {
          case Left(CompanyPostProblem(StatusCodes.BadRequest, message)) ⇒
            message.isEmpty mustBe false
        }
      }
    }

    "receives GetCompany message" should {

      "get existing company" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        val name = "sap"
        companyActor ! GetCompany(name)

        expectMsgPF() {
          case Right(GetCompanyResponse(company)) =>
            company mustBe CompanyDetails("sap", "SAP AG")
        }
      }

      "handle if company doesn't exist" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! GetCompany(UUID.randomUUID().toString)

        expectMsgPF() {
          case Left(CompanyGetProblem(statusCode, _)) =>
            statusCode mustBe StatusCodes.NotFound
        }
      }
    }

    "receives FilterCompanies message" should {

    }
  }
}
