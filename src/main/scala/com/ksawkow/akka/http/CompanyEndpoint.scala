package com.ksawkow.akka.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.ksawkow.akka.model.{JsonProtocol, ServiceError}
import com.ksawkow.akka.service.CompanyActor.{GetCompany, GetCompanyResponse}

import scala.util.{Failure, Success}

class CompanyEndpoint(val companyActor: ActorRef) extends JsonProtocol {

  import scala.concurrent.duration._

  val route =
    pathPrefix("companies") {
      pathEnd {
        post {
          complete {
            StatusCodes.OK
          }
        }
      } ~
        path(Segment) { companyName ⇒
          get {
            onComplete((companyActor ? GetCompany(companyName)).mapTo[Either[ServiceError, GetCompanyResponse]]) {
              case Success(Right(result)) =>
                complete(result.companyDetails)
              case Success(Left(problem)) =>
                complete(BadRequest -> s"""{"message": "${problem.getErrorMessage}"}""")
              case Failure(ex) =>
                complete(InternalServerError -> s"""{"message": "${ex.getMessage}"}""")
            }
          }
        }
    }

  implicit def timeout: Timeout = 1.seconds
}
