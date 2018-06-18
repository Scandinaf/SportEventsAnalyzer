package service.mongo.bson

import java.util.Date

import org.bson.conversions.Bson
import org.mongodb.scala.model.Filters.{and, equal}
import service.collector.statistics.model.PostEventWinInformation
import service.mongo.model.SportEvent.Field.{date, firstTeam, secondTeam}

trait SportEventQueryBuilder extends QueryBuilder {

  protected def getUpdateResultsFilter(p: PostEventWinInformation,
                                       sD: Date,
                                       eD: Date) =
    (p.firstTeam.endsWith("."),
     p.secondTeam.endsWith("."),
     getDateQuery(date, p.date, sD, eD)) match {
      case (true, false, dB) =>
        getDateEventFilter(dB,
                           getRegexQuery(firstTeam, s"^${p.firstTeam}*"),
                           equal(secondTeam, p.secondTeam))
      case (false, true, dB) =>
        getDateEventFilter(dB,
                           equal(firstTeam, p.firstTeam),
                           getRegexQuery(secondTeam, s"^${p.secondTeam}*"))
      case (true, true, dB) =>
        getDateEventFilter(dB,
                           getRegexQuery(firstTeam, s"^${p.firstTeam}*"),
                           getRegexQuery(secondTeam, s"^${p.secondTeam}*"))
      case (_, _, dB) =>
        getDateEventFilter(dB,
                           equal(firstTeam, p.firstTeam),
                           equal(secondTeam, p.secondTeam))
    }

  protected def getDateEventFilter(d: Date, fT: String, sT: String) =
    and(equal(date, d), equal(firstTeam, fT), equal(secondTeam, sT))

  protected def getDateEventFilter(d: Bson, fT: Bson, sT: Bson) = and(d, fT, sT)
}
