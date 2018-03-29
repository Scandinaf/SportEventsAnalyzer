package model

import com.typesafe.config.Config

import scala.collection.JavaConverters._

/**
  * Created by serge on 05.12.2017.
  */
case class Website(protocol: String,
                   domain: String,
                   pages: Vector[Page],
                   cssQuery: String)

object Website {
  def apply(config: Config): Website = {
    val cssQuery = config.getString(Config.cssQuery)
    new Website(
      config.getString(Config.protocol),
      config.getString(Config.domain),
      config
        .getConfigList(Config.pages)
        .asScala
        .toVector
        .map(Page(_, cssQuery)),
      cssQuery
    )
  }

  object Config {
    val blockName = "websites"
    val protocol = "protocol"
    val domain = "domain"
    val pages = "pages"
    val cssQuery = "cssQuery"
  }
}
