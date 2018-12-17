package service.collector.statistics.akka.parimatch.hockey

import akka.actor.Props
import akka.routing.RoundRobinPool
import service.collector.statistics.akka.parimatch.PostEventStatisticsActor

class PostEventStatisticsHockeyActor extends PostEventStatisticsActor {
  protected val child = context.actorOf(
    RoundRobinPool(5)
      .props(Props[HandlerActor])
      .withDispatcher("fork-join-dispatcher"),
    HandlerActor.name)
  protected val sport_key: String = "23"
}

object PostEventStatisticsHockeyActor {
  val name = "PostEventStatisticsHockeyActor"
}
