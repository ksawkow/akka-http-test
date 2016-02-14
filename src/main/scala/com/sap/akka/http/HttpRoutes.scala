package com.sap.akka.http

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._

trait HttpRoutes {
  val routes = logRequestResult("Service") {
    pathPrefix("companies") {
      (get & path(Segment)) { companyName ⇒
          complete{
            StatusCodes.OK
          }
        } ~
        ( put & path(Segment)) { companyName ⇒
          complete{
            StatusCodes.OK
          }
        }
    }
  }


  val routes2 = {
    //logRequestResult("akka-http-microservice") {
    pathPrefix("ip") {
      (get & path(Segment)) { ip =>
          complete {
            StatusCodes.OK
          }
        } ~
        (post & path(Segment)) { ip =>
          complete {
            StatusCodes.OK
          }
        }
    }
    //}
  }
}
