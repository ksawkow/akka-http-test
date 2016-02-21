package com.sap.akka.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.util.Timeout
import com.sap.akka.model.CompanyDetails
import com.sap.akka.service.CompanyActor._

object CompanyActor {

  case class PutCompany(companyDetails: CompanyDetails)

  case object CompanyPut

  case class CompanyNotPut(errorDescription: String)

  case class GetCompany(name: String)

  case class GetCompanyResponse(companyDetails: CompanyDetails)

  case object CompanyNotFound

  def props(): Props = Props(new CompanyActor())
}


class CompanyActor extends Actor with ActorLogging {

  import scala.concurrent.duration._

  implicit def timeout: Timeout = 1.second

  override def receive: Receive = {

    case PutCompany(details) ⇒
      val savedSender = sender
      details.name.isEmpty match {
        case false ⇒ savedSender ! CompanyPut
        case true ⇒ savedSender ! Left(CompanyNotPut("The name of company is required."))
      }

    case GetCompany(name) ⇒
      sender() ! GetCompanyResponse(CompanyDetails("sap", "SAP AG"))
  }
}
