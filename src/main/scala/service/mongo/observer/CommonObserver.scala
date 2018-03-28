package service.mongo.observer

import org.mongodb.scala.Observer
import service.logging.Logger

class CommonObserver[T] extends Observer[T] with Logger {
  override def onNext(result: T): Unit = {}

  override def onError(e: Throwable): Unit =
    logger.error(
      "In the process of working with the database, something went wrong.",
      e)

  override def onComplete(): Unit = {}
}
