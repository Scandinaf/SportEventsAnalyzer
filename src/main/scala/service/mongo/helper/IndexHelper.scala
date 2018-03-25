package service.mongo.helper

import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.{IndexOptions, Indexes}
import org.mongodb.scala.{MongoCollection, SingleObservable}
import service.mongo.model.MongoObject
import service.mongo.observer.IndexObserver

trait IndexHelper[T <: MongoObject] {
  val collection: MongoCollection[T]

  def createAscendingIndex(fldName: String) = {
    val indexName = s"asc_$fldName"
    createIndex(indexName,
                Indexes.ascending(fldName),
                getDefaultIndexOptions(indexName))
  }

  def createDescendingIndex(fldName: String) = {
    val indexName = s"des_$fldName"
    createIndex(indexName,
                Indexes.descending(fldName),
                getDefaultIndexOptions(indexName))
  }

  def createTextIndex(fldName: String) = {
    val indexName = s"text_$fldName"
    createIndex(indexName,
                Indexes.text(fldName),
                getDefaultIndexOptions(indexName))
  }

  def createIndex(indexName: String, key: Bson, opt: IndexOptions) =
    addIndexObserver(collection.createIndex(key, opt), indexName)

  protected def addIndexObserver(singleObservable: SingleObservable[String],
                                 indexName: String) =
    singleObservable.subscribe(IndexObserver(indexName))

  protected def getDefaultIndexOptions(indexName: String) =
    IndexOptions().name(indexName)
}
