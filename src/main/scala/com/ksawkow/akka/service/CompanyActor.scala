package com.ksawkow.akka.service

import akka.actor.{ActorLogging, Props}
import akka.http.scaladsl.model.StatusCode
import com.ksawkow.akka.model.{CompanyDetails, MongoError}
import com.ksawkow.akka.persistence.DefaultMongoPersistence
import com.ksawkow.akka.service.CompanyActor._

import scala.util.{Failure, Success}

object CompanyActor {

  def props(mongoPersistence: DefaultMongoPersistence): Props = {
    println("from PROPS mongoPersistence==null ? " + (mongoPersistence == null))
    Props(new CompanyActor(mongoPersistence))
  }

  case class PostCompany(companyDetails: CompanyDetails)

  case class CompanyPostProblem(statusCode: StatusCode, errorDescription: String)

  case class GetCompany(name: String)

  case class GetCompanyResponse(companyDetails: CompanyDetails)

  case class CompanyGetProblem(statusCode: StatusCode, errorDescription: String)

  case object CompanyPostOK

}

class CompanyActor(mongoPersistence: DefaultMongoPersistence) extends BaseActor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  override def receive: Receive = {

    case PostCompany(details) ⇒
      val savedSender = sender()
      mongoPersistence.createCompany(details) onComplete {
        case Success(Right(result)) ⇒
          savedSender ! CompanyPostOK
        case Success(Left(problem)) ⇒
          savedSender ! Left(problem)
        case Failure(failure) ⇒
          savedSender ! Left(MongoError(failure.getMessage))
      }

    case GetCompany(name) ⇒
      val savedSender = sender()
      mongoPersistence.getCompany(name) onComplete {
        case Success(Right(result)) ⇒
          savedSender ! GetCompanyResponse(result)
        case Success(Left(problem)) ⇒
          savedSender ! Left(problem)
        case Failure(failure) ⇒
          savedSender ! Left(MongoError(failure.getMessage))
      }
  }
}
