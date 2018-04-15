package service.analyzer

import _root_.model.Page
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
        logger.error("Something went wrong during the execution of the actor.",
                     x)
        Resume
      }
    }

  override def receive: Receive = {
    case DataMessage(uri, data, p) => process(uri, data, p)
    case um                        => logger.error(s"Unhandled message!!! $um")
  }

  protected def process(uri: Uri, data: String, p: Page) = {
    logger.info(s"Received message. Uri - $uri")
    SiteAnalyzer(data, p.cssQuery, p.module).analyze match {
      case Left(err) => logger.error(s"Uri - $uri, Page - $p, Error - $err.")
      case _         =>
    }
  }
}

object AnalyzerActor {
  val name = "analyzerActor"
}
