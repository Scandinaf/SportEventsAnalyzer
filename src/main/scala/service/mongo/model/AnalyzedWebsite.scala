package service.mongo.model

import java.util.Date

import org.mongodb.scala.bson.ObjectId

/**
  * Created by serge on 11.12.2017.
  */
case class AnalyzedWebsite(_id: ObjectId = new ObjectId(),
                           domain: String,
                           update: Date = new Date())
    extends MongoObject

object AnalyzedWebsite {
  object Field {
    val domain = "domain"
    val update = "update"
  }
}
