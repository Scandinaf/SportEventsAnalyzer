package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes._
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.model.AnalyzedWebsite

import scala.concurrent.duration._

/**
  * Created by serge on 11.12.2017.
  */
trait AnalyzedWebsiteDAO {
  private val collectionName = "analyzedWebsite"
  val analyzedWebsiteDAO = new AnalyzedWebsiteDAOComponent(
    MongoDBConnector.db.getCollection(collectionName))

  protected class AnalyzedWebsiteDAOComponent(
      protected val collection: MongoCollection[AnalyzedWebsite])
      extends BaseDAOComponent[AnalyzedWebsite]
      with MongoResultHelper[AnalyzedWebsite] {

    initializeIndexes

    val duration = 1.minute

    def findByDomain(domain: String) =
      collection.find(equal(AnalyzedWebsite.Field.domain, domain)).get

    private def initializeIndexes =
      collection.createIndex(ascending(AnalyzedWebsite.Field.domain),
                             IndexOptions()
                               .name(s"asc_${AnalyzedWebsite.Field.domain}")
                               .background(false)
                               .unique(true))
  }
}
