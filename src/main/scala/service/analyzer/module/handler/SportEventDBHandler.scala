package service.analyzer.module.handler

import org.mongodb.scala.BulkWriteResult
import org.mongodb.scala.result.UpdateResult
import service.logging.Logger
import service.mongo.DBLayer
import service.mongo.model.SportEvent
import service.mongo.observer.CommonObserver

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait SportEventDBHandler extends Handler[SportEvent] {
  _: Logger =>
  val dao: DBLayer.SportEventDAOComponent

  def handle(obj: SportEvent) =
    dao
      .insertOrUpdate(obj)
      .subscribe(new CommonObserver[UpdateResult])

  def handleCollection(collection: Vector[SportEvent]) =
    if (collection.nonEmpty)
      dao
        .insertOrUpdate(collection)
        .subscribe(new CommonObserver[BulkWriteResult])
}
