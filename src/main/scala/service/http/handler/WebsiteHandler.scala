package service.http.handler
import java.nio.charset.StandardCharsets

import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity, StatusCodes}
import model.ApplicationSettings.Actor.{analyzerActor, forkJoinEC, materializer}
import model.ApplicationSettings.timeout
import model.Page
import service.analyzer.model.DataMessage
import service.logging.Logger

/**
  * Created by serge on 12.12.2017.
  */
class WebsiteHandler extends BaseHttpHandler[Unit, Page] with Logger {
  val defaultCharset = StandardCharsets.UTF_8

  override def handler(req: HttpRequest, res: HttpResponse, p: Page): Unit =
    res match {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        entity
          .toStrict(timeout)
          .map(_.data)
          .map(_.decodeString(getCharset(res.entity)))
          .map(analyzerActor ! DataMessage(req.uri, _, p))
      case _ =>
        logger.error(
          s"HttpResponse hasn't processed. Request - $req. Response - $res")
    }

  private def getCharset(entity: ResponseEntity) =
    entity.getContentType().getCharsetOption match {
      case optional if optional.isPresent => optional.get().nioCharset()
      case _                              => defaultCharset
    }
}

object WebsiteHandler {
  def apply(): WebsiteHandler = new WebsiteHandler()
}
