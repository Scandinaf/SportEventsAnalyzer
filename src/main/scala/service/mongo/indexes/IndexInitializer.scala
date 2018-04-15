package service.mongo.indexes

import org.mongodb.scala.MongoCollection
import service.mongo.helper.IndexHelper
import service.mongo.model.MongoObject

trait IndexInitializer[T <: MongoObject] extends IndexHelper[T] {
  val collection: MongoCollection[T]
  protected def initializeIndexes
}
