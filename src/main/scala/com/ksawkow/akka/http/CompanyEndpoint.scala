package com.ksawkow.akka.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.ksawkow.akka.model.JsonProtocol
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
            onComplete((companyActor ? GetCompany(companyName)).mapTo[GetCompanyResponse]) {
              case Success(result) => complete(result.companyDetails)
              case Failure(f) => complete(f)
            }
          }
        }
    }

  implicit def timeout: Timeout = 1.seconds
}
