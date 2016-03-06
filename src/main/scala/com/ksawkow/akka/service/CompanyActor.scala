package com.ksawkow.akka.service

import akka.actor.{ActorLogging, Props}
import com.ksawkow.akka.model.{CompanyDetails, MongoError}
import com.ksawkow.akka.persistence.CompanyMongoPersistence
import com.ksawkow.akka.service.CompanyActor._

import scala.util.{Failure, Success}

object CompanyActor {

  def props(mongoPersistence: CompanyMongoPersistence): Props = {
    Props(new CompanyActor(mongoPersistence))
  }

  case class PostCompany(companyDetails: CompanyDetails)

  case object CompanyPostOK

  case class GetCompany(name: String)

  case class GetCompanyResponse(companyDetails: CompanyDetails)
}

class CompanyActor(mongoPersistence: CompanyMongoPersistence) extends BaseActor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {

    case GetCompany(name) ⇒
      val savedSender = sender()
      mongoPersistence.getCompany(name) onComplete {
        case Success(Right(result)) ⇒
          savedSender ! Right(GetCompanyResponse(result))
        case Success(Left(problem)) ⇒
          savedSender ! Left(problem)
        case Failure(failure) ⇒
          savedSender ! Left(MongoError(failure.getMessage))
      }

    case PostCompany(details) ⇒
      val savedSender = sender()
      mongoPersistence.createCompany(details) onComplete {
        case Success(Right(result)) ⇒
          savedSender ! Right(CompanyPostOK)
        case Success(Left(problem)) ⇒
          savedSender ! Left(problem)
        case Failure(failure) ⇒
          savedSender ! Left(MongoError(failure.getMessage))
      }
  }
}
