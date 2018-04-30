package service

/**
  * Created by serge on 05.12.2017.
  */
import org.scalatest.FlatSpec

class ConfigSpec extends FlatSpec {

  behavior of "Config"

  "Attempt to read the MongoDB block" should "not throw exception" in {
    Config.mongoDBSettings
  }
}
