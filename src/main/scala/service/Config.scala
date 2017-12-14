package service

/**
  * Created by serge on 05.12.2017.
  */
import com.typesafe.config._
import model.{MongoDBSettings, Website}

import scala.collection.JavaConverters._

object Config {
  private val config = ConfigFactory.load()

  def websites = {
    val configList =
      config.getConfigList(Website.Config.blockName).asScala.toVector
    configList.map(Website(_))
  }

  def mongoDBSettings =
    MongoDBSettings(config.getConfig(MongoDBSettings.Config.blockName))
}
