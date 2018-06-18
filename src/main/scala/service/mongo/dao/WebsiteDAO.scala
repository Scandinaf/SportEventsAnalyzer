package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.InsertManyOptions
import service.logging.Logger
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.indexes.WebsiteIndexInitializer
import service.mongo.model.Website

trait WebsiteDAO {
  private val collectionName = "websites"
  val websiteDAO = new WebsiteDAOComponent(
    MongoDBConnector.db.getCollection(collectionName))

  class WebsiteDAOComponent(val collection: MongoCollection[Website])
      extends BaseDAOComponent[Website]
      with MongoResultHelper[Website]
      with WebsiteIndexInitializer
      with Logger {
    initializeIndexes

    def insert(elements: Vector[Website]) =
      collection.insertMany(elements, InsertManyOptions().ordered(false))

    def getAll = collection.find().toFuture()

    def drop = collection.drop.toFuture()
  }
}
