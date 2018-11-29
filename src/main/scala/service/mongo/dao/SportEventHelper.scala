package service.mongo.dao

import com.github.nscala_time.time.Imports.DateTime
import org.mongodb.scala.model.{UpdateOneModel, UpdateOptions}
import org.mongodb.scala.{Completed, MongoCollection}
import service.collector.statistics.model.PostEventWinInformation
import service.logging.Logger
import service.mongo.MongoDBConnector.db.getCollection
import service.mongo.bson.{SportEventBsonBuilder, SportEventQueryBuilder}
import service.mongo.indexes.SportEventIndexInitializer
import service.mongo.model.SportEvent
import service.mongo.observer.CommonObserver
import service.utils.TimeDateUtil

trait SportEventHelper {
  class SportEventDAOComponent(collectionName: String)
      extends SportEventBsonBuilder
      with SportEventQueryBuilder
      with SportEventIndexInitializer
      with Logger {

    val collection: MongoCollection[SportEvent] = getCollection(collectionName)
    val betChangesCollection: MongoCollection[SportEvent] =
      getCollection(s"betChanges_$collectionName")

    initializeIndexes

    def updateResults(l: Vector[PostEventWinInformation]) =
      TimeDateUtil.getStartEndDate(DateTime.yesterday.toDate) match {
        case (sD, eD) =>
          collection.bulkWrite(
            l.map(e =>
              UpdateOneModel.apply(getUpdateResultsFilter(e, sD, eD),
                                   getSetEventResult(e.eventResult))))
      }

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
              UpdateOptions().upsert(true)))) match {
        case x =>
          betChangesCollection
            .insertMany(l)
            .subscribe(new CommonObserver[Completed])
          x
      }
  }
}
