package service.mongo.indexes

import org.mongodb.scala.MongoCollection
import service.mongo.helper.ImplicitHelpers.IndexHelper
import service.mongo.model.Website
import service.mongo.model.Website.Field.tag

trait WebsiteIndexInitializer extends IndexInitializer {
  val collection: MongoCollection[Website]

  override protected def initializeIndexes: Unit =
    collection.createAscendingIndex(tag)
}
