package com.ksawkow.akka.service

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.ksawkow.akka.persistence.DefaultMongoPersistence

import scala.concurrent.ExecutionContext.Implicits.global

trait Actors {

  implicit val system: ActorSystem = ActorSystem("company-service")
  implicit val materializer = ActorMaterializer()

  val mongoPersistence = new DefaultMongoPersistence()
  val companyActor = system.actorOf(CompanyActor.props(mongoPersistence))
}
