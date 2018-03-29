package service.mongo.dao

import service.mongo.MongoDBConnector

/**
  * Created by serge on 24.03.2018.
  */
trait SportEventDAO_Football extends SportEventHelper {
  private val collectionName = "sportEvent_Football"
  val sportEventDAO_Football = new SportEventDAOComponent(
    MongoDBConnector.db.getCollection(collectionName))
}
