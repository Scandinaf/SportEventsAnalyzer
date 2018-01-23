package model

import com.typesafe.config.Config

import scala.collection.JavaConverters._

/**
  * Created by serge on 22.01.2018.
  */
case class KeyWord(tag: String, words: List[String])

object KeyWord {
  def apply(config: Config): KeyWord =
    new KeyWord(config.getString(Config.tag),
                config.getStringList(Config.words).asScala.toList)

  def apply(tag: String, words: List[String]): KeyWord = new KeyWord(tag, words)

  object Config {
    val blockName = "keyWords"
    val tag = "tag"
    val words = "words"
  }
}
