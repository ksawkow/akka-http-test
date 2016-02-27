package com.ksawkow.akka.persistence

import akka.stream.Materializer
import akka.stream.scaladsl.{Sink, Source}
import com.ksawkow.akka.config.ServiceConfiguration
import com.ksawkow.akka.model.{CompanyDetails, MongoError, ServiceError}
import com.mongodb.ConnectionString
import com.mongodb.async.client.MongoClientSettings
import com.mongodb.client.model.Filters
import com.mongodb.connection.ClusterSettings
import com.mongodb.reactivestreams.client.MongoClients
import org.bson.Document

import scala.concurrent.{ExecutionContext, Future}

object MongoNames {
  val IdAttribute = "_id"
  val AttributeCompanyDescription = "description"
}

trait MongoClient extends ServiceConfiguration {

  val mongoClient = MongoClients.create(mongoClientSettings())

  private def mongoClientSettings() = {
    val clusterSettings = ClusterSettings.builder()
      .applyConnectionString(new ConnectionString(getMongoUri))
      .build()
    MongoClientSettings.builder()
      .clusterSettings(clusterSettings)
      .build()
  }
}

trait MongoPersistence extends MongoClient {

  val companyCollection = mongoClient.getDatabase("company-service").getCollection("company")

  def createCompany(company: CompanyDetails)
                   (implicit executionContext: ExecutionContext, materializer: Materializer): Future[Either[ServiceError, Boolean]]

  def getCompany(name: String)
                (implicit executionContext: ExecutionContext, materializer: Materializer): Future[Either[ServiceError, CompanyDetails]]
}

class DefaultMongoPersistence extends MongoPersistence {

  import MongoNames._

  override def createCompany(company: CompanyDetails)
                            (implicit executionContext: ExecutionContext, materializer: Materializer): Future[Either[ServiceError, Boolean]] = {

    val document = new Document(IdAttribute, company.name)
      .append(AttributeCompanyDescription, company.description)

    Source.fromPublisher(companyCollection.insertOne(document))
      .runWith(Sink.head)
      .map(result => Right(true))
      .recover({
        case t: Throwable ⇒ Left(MongoError(t.getMessage))
      })
  }

  def getCompany(name: String)
                (implicit executionContext: ExecutionContext, materializer: Materializer): Future[Either[ServiceError, CompanyDetails]] = {

    Source.fromPublisher(companyCollection.find(Filters.eq(IdAttribute, name)))
      .map(doc ⇒ Right(documentToCompany(doc)))
      .runWith(Sink.head)
      .recover({
        case t: Throwable ⇒ Left(MongoError(t.getMessage))
      })
  }

  private def documentToCompany(document: Document): CompanyDetails = {
    val name = document.getString(IdAttribute)
    val description = document.getString(AttributeCompanyDescription)
    CompanyDetails(name, description)
  }
}
