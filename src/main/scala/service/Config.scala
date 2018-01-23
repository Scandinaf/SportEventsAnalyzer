package service

/**
  * Created by serge on 05.12.2017.
  */
import com.typesafe.config._
import model.{KeyWord, MongoDBSettings, Website}

import scala.collection.JavaConverters._

object Config {
  private val config = ConfigFactory.load()

  lazy val keyWords =
    config
      .getConfigList(KeyWord.Config.blockName)
      .asScala
      .toVector
      .map(KeyWord(_))

  def websites = {
    val configList =
      config.getConfigList(Website.Config.blockName).asScala.toVector
    configList.map(Website(_))
  }

  def mongoDBSettings =
    MongoDBSettings(config.getConfig(MongoDBSettings.Config.blockName))
}
