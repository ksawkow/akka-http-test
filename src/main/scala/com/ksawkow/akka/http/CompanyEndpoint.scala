package com.ksawkow.akka.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.ksawkow.akka.model.{CompanyDetails, JsonProtocol, ServiceError}
import com.ksawkow.akka.service.CompanyActor.{CompanyPostOK, GetCompany, GetCompanyResponse, PostCompany}

import scala.util.{Failure, Success}

class CompanyEndpoint(val companyActor: ActorRef) extends JsonProtocol {

  import scala.concurrent.duration._

  implicit def timeout: Timeout = 1.seconds

  val route =
    pathPrefix("companies") {
      pathEnd {
        post {
          entity(as[CompanyDetails]) { company ⇒
            onComplete((companyActor ? PostCompany(company)).mapTo[Either[ServiceError, CompanyPostOK.type]]) {
              case Success(Right(_)) =>
                complete(OK → s"Company '${company.name}' saved")
              case Success(Left(problem)) =>
                complete(BadRequest → s"""{"message": "${problem.getErrorMessage}"}""")
              case Failure(ex) =>
                complete(InternalServerError → s"""{"message": "${ex.getMessage}"}""")
            }
          }
        }
      } ~
        path(Segment) { companyName ⇒
          get {
            onComplete((companyActor ? GetCompany(companyName)).mapTo[Either[ServiceError, GetCompanyResponse]]) {
              case Success(Right(result)) =>
                complete(result.companyDetails)
              case Success(Left(problem)) =>
                complete(BadRequest → s"""{"message": "${problem.getErrorMessage}"}""")
              case Failure(ex) =>
                complete(InternalServerError → s"""{"message": "${ex.getMessage}"}""")
            }
          }
        }
    }
}
