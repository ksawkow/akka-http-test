package com.ksawkow.akka.service

import akka.actor.{Actor, ActorSystem}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.Timeout

trait BaseActor extends Actor {

  import scala.concurrent.duration._

  implicit val system: ActorSystem = context.system
  implicit val materializer = ActorMaterializer(ActorMaterializerSettings(system))

  implicit def timeout: Timeout = 1.second
}
