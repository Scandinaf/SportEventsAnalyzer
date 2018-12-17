package service.collector.statistics.akka.parimatch.football

import akka.actor.Props
import akka.routing.RoundRobinPool
import service.collector.statistics.akka.parimatch.PostEventStatisticsActor

protected[akka] class PostEventStatisticsFootballActor
    extends PostEventStatisticsActor {
  protected val child = context.actorOf(
    RoundRobinPool(5)
      .props(Props[HandlerActor])
      .withDispatcher("fork-join-dispatcher"),
    HandlerActor.name)
  protected val sport_key: String = "21"
}

object PostEventStatisticsFootballActor {
  val name = "PostEventStatisticsFootballActor"
}
