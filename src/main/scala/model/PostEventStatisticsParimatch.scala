package model

import com.typesafe.config.Config

case class PostEventStatisticsParimatch(baseUrl: String, cssQuery: String)

object PostEventStatisticsParimatch {

  def apply(config: Config): PostEventStatisticsParimatch =
    new PostEventStatisticsParimatch(config.getString(Config.baseUrl),
                                     config.getString(Config.cssQuery))

  object Config {
    val blockName = "postEventStatisticsParimatch"
    val baseUrl = "baseUrl"
    val cssQuery = "cssQuery"
  }
}
