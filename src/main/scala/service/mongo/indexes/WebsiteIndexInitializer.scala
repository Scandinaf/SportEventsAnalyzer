package service.mongo.indexes

import service.mongo.model.Website
import service.mongo.model.Website.Field.tag

trait WebsiteIndexInitializer extends IndexInitializer[Website] {
  override protected def initializeIndexes: Unit =
    createAscendingIndex(tag)
}
