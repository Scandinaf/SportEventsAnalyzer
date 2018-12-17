package service.mongo.model

import java.util.Date

import org.mongodb.scala.bson.ObjectId

/**
  * Created by serge on 08.03.2018.
  */
case class SportEvent(event: String,
                      firstTeam: String,
                      secondTeam: String,
                      date: Date,
                      bet: Bet,
                      eventResult: Option[EventResult] = None)
    extends MongoObject

object SportEvent {
  object Field {
    val event = "event"
    val firstTeam = "firstTeam"
    val secondTeam = "secondTeam"
    val date = "date"
    val bet = "bet"
    val eventResult = "eventResult"
  }
}
