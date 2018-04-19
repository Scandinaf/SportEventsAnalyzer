package service.mongo.model

case class EventResult(score: Option[String] = None,
                       winner: Option[Int] = None,
                       eventTookPlace: Boolean = true)
object EventResult {
  def apply: EventResult = new EventResult(eventTookPlace = false)

  def apply(score: String, winner: Winner): EventResult =
    new EventResult(winner = Some(winner.numberV), score = Some(score))

  sealed trait Winner { protected[model] val numberV: Int }
  case object FirstTeam extends Winner { protected[model] val numberV = 1 }
  case object SecondTeam extends Winner { protected[model] val numberV = 2 }
  case object Draw extends Winner { protected[model] val numberV = 3 }

  object Field {
    val score = "score"
    val winner = "winner"
  }
}
