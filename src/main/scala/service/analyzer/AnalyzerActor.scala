package service.analyzer

import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import akka.http.scaladsl.model.Uri
import service.analyzer.model.DataMessage
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by serge on 13.12.2017.
  */
class AnalyzerActor extends Actor with Logger {

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error(s"Exception - $x")
        Resume
      }
    }

  override def receive: Receive = {
    case DataMessage(uri, data) => process(uri, data)
    case um                     => logger.error(s"Unhandled message!!! $um")
  }

  protected def process(uri: Uri, data: String) = {
    logger.info(s"Received message. Uri - $uri")
    SiteAnalyzer(data).analyze
  }
}
