package model

import com.typesafe.config.Config

/**
  * Created by serge on 05.12.2017.
  */
case class Website(protocol: String, domain: String, pagePath: String)

object Website {
  def apply(config: Config): Website =
    new Website(config.getString(Config.protocol),
                config.getString(Config.domain),
                config.getString(Config.pagePath))

  object Config {
    val blockName = "websites"
    val protocol = "protocol"
    val domain = "domain"
    val pagePath = "pagePath"
  }
}
