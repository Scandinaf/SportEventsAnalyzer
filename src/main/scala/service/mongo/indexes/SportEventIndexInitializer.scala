package service.mongo.indexes

import org.mongodb.scala.model.{IndexOptions, Indexes}
import service.mongo.model.SportEvent
import service.mongo.model.SportEvent.Field._

trait SportEventIndexInitializer extends IndexInitializer[SportEvent] {
  override protected def initializeIndexes: Unit = {
    createAscendingIndex(firstTeam)
    createAscendingIndex(secondTeam)
    createDescendingIndex(date)
    createDateEventIndex
  }

  protected def createDateEventIndex = {
    val indexName = s"$date-$event"
    createIndex(
      indexName,
      Indexes.compoundIndex(Indexes.descending(date), Indexes.ascending(event)),
      IndexOptions().name(indexName).unique(true))
  }
}
