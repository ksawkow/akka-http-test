package com.sap.akka.service

import akka.actor.ActorSystem
import akka.stream.Materializer

trait Actors {
  implicit val system: ActorSystem
  implicit val materializer: Materializer

  val companyActor = system.actorOf(CompanyActor.props())
}
