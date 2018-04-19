package service.mongo.bson

import java.util.Date

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters.{and, equal}
import service.collector.statistics.model.PostEventWinInformation
import service.mongo.model.SportEvent.Field.{date, firstTeam, secondTeam}

trait SportEventQueryBuilder extends QueryBuilder {

  protected def getDateEventFilter(d: Date, fT: String, sT: String) =
    and(equal(date, d), equal(firstTeam, fT), equal(secondTeam, sT))

  protected def getDateEventFilter(d: Date, bson: Bson, sT: String) =
    and(equal(date, d), bson, equal(secondTeam, sT))

  protected def getDateEventFilter(d: Date, fT: String, bson: Bson) =
    and(equal(date, d), equal(firstTeam, fT), bson)

  protected def getDateEventFilter(d: Date, v1: Bson, v2: Bson) =
    and(equal(date, d), v1, v2)
  protected def getUpdateResultsFilter(p: PostEventWinInformation) =
    (p.firstTeam.endsWith("."), p.secondTeam.endsWith(".")) match {
      case (true, false) =>
        getDateEventFilter(p.date,
                           getRegexQuery(firstTeam, s"^${p.firstTeam}*"),
                           p.secondTeam)
      case (false, true) =>
        getDateEventFilter(p.date,
                           p.firstTeam,
                           getRegexQuery(secondTeam, s"^${p.secondTeam}*"))
      case (true, true) =>
        getDateEventFilter(p.date,
                           getRegexQuery(firstTeam, s"^${p.firstTeam}*"),
                           getRegexQuery(secondTeam, s"^${p.secondTeam}*"))
      case _ => getDateEventFilter(p.date, p.firstTeam, p.secondTeam)
    }
}
