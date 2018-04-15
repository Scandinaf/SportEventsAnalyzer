package service.http.handler

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import model.ApplicationSettings.Actor.lbActor
import service.akka.lb.LoadBalancerActor.Message.HtmlContentBalancing
import service.logging.Logger
import model.ApplicationSettings.Actor.forkJoinEC

class PostEventStatisticsHandler
    extends BaseHttpHandler[Unit, (String, ActorRef)]
    with Logger {
  override def handler(req: HttpRequest,
                       res: HttpResponse,
                       v: (String, ActorRef)): Unit =
    res match {
      case HttpResponse(StatusCodes.OK, _, entity, _) =>
        getData(entity).map(lbActor ! HtmlContentBalancing(_, v._1, v._2))
      case _ => logger.error(getErrorMessage(req, res))
    }
}

object PostEventStatisticsHandler {
  def apply(): PostEventStatisticsHandler = new PostEventStatisticsHandler()
}
