package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.{UpdateOneModel, UpdateOptions}
import service.collector.statistics.model.PostEventWinInformation
import service.logging.Logger
import service.mongo.bson.{SportEventBsonBuilder, SportEventQueryBuilder}
import service.mongo.helper.MongoResultHelper
import service.mongo.indexes.SportEventIndexInitializer
import service.mongo.model.SportEvent

trait SportEventHelper {
  class SportEventDAOComponent(val collection: MongoCollection[SportEvent])
      extends BaseDAOComponent[SportEvent]
      with MongoResultHelper[SportEvent]
      with SportEventBsonBuilder
      with SportEventQueryBuilder
      with SportEventIndexInitializer
      with Logger {
    initializeIndexes

    def updateResults(l: Vector[PostEventWinInformation]) =
      collection.bulkWrite(
        l.map(e =>
          UpdateOneModel.apply(getUpdateResultsFilter(e),
                               getSetEventResult(e.eventResult))))

    def insertOrUpdate(v: SportEvent) =
      collection.updateOne(
        getDateEventFilter(v.date, v.firstTeam, v.secondTeam),
        getSetOnInsertUpdate(v),
        UpdateOptions().upsert(true)
      )

    def insertOrUpdate(l: Vector[SportEvent]) =
      collection.bulkWrite(
        l.map(
          r =>
            UpdateOneModel.apply(
              getDateEventFilter(r.date, r.firstTeam, r.secondTeam),
              getSetOnInsertUpdate(r),
              UpdateOptions().upsert(true))))
  }
}
