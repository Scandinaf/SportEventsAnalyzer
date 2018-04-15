package service.mongo.dao

import java.util.Date

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.BsonNull
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates.{combine, set, setOnInsert}
import org.mongodb.scala.model.{UpdateOneModel, UpdateOptions}
import service.collector.statistics.model.PostEventWinInformation
import service.logging.Logger
import service.mongo.helper.MongoResultHelper
import service.mongo.indexes.SportEventIndexInitializer
import service.mongo.model.MongoObject.Field._id
import service.mongo.model.SportEvent.Field._
import service.mongo.model.{EventResult, SportEvent}

trait SportEventHelper {
  class SportEventDAOComponent(val collection: MongoCollection[SportEvent])
      extends BaseDAOComponent[SportEvent]
      with MongoResultHelper[SportEvent]
      with SportEventIndexInitializer
      with Logger {
    initializeIndexes

    def updateResults(l: Vector[PostEventWinInformation]) =
      collection.bulkWrite(
        l.map(
          e =>
            UpdateOneModel.apply(
              getDateEventFilter(e.date, s"${e.firstTeam} - ${e.secondTeam}"),
              getSetEventResult(e.eventResult))))

    def insertOrUpdate(v: SportEvent) =
      collection.updateOne(
        getDateEventFilter(v.date, v.event),
        getSetOnIsertUpdate(v),
        UpdateOptions().upsert(true)
      )

    def insertOrUpdate(l: Vector[SportEvent]) =
      collection.bulkWrite(
        l.map(
          r =>
            UpdateOneModel.apply(getDateEventFilter(r.date, r.event),
                                 getSetOnIsertUpdate(r),
                                 UpdateOptions().upsert(true))))

    protected def getSetEventResult(er: EventResult) =
      set(eventResult, er)

    protected def getSetOnIsertUpdate(v: SportEvent) =
      combine(
        set(bet, v.bet),
        combine(
          setOnInsert(_id, v._id),
          setOnInsert(event, v.event),
          setOnInsert(firstTeam, v.firstTeam),
          setOnInsert(secondTeam, v.secondTeam),
          setOnInsert(date, v.date),
          setOnInsert(eventResult, BsonNull())
        )
      )

    protected def getDateEventFilter(d: Date, e: String) =
      and(equal(date, d), equal(event, e))
  }
}
