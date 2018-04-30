package service.collector.website.akka

import model.ApplicationSettings
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor
import service.analyzer.RoutingAnalyzerActor
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.collector.website.akka.WebsiteActor.name
import service.collector.website.akka.parimatch.football.FootballWebsitesActor

class WebsiteActor extends ActorTemplate {
  override def preStart(): Unit = initializeChildActors

  override def receive: Receive = {
    case CollectInformation => process
    case um                 => logger.error(s"Unhandled message!!! $um")
  }

  private def initializeChildActors = {
    val fsmActor = context.system.actorOf(
      ProcessDoneActor
        .props("Gathering information about websites",
               ApplicationSettings.Actor.routingAnalyzerActor)
        .withDispatcher("fork-join-dispatcher"))
    context.actorOf(FootballWebsitesActor
                      .props(fsmActor)
                      .withDispatcher("fork-join-dispatcher"),
                    FootballWebsitesActor.name)
  }

  private def process = {
    logger.info("The process of collecting information about websites began!!!")
    context.actorSelection(s"/user/${RoutingAnalyzerActor.name}/$name/*") ! CollectInformation
  }
}

object WebsiteActor {
  val name = "WebsiteActor"
  object Message {
    object CollectInformation
  }
}
