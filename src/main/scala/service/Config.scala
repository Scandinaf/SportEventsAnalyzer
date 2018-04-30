package service

/**
  * Created by serge on 05.12.2017.
  */
import com.typesafe.config._
import model.{MongoDBSettings, PostEventStatistics}

object Config {
  private val config = ConfigFactory.load()
  lazy val mongoDBSettings =
    MongoDBSettings(config.getConfig(MongoDBSettings.Config.blockName))
  lazy val postEventStatisticsParimatchSettings =
    PostEventStatistics(
      config.getConfig(PostEventStatistics.Config.getBlockName("parimatch")))
}
