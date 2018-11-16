package service

/**
  * Created by serge on 05.12.2017.
  */
import com.typesafe.config._
import model.{MongoDBSettings, PostEventStatistics, TeamSportConfig}

object Config {
  private val config = ConfigFactory.load()
  lazy val mongoDBSettings =
    MongoDBSettings(config.getConfig(MongoDBSettings.Config.blockName))

  object Parimatch {
    private val pm_config = ConfigFactory.load("parimatch.conf")
    lazy val baseUrl = pm_config.getString("baseUrl")
    lazy val footballConfig = TeamSportConfig(pm_config.getConfig("football"))
    lazy val hockeyConfig = TeamSportConfig(pm_config.getConfig("hockey"))
    lazy val postEventStatisticsSettings =
      PostEventStatistics(
        pm_config.getConfig(PostEventStatistics.Config.blockName))
  }
}
