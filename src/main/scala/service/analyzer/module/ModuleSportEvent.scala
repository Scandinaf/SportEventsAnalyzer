package service.analyzer.module

import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}
import service.analyzer.exception.Error
import service.analyzer.module.builder.ModelBuilder
import service.analyzer.module.handler.Handler
import service.logging.Logger
import service.mongo.model.SportEvent
import service.utils.ImplicitHelper.VectorImplicits.flattenWithLog
import service.utils.TimeDateUtil._

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait ModuleSportEvent extends Module {
  _: Handler[SportEvent] with ModelBuilder[SportEvent] with Logger =>
  def process(elementQuery: ElementQuery[Element]): Either[Error, Unit] =
    if (elementQuery.size > 1)
      validate(getHeadersMap(elementQuery)).fold(
        l =>
          Left(
            Error(s"The following keys were not found: ${l.mkString(",")}.")),
        headersMap => {
          val sportEvents: Vector[SportEvent] =
            elementQuery.tail.map(e => build(headersMap, e)).toVector
          Right(
            handleCollection(
              checkDateInRange[SportEvent](sportEvents, DayRange, _.date)))
        }
      )
    else
      Left(Error(
        s"The transmitted data is incorrect. Module - ${getClass.getName}. Size - ${elementQuery.size}"))

  def getHeadersMap(elementQuery: ElementQuery[Element]) =
    elementQuery.head.children
      .map(_.text)
      .zipWithIndex
      .toMap
}
