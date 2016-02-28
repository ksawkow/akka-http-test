package com.ksawkow.akka.model

import spray.json.DefaultJsonProtocol

case class CompanyDetails(name: String, description: String)

object JsonProtocol extends JsonProtocol

trait JsonProtocol extends DefaultJsonProtocol {

  implicit val mongoErrorFormat = jsonFormat1(MongoError)
  implicit val mongoAlreadyExistsFormat = jsonFormat1(MongoAlreadyExists)
  implicit val mongoNotFoundFormat = jsonFormat1(MongoNotFound)

  implicit val companyDetailsFormat = jsonFormat2(CompanyDetails)
}