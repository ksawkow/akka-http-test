package com.ksawkow.akka.service

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait Actors {

  implicit val system: ActorSystem = ActorSystem("company-service")
  implicit val materializer = ActorMaterializer()

  val companyActor = system.actorOf(CompanyActor.props())
}
