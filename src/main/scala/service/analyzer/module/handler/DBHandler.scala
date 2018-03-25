package service.analyzer.module.handler

import service.logging.Logger
import service.mongo.dao.BaseDAOComponent
import service.mongo.model.MongoObject

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait DBHandler[T <: MongoObject]
    extends Handler[T]
    with Logger {
  val dao: BaseDAOComponent[T]

  def handle(obj: T) =
    dao
      .insert(obj)
      .subscribe((err: Throwable) => logger.error(err.toString),
                 () => logger.info("OK"))
}
