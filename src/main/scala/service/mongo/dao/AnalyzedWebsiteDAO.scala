package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.model.{AnalyzedWebsite, MongoObject}

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

    val duration = 1.minute

    def findById(_id: ObjectId): Seq[AnalyzedWebsite] =
      collection.find(equal(MongoObject.Field._id, _id)).getAll
  }
}
