package service.mongo

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import service.mongo.model.{Bet, EventResult, SportEvent}

/**
  * Created by serge on 11.12.2017.
  */
protected object Codecs {
  val codecRegistry = fromRegistries(
    fromProviders(classOf[SportEvent], classOf[Bet], classOf[EventResult]),
    DEFAULT_CODEC_REGISTRY)
}
