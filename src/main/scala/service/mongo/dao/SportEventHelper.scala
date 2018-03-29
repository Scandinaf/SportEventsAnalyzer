package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates.{combine, set, setOnInsert}
import org.mongodb.scala.model.{UpdateOneModel, UpdateOptions}
import service.logging.Logger
import service.mongo.helper.MongoResultHelper
import service.mongo.indexes.SportEventIndexInitializer
import service.mongo.model.MongoObject.Field._id
import service.mongo.model.SportEvent
import service.mongo.model.SportEvent.Field._

trait SportEventHelper {
  class SportEventDAOComponent(val collection: MongoCollection[SportEvent])
      extends BaseDAOComponent[SportEvent]
      with MongoResultHelper[SportEvent]
      with SportEventIndexInitializer
      with Logger {
    initializeIndexes

    def insertOrUpdate(v: SportEvent) =
      collection.updateOne(
        getDateEventFilter(v),
        getSetOnIsertUpdate(v),
        UpdateOptions().upsert(true)
      )

    def insertOrUpdate(l: Vector[SportEvent]) =
      collection.bulkWrite(
        l.map(
          r =>
            UpdateOneModel.apply(getDateEventFilter(r),
                                 getSetOnIsertUpdate(r),
                                 UpdateOptions().upsert(true))))

    protected def getSetOnIsertUpdate(v: SportEvent) =
      combine(
        set(bet, v.bet),
        combine(setOnInsert(_id, v._id),
                setOnInsert(event, v.event),
                setOnInsert(firstTeam, v.firstTeam),
                setOnInsert(secondTeam, v.secondTeam),
                setOnInsert(date, v.date))
      )

    protected def getDateEventFilter(v: SportEvent) =
      and(equal(date, v.date), equal(event, v.event))
  }
}
