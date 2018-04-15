package service.collector.statistics.akka

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import akka.http.scaladsl.model.HttpRequest
import akka.routing.RoundRobinPool
import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import service.Config
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.collector.statistics.akka.parimatch.football.HandlerActor
import service.http.HttpClient
import service.http.handler.PostEventStatisticsHandler
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

protected[akka] class PostEventStatisticsFootballActor
    extends Actor
    with Logger {
  protected val dtf = DateTimeFormat.forPattern("yyyyMMdd")
  protected val baseUrl = Config.postEventStatisticsParimatchSettings.baseUrl
  protected val cssQuery = Config.postEventStatisticsParimatchSettings.cssQuery
  protected val child = context.actorOf(
    RoundRobinPool(5)
      .props(Props[HandlerActor])
      .withDispatcher("fork-join-dispatcher"),
    HandlerActor.name)

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

  private def process = {
    logger.info(
      "The process of collecting post-match football statistics began!!!")
    HttpClient.Get
      .singleCall(HttpRequest(uri = getUrl),
                  (cssQuery, child),
                  PostEventStatisticsHandler().handler)
  }

  private def getUrl = {
    val currentDate = DateTime.yesterday.toString(dtf)
    s"$baseUrl$currentDate&SK=21"
  }
}

object PostEventStatisticsFootballActor {
  val name = "PostEventStatisticsFootballActor"
}
