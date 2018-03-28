package service.utils

import model.ApplicationSettings.Actor.forkJoinEC
import service.logging.Logger

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Try}

object ImplicitHelper extends Logger {
  object VectorImplicits {
    implicit def flattenWithLog[T](collection: Vector[Try[T]]): Vector[T] = {
      val r = collection.span(_.isSuccess)
      Future {
        r._2.map({
          case Failure(err) =>
            logger.error("Something went wrong!!!", err)
          case uV =>
            logger.warn(s"Not found handler for the next type. Obj - $uV")
        })
      }
      r._1.map(_.get)
    }
  }
}
