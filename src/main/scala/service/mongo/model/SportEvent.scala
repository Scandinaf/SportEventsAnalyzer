package service.mongo.model

import java.util.Date

import org.mongodb.scala.bson.ObjectId

/**
  * Created by serge on 08.03.2018.
  */
case class SportEvent(_id: ObjectId = new ObjectId(),
                      event: String,
                      firstTeam: String,
                      secondTeam: String,
                      date: Date,
                      bet: Bet)
    extends MongoObject

object SportEvent {
  object Field {
    val event = "event"
    val firstTeam = "firstTeam"
    val secondTeam = "secondTeam"
    val date = "date"
    val bet = "bet"
  }
}
