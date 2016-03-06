package com.ksawkow.akka.persistence

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.ksawkow.akka.BaseTest
import com.ksawkow.akka.model.{CompanyDetails, MongoAlreadyExists, MongoNotFound}

import scala.concurrent.ExecutionContext

class CompanyMongoPersistenceTest extends BaseTest with MongoClient {

  val system = ActorSystem()

  implicit val materializer = ActorMaterializer()(system)

  implicit val executionContext: ExecutionContext = system.dispatcher

  val persistence = new CompanyMongoPersistence {
    override val companyCollection = mongoClient.getDatabase("company-service-tests").getCollection("company-tests")
  }

  after {
    whenReady(Source.fromPublisher(persistence.companyCollection.drop()).runWith(Sink.ignore)) { _ =>
    }
  }

  "MongoPersistence" must {

    "handle getting company requests" when {

      "succeed while getting company" in {

        val company = CompanyDetails("ipc", "fine company")
        whenReady(persistence.createCompany(company)) { result =>
          result mustBe Right(true)
        }

        whenReady(persistence.getCompany("ipc")) { result =>
          result.isRight mustBe true
          result.right.get mustBe company
        }
      }

      "fail to get a nonexistent company" in {

        val companyName = UUID.randomUUID().toString
        whenReady(persistence.getCompany(companyName)) { result =>
          result mustBe Left(MongoNotFound("Company not found"))
        }
      }

      "save one company, fail to save second one of the same name and read should return data of the first one" in {

        val company = CompanyDetails("ipc", "fine company")
        whenReady(persistence.createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val company2 = CompanyDetails("ipc", "bad company")
        whenReady(persistence.createCompany(company2)) { result =>
          result.isLeft mustBe true
          result.left.get.asInstanceOf[MongoAlreadyExists].message.contains("already") mustBe true
        }

        whenReady(persistence.getCompany("ipc")) { result =>
          result.isRight mustBe true
          result.right.get.description mustBe "fine company"
        }
      }

      "fail while trying to read a nonexistent company" in {

        whenReady(persistence.getCompany(UUID.randomUUID().toString)) { result =>
          result.isLeft mustBe true
        }
      }
    }

    "handle new company requests" when {

      "succeed to create a new company" in {

        val company = CompanyDetails("name", "fine company with bad people")
        whenReady(persistence.createCompany(company)) { result =>
          result mustBe Right(true)
        }
      }

      "succeed to create two new companies" in {

        val company = CompanyDetails("pas", "fine company with bad people")
        whenReady(persistence.createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val secondCompany = CompanyDetails("hp", "fine company")
        whenReady(persistence.createCompany(secondCompany)) { result =>
          result mustBe Right(true)
        }
      }

      "fail to create two companies with the same names" in {

        val company = CompanyDetails("one", "fine company with bad people")
        whenReady(persistence.createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val secondCompany = CompanyDetails("one", "fine company")
        whenReady(persistence.createCompany(secondCompany)) { result =>
          result.isLeft mustBe true
          result.left.get.asInstanceOf[MongoAlreadyExists].message.contains("already") mustBe true
        }
      }
    }
  }
}
