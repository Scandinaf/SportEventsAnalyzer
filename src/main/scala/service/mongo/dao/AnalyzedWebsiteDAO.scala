package service.mongo.dao

import model.Website
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Indexes._
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.model.AnalyzedWebsite
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
      with MongoResultHelper[AnalyzedWebsite] {

    import AnalyzedWebsite.Field._

    initializeIndexes

    def findByDomain(domain: String) =
      collection.find(equal(AnalyzedWebsite.Field.domain, domain)).get

    def findByDomains(websites: Seq[Website]) =
      if (websites.isEmpty) Seq.empty
      else {
        val query = and(in(domain, websites.map(_.domain)),
                        lte(expirationDate, DateHelper.getCurrentDate))
        collection.find(query).getAll
      }

    private def initializeIndexes =
      collection.createIndex(ascending(domain),
                             IndexOptions()
                               .name(s"asc_${domain}")
                               .background(false)
                               .unique(true))
  }
}
