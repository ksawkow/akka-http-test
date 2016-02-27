/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.ksawkow.akka.persistence

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.ksawkow.akka.BaseTest
import com.ksawkow.akka.model.CompanyDetails

import scala.concurrent.ExecutionContext

class DefaultMongoPersistenceTest extends DefaultMongoPersistence with BaseTest {

  override val companyCollection = mongoClient.getDatabase("company-service-test").getCollection("company")

  val system = ActorSystem()

  implicit val materializer = ActorMaterializer()(system)

  implicit val executionContext: ExecutionContext = system.dispatcher

  after {
    whenReady(Source.fromPublisher(companyCollection.drop()).runWith(Sink.ignore)) { _ =>
    }
  }

  "MongoPersistence" must {

    "handle getting company requests" when {

      "succeed while getting company" in {

        val company = CompanyDetails("ipc", "fine company")
        whenReady(createCompany(company)) { result =>
          result mustBe Right(true)
        }

        whenReady(getCompany("ipc")) { result =>
          result.isRight mustBe true
          result.right.get mustBe company
        }
      }

      "fail while save one company, try to save second of the same name and read should be the first one" in {

        val company = CompanyDetails("ipc", "fine company")
        whenReady(createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val company2 = CompanyDetails("ipc", "bad company")
        whenReady(createCompany(company2)) { result =>
          result.isLeft mustBe true
        }

        whenReady(getCompany("ipc")) { result =>
          result.isRight mustBe true
          result.right.get.description mustBe "fine company"
        }
      }

      "faile while trying to read a nonexistent company" in {

        whenReady(getCompany(UUID.randomUUID().toString)) { result =>
          result.isLeft mustBe true
        }
      }
    }

    "handle new company requests" when {

      "succeed to create a new company" in {

        val company = CompanyDetails("name", "fine company with bad people")
        whenReady(createCompany(company)) { result =>
          result mustBe Right(true)
        }
      }

      "succeed to create two new companies" in {

        val company = CompanyDetails("pas", "fine company with bad people")
        whenReady(createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val secondCompany = CompanyDetails("hp", "fine company")
        whenReady(createCompany(secondCompany)) { result =>
          result mustBe Right(true)
        }
      }

      "fail to create two companies with the same names" in {

        val company = CompanyDetails("one", "fine company with bad people")
        whenReady(createCompany(company)) { result =>
          result mustBe Right(true)
        }

        val secondCompany = CompanyDetails("one", "fine company")
        whenReady(createCompany(secondCompany)) { result =>
          result.isLeft mustBe true
          println(result)
        }
      }
    }
  }
}
