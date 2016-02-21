package com.sap.akka.service

import java.util.UUID

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
        companyActor ! PutCompany(CompanyDetails("sap", "SAP AG"))

        // then
        expectMsg(CompanyPut)
      }

      "fail to create company if company's name is empty" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! PutCompany(CompanyDetails("", "IBM Poland Sp. z o.o."))

        // then
        expectMsg(CompanyPut)
      }
    }

    "receives GetCompany message" should {

      "get existing company" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        val name = "IBM"
        companyActor ! GetCompany(name)

        expectMsgPF() {
          case Right(GetCompanyResponse(company)) =>
            company shouldBe CompanyDetails("sap", "SAP AG")
        }
      }

      "handle if company doesn't exist" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! GetCompany(UUID.randomUUID().toString)

        expectMsg(CompanyNotFound)
      }
    }

    "receives FilterCompanies message" should {

    }
  }
}
