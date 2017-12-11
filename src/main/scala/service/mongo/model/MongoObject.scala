package service.mongo.model

import org.mongodb.scala.bson.ObjectId

/**
  * Created by serge on 11.12.2017.
  */
trait MongoObject {
  val _id: ObjectId
}

object MongoObject {
  object Field {
    val _id = "_id"
  }
}
