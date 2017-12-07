package model

import com.typesafe.config.Config

/**
  * Created by serge on 07.12.2017.
  */
case class MongoDBSettings(db: String,
                           userName: String,
                           password: String,
                           url: String,
                           port: Int)

object MongoDBSettings {
  def apply(config: Config): MongoDBSettings =
    new MongoDBSettings(
      config.getString(Config.db),
      config.getString(Config.userName),
      config.getString(Config.password),
      config.getString(Config.url),
      config.getInt(Config.port)
    )

  object Config {
    val blockName = "mongoDB"
    val db = "db"
    val userName = "userName"
    val password = "password"
    val url = "url"
    val port = "port"
  }
}
