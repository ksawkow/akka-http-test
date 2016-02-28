
package com.ksawkow.akka.model

sealed abstract class ServiceError(message: String) {

  def getErrorMessage: String = message
}

case class MongoError(message: String) extends ServiceError(message)

case class MongoAlreadyExists(message: String) extends ServiceError(message)

case class MongoNotFound(message: String) extends ServiceError(message)