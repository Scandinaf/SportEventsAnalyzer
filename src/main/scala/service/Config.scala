package service

/**
  * Created by serge on 05.12.2017.
  */

import com.typesafe.config._
import model.Website

import scala.collection.JavaConverters._

object Config {
  private val config = ConfigFactory.load()

  lazy val websites = {
    val configList: Seq[Config] = config.getConfigList(Website.Config.blockName).asScala
    configList map (Website(_))
  }
}


