package service.http.handler
import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import model.ApplicationSettings.Actor.forkJoinEC
import model.Page
import service.analyzer.model.DataMessage
import service.logging.Logger

/**
  * Created by serge on 12.12.2017.
  */
class WebsiteHandler
    extends BaseHttpHandler[Unit, (Page, ActorRef)]
    with Logger {
  override def handler(req: HttpRequest,
                       res: HttpResponse,
                       p: (Page, ActorRef)): Unit =
    res match {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        getData(entity).map(p._2 ! DataMessage(req.uri, _, p._1))
      case _ => logger.error(getErrorMessage(req, res))
    }
}

object WebsiteHandler {
  def apply(): WebsiteHandler = new WebsiteHandler()
}
