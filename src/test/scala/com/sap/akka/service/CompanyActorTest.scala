package com.sap.akka.service

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.sap.akka.BaseTest
import com.sap.akka.model.CompanyDetails
import com.typesafe.config.ConfigFactory

class CompanyActorTest extends TestKit(ActorSystem("system-test", ConfigFactory.parseString( """akka.loggers = ["akka.testkit.TestEventListener"]""")))
with BaseTest {

  "CompanyActor" when {

    "receives CreateCompany message" should {

      "create company" in {
        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! CreateCompany("sap", "SAP AG")

        // then
        expectMsg(Right(CompanyCreated))
      }

      "handle when client fails to create schema" in {
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
            company shouldBe CompanyDetails("IBM", "IBM Worldwide")
        }
      }

      "handle if company doesn't exist" in {

        // given
        val companyActor = system.actorOf(CompanyActor.props())

        // when
        companyActor ! GetCompany(UUID.randomUUID().toString)

        expectMsgPF() {
          case Left(ResourceNotFound(_)) =>
        }
      }
    }

    "receives FilterCompanies message" should {

    }
  }
}
