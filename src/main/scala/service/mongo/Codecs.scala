package service.mongo

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import service.mongo.model.{AnalyzedWebsite, Bet, SportEvent}

/**
  * Created by serge on 11.12.2017.
  */
protected object Codecs {
  val codecRegistry = fromRegistries(
    fromProviders(classOf[AnalyzedWebsite], classOf[SportEvent], classOf[Bet]),
    DEFAULT_CODEC_REGISTRY)
}
