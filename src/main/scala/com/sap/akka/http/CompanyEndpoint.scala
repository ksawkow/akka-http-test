package com.sap.akka.http

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.sap.akka.model.JsonProtocol
import com.sap.akka.service.Actors
import com.sap.akka.service.CompanyActor.{GetCompany, GetCompanyResponse}

import scala.util.{Failure, Success}

trait CompanyEndpoint extends Actors with JsonProtocol {

  import scala.concurrent.duration._

  implicit def timeout: Timeout = 1.seconds

  val routes =
    pathPrefix("companies") {
      path(Segment) { companyName ⇒
        get {
          onComplete((companyActor ? GetCompany(companyName)).mapTo[GetCompanyResponse]) {
            case Success(result) => complete(result.companyDetails.asInstanceOf[ToResponseMarshallable])
            case Failure(f) => complete(f)
          }
        } ~
          post {
            complete {
              StatusCodes.OK
            }
          }
      }
    }
}
