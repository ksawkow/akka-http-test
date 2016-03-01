package com.ksawkow.akka.service

import com.ksawkow.akka.BaseActorTest
import com.ksawkow.akka.model.{CompanyDetails, MongoAlreadyExists, MongoError, MongoNotFound}
import com.ksawkow.akka.persistence.DefaultMongoPersistence
import com.ksawkow.akka.service.CompanyActor.{CompanyPostOK, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CompanyActorTest extends BaseActorTest {

  private val stubMongoPersistence = stub[DefaultMongoPersistence]

  "CompanyActor" when {

    "receives CreateCompany message" should {

      "succeed to create a company" in {
        // given
        (stubMongoPersistence.createCompany _)
          .when(*)
          .returns(Future.successful(Right(true)))

        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        companyActor ! PostCompany(CompanyDetails("sap", "SAP AG"))

        // then
        expectMsg(Right(CompanyPostOK))
      }

      "fail to create company if underlying persistence fails with known error" in {

        // given
        (stubMongoPersistence.createCompany _)
          .when(*)
          .returns(Future {
            Left(MongoAlreadyExists(s"Company named 'bcd' already exists"))
          })
        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        companyActor ! PostCompany(CompanyDetails("bcd", "IPM Poland Sp. z o.o."))

        // then
        expectMsgPF() {
          case Left(MongoAlreadyExists(_)) ⇒
        }
      }

      "fail to create company if underlying persistence fails with exception" in {

        // given
        (stubMongoPersistence.createCompany _)
          .when(*)
          .returns(Future.failed(new IllegalStateException("Fail ..")))

        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        companyActor ! PostCompany(CompanyDetails("bcd", "IPM Poland Sp. z o.o."))

        // then
        expectMsgPF() {
          case Left(MongoError(_)) ⇒
        }
      }
    }

    "receives GetCompany message" must {

      "get existing company" in {

        // given
        (stubMongoPersistence.getCompany _)
          .when(*)
          .returns(Future.successful(Right(CompanyDetails("aeco", "PL"))))
        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        val name = "aeco"
        companyActor ! GetCompany(name)

        expectMsg(Right(GetCompanyResponse(CompanyDetails("aeco", "PL"))))
      }

      "fail to get company if underlying persistence fails with known error" in {

        // given
        (stubMongoPersistence.getCompany _)
          .when(*)
          .returns(Future {
            Left(MongoNotFound(s"Company named 'bcd' doesn't exists"))
          })
        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        companyActor ! GetCompany("bcd")

        // then
        expectMsgPF() {
          case Left(MongoNotFound(_)) ⇒
        }
      }

      "fail to create company if underlying persistence fails with exception" in {

        // given
        (stubMongoPersistence.getCompany _)
          .when(*)
          .returns(Future.failed(new IllegalStateException("Fail ..")))

        val companyActor = system.actorOf(CompanyActor.props(stubMongoPersistence))

        // when
        companyActor ! GetCompany("bcd")

        // then
        expectMsgPF() {
          case Left(MongoError(_)) ⇒
        }
      }
    }
  }
}
