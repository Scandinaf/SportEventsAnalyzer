package service.mongo.dao

import service.mongo.MongoDBConnector

trait SportEventDAO_Hockey extends SportEventHelper {
  private val collectionName = "sportEvent_Hockey"
  val sportEventDAO_Hockey = new SportEventDAOComponent(
    MongoDBConnector.db.getCollection(collectionName))
}
