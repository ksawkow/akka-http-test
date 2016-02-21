package com.sap.akka.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

trait HttpServer extends CompanyHttpRoutes {

  implicit override val system: ActorSystem

  val interface = "0.0.0.0"
  val port = 9000
  Http().bindAndHandle(routes, interface, port)
}
