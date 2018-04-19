package service.mongo.bson

import org.mongodb.scala.model.Filters.regex

trait QueryBuilder {
  protected def getRegexQuery(fldN: String, pattern: String) =
    regex(fldN, pattern)
}
