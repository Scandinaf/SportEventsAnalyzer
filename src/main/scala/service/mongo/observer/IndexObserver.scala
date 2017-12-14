package service.mongo.observer

import org.mongodb.scala.Observer
import service.logging.Logger

/**
  * Created by serge on 14.12.2017.
  */
class IndexObserver(val indexName: String)
    extends Observer[String]
    with Logger {
  override def onError(e: Throwable): Unit =
    logger.error(
      s"Index creation process was interrupted. Index - $indexName, ex - $e")

  override def onComplete(): Unit =
    logger.info(s"Index $indexName was created successfully!!!")

  override def onNext(result: String): Unit = super.onNext(result)
}

object IndexObserver {
  def apply(indexName: String): IndexObserver = new IndexObserver(indexName)
}
