package service.mongo.model

case class EventResult(score: Option[String] = None,
                       winner: Option[String] = None)
object EventResult {
  object Field {
    val score = "score"
    val winner = "winner"
  }
}
