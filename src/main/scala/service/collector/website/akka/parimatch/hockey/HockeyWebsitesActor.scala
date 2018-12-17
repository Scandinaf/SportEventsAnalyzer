package service.collector.website.akka.parimatch.hockey

import akka.actor.{ActorRef, Props}
import service.Config.{Parimatch => config}
import service.collector.website.akka.parimatch.WebsitesActor

class HockeyWebsitesActor(protected val actorFSM: ActorRef)
    extends WebsitesActor {
  protected val sport = "hockey"
  protected val cssQuery = config.hockeyConfig.infSourceCssQuery
  protected val child =
    context.actorOf(HockeyUrlHandlerActor
                      .props(actorFSM)
                      .withDispatcher("fork-join-dispatcher"),
                    HockeyUrlHandlerActor.name)
}

object HockeyWebsitesActor {
  val name = "HockeyWebsitesActor_parimatch"
  def props(actor: ActorRef): Props =
    Props(classOf[HockeyWebsitesActor], actor)
}
