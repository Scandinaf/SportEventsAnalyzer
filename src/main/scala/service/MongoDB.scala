package service

/**
  * Created by serge on 07.12.2017.
  */
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCredential, ServerAddress}

import scala.collection.JavaConverters._

object MongoDB {

  private val mongoSettings = Config.mongoDBSettings
  private val mongoClient = initializeMongoClient
  private val db = mongoClient.getDatabase(mongoSettings.db)

  def getCollection(collectionName: String) = db.getCollection(collectionName)

  def getListDatabase =
    mongoClient.listDatabaseNames().toFuture()

  private def initializeMongoClient =
    MongoClient(getMongoClientSettings)

  private def getMongoClientSettings =
    MongoClientSettings
      .builder()
      .credentialList(List(getCredentialSettings).asJava)
      .clusterSettings(getClusterSettings)
      .build()

  private def getCredentialSettings =
    MongoCredential.createCredential(mongoSettings.userName,
                                     mongoSettings.db,
                                     mongoSettings.password.toCharArray)

  private def getClusterSettings =
    ClusterSettings
      .builder()
      .hosts(List(new ServerAddress(
        s"${mongoSettings.url}:${mongoSettings.port}")).asJava)
      .build()
}
