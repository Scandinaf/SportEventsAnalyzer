package service.collector.website.akka.parimatch.football

import akka.actor.{ActorRef, Props}
import service.Config.{Parimatch => config}
import service.collector.website.akka.parimatch.WebsitesActor

class FootballWebsitesActor(protected val actorFSM: ActorRef)
    extends WebsitesActor {
  protected val sport = "football"
  protected val cssQuery = config.footballConfig.infSourceCssQuery
  protected val child =
    context.actorOf(
      FootballUrlHandlerActor.props(actorFSM).withDispatcher("fork-join-dispatcher"),
      FootballUrlHandlerActor.name)
}

object FootballWebsitesActor {
  val name = "FootballWebsitesActor_parimatch"
  def props(actor: ActorRef): Props =
    Props(classOf[FootballWebsitesActor], actor)
}
