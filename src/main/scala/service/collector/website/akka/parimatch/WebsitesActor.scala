package service.collector.website.akka.parimatch

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpRequest
import service.Config.{Parimatch => config}
import service.akka.ActorTemplate
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.http.HttpClient
import service.http.handler.LoadBalancerHandler

trait WebsitesActor extends ActorTemplate {
  protected val baseUrl = config.baseUrl
  protected val sport: String
  protected val actorFSM: ActorRef
  protected val cssQuery: String
  protected val child: ActorRef

  override def receive: Receive = {
    case CollectInformation => process
    case um                 => logger.error(s"Unhandled message!!! $um")
  }

  private def process = {
    logger.info(
      s"The process of collecting information about a parimatch $sport websites began!!!")
    HttpClient.Get
      .singleCall(HttpRequest(uri = baseUrl),
                  (cssQuery, child),
                  LoadBalancerHandler().handler)
  }
}
