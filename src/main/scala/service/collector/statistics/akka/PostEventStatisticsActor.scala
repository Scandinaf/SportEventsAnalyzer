package service.collector.statistics.akka

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.collector.statistics.akka.PostEventStatisticsActor.name
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

class PostEventStatisticsActor extends Actor with Logger {

  override def preStart(): Unit = initializeChildActors

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error("Something went wrong during the execution of the actor.",
                     x)
        Resume
      }
    }

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
  val name = "postEventStatisticsActor"
  object Message {
    object CollectStatistics
  }
}
