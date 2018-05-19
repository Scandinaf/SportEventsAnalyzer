package service.http.model

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpRequest
import model.Page
import service.logging.Logger
import service.mongo.model.Website

import scala.util.{Failure, Success, Try}

/**
  * Created by serge on 13.12.2017.
  */
object HttpRequestCompanion extends Logger {
  def buildHttpRequests(websites: Vector[Website], actor: ActorRef) =
    websites
      .map(el => handler(createHttpRequest(el.url), el, actor))
      .flatten

  private def handler(request: Try[HttpRequest],
                      s: Website,
                      actorRef: ActorRef) =
    request match {
      case Success(req) =>
        Some((req, (Page(s.url, s.module, s.cssQuery), actorRef)))
      case Failure(ex) =>
        logger.error("Problems with the website url.", ex)
        None
    }

  private def createHttpRequest(url: String): Try[HttpRequest] =
    Try(HttpRequest(uri = url))
}
