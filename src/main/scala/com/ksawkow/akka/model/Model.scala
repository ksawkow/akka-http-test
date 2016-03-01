package com.ksawkow.akka.model

import spray.json.DefaultJsonProtocol

case class CompanyDetails(name: String, description: String)

object JsonProtocol extends JsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val companyDetailsFormat = jsonFormat2(CompanyDetails)
}