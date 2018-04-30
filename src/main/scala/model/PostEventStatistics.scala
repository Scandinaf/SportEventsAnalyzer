package model

import com.typesafe.config.Config

case class PostEventStatistics(baseUrl: String, cssQuery: String)

object PostEventStatistics {
  def apply(config: Config): PostEventStatistics =
    new PostEventStatistics(config.getString(Config.baseUrl),
                            config.getString(Config.cssQuery))

  object Config {
    def getBlockName(name: String) = s"postEventStatistics_$name"
    val baseUrl = "baseUrl"
    val cssQuery = "cssQuery"
  }
}
