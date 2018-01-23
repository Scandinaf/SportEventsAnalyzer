package service

/**
  * Created by serge on 05.12.2017.
  */
import org.scalatest.FlatSpec

class ConfigSpec extends FlatSpec {

  behavior of "Config"

  "Websites block" should "be non-empty" in {
    assert(!Config.websites.isEmpty)
  }

  "KeyWords block" should "be non-empty" in {
    assert(!Config.keyWords.isEmpty)
  }

  "Attempt to read the MongoDB block" should "not throw exception" in {
    Config.mongoDBSettings
  }
}
