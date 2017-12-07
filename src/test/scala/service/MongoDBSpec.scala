package service

import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by serge on 07.12.2017.
  */
class MongoDBSpec extends FlatSpec {

  behavior of "MongoDB"

  it should "correctly initialize the connection" in {
    val databaseList = Await.result(MongoDB.getListDatabase, 1 minute)
    assert(!databaseList.isEmpty)
  }
}
