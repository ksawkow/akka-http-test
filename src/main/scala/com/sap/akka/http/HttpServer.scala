package com.sap.akka.http

import akka.http.scaladsl.Http

import scala.util.{Failure, Success}

trait HttpServer extends CompanyEndpoint {

  val interface = "0.0.0.0"
  val port = 9000

  import scala.concurrent.ExecutionContext.Implicits.global

  Http().bindAndHandle(routes, interface, port) onComplete {
    case Success(binding) =>
      println(s"Server started, port: $port")
    case Failure(e) =>
      println(s"Cannot start server, error: ${e.getMessage}")
  }
}
