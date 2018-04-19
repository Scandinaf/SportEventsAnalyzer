package service.utils

import java.util.Calendar

import model.ApplicationSettings.Actor.forkJoinEC
import service.logging.Logger

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Try}

object ImplicitHelper extends Logger {
  object CalendarImplicits {
    implicit class CalendarBuilder(c: Calendar) {
      def setB(field: Int, value: Int) = {
        c.set(field, value)
        c
      }

      def addB(field: Int, value: Int) = {
        c.add(field, value)
        c
      }
    }
  }

  object VectorImplicits {

    private def logFailureElements(v: Vector[Try[_]]) =
      if (v.nonEmpty)
        Future {
          v.map({
            case Failure(err) =>
              logger.error("Something went wrong!!!", err)
            case uV =>
              logger.warn(s"Not found handler for the next type. Obj - $uV")
          })
        }

    implicit def flattenWithLog[T](collection: Vector[Try[T]]): Vector[T] = {
      val r = collection.partition(_.isSuccess)
      logFailureElements(r._2)
      r._1.map(_.get)
    }
  }
}
