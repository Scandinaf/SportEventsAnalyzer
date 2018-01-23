package service.mongo.dao

import model.Website
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes._
import service.logging.Logger
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.model.AnalyzedWebsite
import service.mongo.observer.IndexObserver
import util.DateHelper

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
      with MongoResultHelper[AnalyzedWebsite]
      with Logger {

    import AnalyzedWebsite.Field._

    initializeIndexes

    def findByDomain(domain: String) =
      collection.find(equal(AnalyzedWebsite.Field.domain, domain)).get

    def findByDomains(websites: Vector[Website]) =
      if (websites.isEmpty) Vector.empty
      else {
        val query = and(in(domain, websites.map(_.domain)),
                        gte(expirationDate, DateHelper.getCurrentDate))
        collection.find(query).getAll
      }

    private def initializeIndexes = {
      val dIN = s"asc_${domain}"
      collection
        .createIndex(ascending(domain),
                     IndexOptions()
                       .name(dIN)
                       .background(false)
                       .unique(true))
        .subscribe(IndexObserver(dIN))
    }
  }
}
