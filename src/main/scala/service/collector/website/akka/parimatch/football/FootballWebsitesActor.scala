package service.collector.website.akka.parimatch.football

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.HttpRequest
import service.akka.ActorTemplate
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.http.HttpClient
import service.http.handler.LoadBalancerHandler

class FootballWebsitesActor(actor: ActorRef) extends ActorTemplate {
  protected val baseUrl = "https://www.parimatch.by"
  protected val cssQuery =
    "div#lobbySportsHolder > li:nth-child(1) > ul > li > a"
  protected val child =
    context.actorOf(
      HandlerActor.props(actor).withDispatcher("fork-join-dispatcher"),
      HandlerActor.name)

  override def receive: Receive = {
    case CollectInformation => process
    case um                 => logger.error(s"Unhandled message!!! $um")
  }

  private def process = {
    logger.info(
      "The process of collecting information about a parimatch football websites began!!!")
    HttpClient.Get
      .singleCall(HttpRequest(uri = baseUrl),
                  (cssQuery, child),
                  LoadBalancerHandler().handler)
  }
}

object FootballWebsitesActor {
  val name = "FootballWebsitesActor_parimatch"
  def props(actor: ActorRef): Props =
    Props(classOf[FootballWebsitesActor], actor)
}
