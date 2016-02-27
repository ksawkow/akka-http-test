package com.ksawkow.akka.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.util.Timeout
import com.ksawkow.akka.model.CompanyDetails
import com.ksawkow.akka.service.CompanyActor._

object CompanyActor {

  def props(): Props = Props(new CompanyActor())

  case class PostCompany(companyDetails: CompanyDetails)

  case class CompanyPostProblem(statusCode: StatusCode, errorDescription: String)

  case class GetCompany(name: String)

  case class GetCompanyResponse(companyDetails: CompanyDetails)

  case class CompanyGetProblem(statusCode: StatusCode, errorDescription: String)

  case object CompanyPostOK

}


class CompanyActor extends Actor with ActorLogging {

  import scala.concurrent.duration._

  implicit def timeout: Timeout = 1.second

  override def receive: Receive = {

    case PostCompany(details) ⇒
      val savedSender = sender()
      details.name.isEmpty match {
        case false ⇒ savedSender ! CompanyPostOK
        case true ⇒ savedSender ! Left(CompanyPostProblem(StatusCodes.BadRequest, "The name of company is required."))
      }

    case GetCompany(name) ⇒
      sender() ! GetCompanyResponse(CompanyDetails("sap", "SAP AG"))
  }
}
