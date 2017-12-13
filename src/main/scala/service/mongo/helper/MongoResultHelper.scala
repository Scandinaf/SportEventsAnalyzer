package service.mongo.helper

import model.ApplicationSettings.timeout
import org.mongodb.scala.FindObservable
import service.mongo.model.MongoObject

import scala.concurrent.Await

/**
  * Created by serge on 11.12.2017.
  */
trait MongoResultHelper[T <: MongoObject] {
  implicit class FindObservableImpl[T](ob: FindObservable[T]) {
    def getAll =
      Await.result(ob.toFuture(), timeout)
    def get = getAll.headOption
  }
}
