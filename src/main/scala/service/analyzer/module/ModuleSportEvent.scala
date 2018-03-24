package service.analyzer.module

import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}
import service.analyzer.module.builder.ModelBuilder
import service.analyzer.module.handler.Handler
import service.logging.Logger
import service.mongo.model.SportEvent

import scala.util.{Failure, Success}

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait ModuleSportEvent extends Module {
  _: Handler[SportEvent] with ModelBuilder[SportEvent] with Logger =>
  def process(elementQuery: ElementQuery[Element]) =
    if (elementQuery.size > 2) {
      val textPosMap = getTextPosMap(elementQuery)
      elementQuery.tail.foreach(e =>
        build(textPosMap, e) match {
          case Success(v) => handle(v)
          case Failure(err) =>
            logger.error("The model build process failed.", err)
      })
    }

  def getTextPosMap(elementQuery: ElementQuery[Element]) =
    elementQuery.head.children
      .map(_.text)
      .zipWithIndex
      .toMap
}
