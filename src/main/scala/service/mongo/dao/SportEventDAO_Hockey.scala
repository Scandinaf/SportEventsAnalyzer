package service.mongo.dao

trait SportEventDAO_Hockey extends SportEventHelper {
  private val collectionName = "sportEvent_Hockey"
  val sportEventDAO_Hockey = new SportEventDAOComponent(collectionName)
}
