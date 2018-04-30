package service.collector.statistics.akka

import akka.actor.Props
import service.akka.ActorTemplate
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.collector.statistics.akka.PostEventStatisticsActor.name

class PostEventStatisticsActor extends ActorTemplate {
  override def preStart(): Unit = initializeChildActors

  override def receive: Receive = {
    case CollectStatistics => process
    case um                => logger.error(s"Unhandled message!!! $um")
  }

  private def initializeChildActors = {
    context.actorOf(Props[PostEventStatisticsFootballActor].withDispatcher(
                      "fork-join-dispatcher"),
                    PostEventStatisticsFootballActor.name)
  }

  private def process = {
    logger.info("The process of collecting post-match statistics began!!!")
    context.actorSelection(s"/user/$name/*") ! CollectStatistics
  }
}

object PostEventStatisticsActor {
  val name = "PostEventStatisticsActor"
  object Message {
    object CollectStatistics
  }
}
