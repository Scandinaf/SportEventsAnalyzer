package service.mongo.model

import org.mongodb.scala.bson.ObjectId

case class Website(_id: ObjectId = new ObjectId(),
                   url: String,
                   cssQuery: String,
                   module: String,
                   tag: String)
    extends MongoObject

object Website {
  object Tag {
    val football = "football"
  }

  object Field {
    val url = "url"
    val cssQuery = "cssQuery"
    val module = "module"
    val tag = "tag"
  }
}
