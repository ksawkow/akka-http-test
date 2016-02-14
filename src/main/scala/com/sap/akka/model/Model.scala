package com.sap.akka.model

import spray.json.DefaultJsonProtocol

case class CompanyDetails(name: String, description: String)

case class Employee(pesel: String, name: String)

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val companyDetailsFormat = jsonFormat2(CompanyDetails)

  implicit val employeeFormat = jsonFormat2(Employee)
}

