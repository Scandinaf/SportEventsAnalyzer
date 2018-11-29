package service.mongo.indexes

import org.mongodb.scala.MongoCollection
import org.mongodb.scala.model.{IndexOptions, Indexes}
import service.mongo.helper.ImplicitHelpers.IndexHelper
import service.mongo.model.SportEvent
import service.mongo.model.SportEvent.Field._

trait SportEventIndexInitializer extends IndexInitializer {
  val collection: MongoCollection[SportEvent]
  val betChangesCollection: MongoCollection[SportEvent]

  override protected def initializeIndexes: Unit = {
    collection.createAscendingIndex(firstTeam)
    collection.createAscendingIndex(secondTeam)
    collection.createDescendingIndex(date)
    createDateEventIndex(collection,
                         IndexOptions().name(s"$date-$event").unique(true))
    createDateEventIndex(betChangesCollection,
                         IndexOptions().name(s"$date-$event").background(true))
  }

  protected def createDateEventIndex(collection: MongoCollection[SportEvent],
                                     opt: IndexOptions) =
    collection.createIndex(
      s"$date-$event",
      Indexes.compoundIndex(Indexes.descending(date), Indexes.ascending(event)),
      opt)
}
