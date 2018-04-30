package service.analyzer

import _root_.model.Page
import akka.http.scaladsl.model.Uri
import service.akka.ActorTemplate
import service.analyzer.model.DataMessage

/**
  * Created by serge on 13.12.2017.
  */
class AnalyzerActor extends ActorTemplate {
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
  val name = "AnalyzerActor"
}
