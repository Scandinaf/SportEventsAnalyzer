package service

/**
  * Created by serge on 05.12.2017.
  */
import com.typesafe.config._
import model.{MongoDBSettings, PostEventStatisticsParimatch, Website}

import scala.collection.JavaConverters._

object Config {
  private val config = ConfigFactory.load()

  lazy val websites =
    config
      .getConfigList(Website.Config.blockName)
      .asScala
      .toVector
      .map(Website(_))

  lazy val mongoDBSettings =
    MongoDBSettings(config.getConfig(MongoDBSettings.Config.blockName))

  lazy val postEventStatisticsParimatchSettings =
    PostEventStatisticsParimatch(
      config.getConfig(PostEventStatisticsParimatch.Config.blockName))
}
