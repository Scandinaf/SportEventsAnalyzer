package service.collector.website.akka

import model.ApplicationSettings
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.collector.website.akka.parimatch.football.FootballWebsitesActor
import service.collector.website.akka.parimatch.hockey.HockeyWebsitesActor

class WebsiteActor extends ActorTemplate {
  override def preStart(): Unit = initializeChildActors

  override def receive: Receive = {
    case CollectInformation => process
    case um                 => logger.error(s"Unhandled message!!! $um")
  }

  private def initializeChildActors = {
    val fsmActor = context.actorOf(
      ProcessDoneActor
        .props("Gathering information about websites",
               ApplicationSettings.Actor.routingAnalyzerActor)
        .withDispatcher("fork-join-dispatcher"),
      ProcessDoneActor.name
    )
    context.actorOf(FootballWebsitesActor
                      .props(fsmActor)
                      .withDispatcher("fork-join-dispatcher"),
                    FootballWebsitesActor.name)
    context.actorOf(HockeyWebsitesActor
                      .props(fsmActor)
                      .withDispatcher("fork-join-dispatcher"),
                    HockeyWebsitesActor.name)
  }

  private def process = {
    logger.info("The process of collecting information about websites began!!!")
    context.children.foreach(
      actor =>
        if (!actor.path.name.eq(ProcessDoneActor.name))
          actor ! CollectInformation)
  }
}

object WebsiteActor {
  val name = "WebsiteActor"
  object Message {
    object CollectInformation
  }
}
