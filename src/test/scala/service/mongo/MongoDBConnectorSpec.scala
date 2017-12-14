package service.mongo

import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by serge on 07.12.2017.
  */
class MongoDBConnectorSpec extends FlatSpec {

  behavior of "MongoDBConnector"

  it should "correctly initialize the connection" in {
    val databaseList = Await.result(MongoDBConnector.getListDatabase, 1 minute)
    assert(!databaseList.isEmpty)
  }
}
