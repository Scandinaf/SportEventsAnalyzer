package service.mongo.bson

import java.util.Date

import org.mongodb.scala.model.Filters._

trait QueryBuilder {
  protected def getRegexQuery(fldN: String, pattern: String) =
    regex(fldN, pattern)

  protected def getDateQuery(fldName: String, d: Date, sD: Date, eD: Date) =
    or(equal(fldName, d), and(gte(fldName, sD), lte(fldName, eD)))
}
