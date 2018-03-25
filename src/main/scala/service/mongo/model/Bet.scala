package service.mongo.model

/**
  * Created by serge on 08.03.2018.
  */
case class Bet(winF: Double,
               draw: Option[Double] = None,
               winS: Double,
               winFD: Option[Double] = None,
               winSD: Option[Double] = None)

object Bet {
  object Field {
    val winF = "winF"
    val draw = "draw"
    val winS = "winS"
    val winFD = "winFD"
    val winSD = "winSD"
  }
}
