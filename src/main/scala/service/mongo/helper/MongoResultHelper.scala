package service.mongo.helper

import org.mongodb.scala.FindObservable
import service.mongo.model.MongoObject

import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration

/**
  * Created by serge on 11.12.2017.
  */
trait MongoResultHelper[T <: MongoObject] {
  val duration: FiniteDuration
  implicit class FindObservableImpl[T](ob: FindObservable[T]) {
    def getAll =
      Await.result(ob.toFuture(), duration)
  }
}
