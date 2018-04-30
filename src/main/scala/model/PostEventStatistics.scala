package model

import com.typesafe.config.Config

case class PostEventStatistics(path: String, cssQuery: String)

object PostEventStatistics {
  def apply(config: Config): PostEventStatistics =
    new PostEventStatistics(config.getString(Config.path),
                            config.getString(Config.cssQuery))

  object Config {
    val blockName = "postEventStatistics"
    val path = "path"
    val cssQuery = "cssQuery"
  }
}
