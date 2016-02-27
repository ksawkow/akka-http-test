package com.ksawkow.akka.http

import akka.http.scaladsl.Http
import com.ksawkow.akka.service.Actors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

trait HttpServer extends Actors {

  val routes = new CompanyEndpoint(companyActor).route

  val interface = "0.0.0.0"
  val port = 9119

  Http().bindAndHandle(routes, interface, port) onComplete {
    case Success(binding) =>
      println(s"Server started, port: $port")
    case Failure(e) =>
      println(s"Cannot start server, error: ${e.getMessage}")
  }
}
