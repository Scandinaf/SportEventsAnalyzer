package model

import com.typesafe.config.Config

case class TeamSportConfig(module: String,
                           cssQuery: String,
                           infSourceCssQuery: String)
object TeamSportConfig {
  def apply(config: Config): TeamSportConfig =
    new TeamSportConfig(config.getString(Config.module),
                        config.getString(Config.cssQuery),
                        config
                          .getConfig(Config.informationSourceBN)
                          .getString(Config.cssQuery))

  object Config {
    val module = "module"
    val cssQuery = "cssQuery"
    val informationSourceBN = "informationSource"
  }
}
