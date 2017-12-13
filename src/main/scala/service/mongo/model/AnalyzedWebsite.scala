package service.mongo.model

import java.util.Date

import org.mongodb.scala.bson.ObjectId
import util.DateHelper

/**
  * Created by serge on 11.12.2017.
  */
case class AnalyzedWebsite(_id: ObjectId = new ObjectId(),
                           domain: String,
                           expirationDate: Date = DateHelper.getDate())
    extends MongoObject

object AnalyzedWebsite {
  object Field {
    val domain = "domain"
    val expirationDate = "expirationDate"
  }
}
