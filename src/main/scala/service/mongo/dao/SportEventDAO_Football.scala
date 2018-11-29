package service.mongo.dao

/**
  * Created by serge on 24.03.2018.
  */
trait SportEventDAO_Football extends SportEventHelper {
  private val collectionName = "sportEvent_Football"
  val sportEventDAO_Football = new SportEventDAOComponent(collectionName)
}
