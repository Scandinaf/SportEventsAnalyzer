package service.mongo.dao

import org.mongodb.scala.MongoCollection
import service.logging.Logger
import service.mongo.MongoDBConnector
import service.mongo.helper.MongoResultHelper
import service.mongo.indexes.SportEventIndexInitializer
import service.mongo.model.SportEvent

/**
  * Created by serge on 24.03.2018.
  */
trait SportEventDAO {
  private val collectionName = "sportEvent"
  val sportEventDAO = new SportEventDAOComponent(
    MongoDBConnector.db.getCollection(collectionName))

  protected class SportEventDAOComponent(
      val collection: MongoCollection[SportEvent])
      extends BaseDAOComponent[SportEvent]
      with MongoResultHelper[SportEvent]
      with SportEventIndexInitializer
      with Logger {
    initializeIndexes
  }
}
