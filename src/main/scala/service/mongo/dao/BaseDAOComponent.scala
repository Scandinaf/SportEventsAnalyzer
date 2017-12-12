package service.mongo.dao

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import service.mongo.helper.MongoResultHelper
import service.mongo.model.MongoObject

/**
  * Created by serge on 11.12.2017.
  */
trait BaseDAOComponent[T <: MongoObject] {
  self: MongoResultHelper[T] =>
  protected val collection: MongoCollection[T]

  def insert(obj: T) =
    collection.insertOne(obj)

  def replaceById(obj: T) =
    collection.replaceOne(equal(MongoObject.Field._id, obj._id), obj)

  def deleteById(_id: ObjectId) =
    collection.deleteOne(equal(MongoObject.Field._id, _id))

  def findAllById(_id: ObjectId): Seq[T] =
    collection.find(equal(MongoObject.Field._id, _id)).getAll

  def findById(_id: ObjectId): Option[T] =
    collection.find(equal(MongoObject.Field._id, _id)).get
}
