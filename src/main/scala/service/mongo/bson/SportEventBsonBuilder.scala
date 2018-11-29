package service.mongo.bson

import org.mongodb.scala.bson.BsonNull
import org.mongodb.scala.model.Updates.{combine, set, setOnInsert}
import service.mongo.model.SportEvent.Field._
import service.mongo.model.{EventResult, SportEvent}

trait SportEventBsonBuilder {
  protected def getSetOnInsertUpdate(v: SportEvent) =
    combine(
      set(bet, v.bet),
      combine(
        setOnInsert(event, v.event),
        setOnInsert(firstTeam, v.firstTeam),
        setOnInsert(secondTeam, v.secondTeam),
        setOnInsert(date, v.date),
        setOnInsert(eventResult, BsonNull())
      )
    )

  protected def getSetEventResult(er: EventResult) =
    set(eventResult, er)
}
