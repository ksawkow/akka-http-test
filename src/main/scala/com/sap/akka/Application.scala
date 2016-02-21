package com.sap.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.sap.akka.http.HttpServer

object Application extends App with HttpServer {
  override implicit val system: ActorSystem = ActorSystem("company-service")
  override implicit val materializer = ActorMaterializer()
}
