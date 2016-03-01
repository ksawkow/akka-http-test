package com.ksawkow.akka.model

import spray.json.DefaultJsonProtocol

case class CompanyDetails(name: String, description: String)

trait JsonProtocol extends DefaultJsonProtocol {
  implicit val companyDetailsFormat = jsonFormat2(CompanyDetails)
}