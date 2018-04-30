package service.collector.statistics.akka

import akka.actor.Props
import akka.http.scaladsl.model.HttpRequest
import akka.routing.RoundRobinPool
import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import service.Config.{Parimatch => config}
import service.akka.ActorTemplate
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.collector.statistics.akka.parimatch.football.HandlerActor
import service.http.HttpClient
import service.http.handler.LoadBalancerHandler

protected[akka] class PostEventStatisticsFootballActor extends ActorTemplate {
  protected val dtf = DateTimeFormat.forPattern("yyyyMMdd")
  protected val url =
    s"${config.baseUrl}${config.postEventStatisticsSettings.path}"
  protected val cssQuery = config.postEventStatisticsSettings.cssQuery
  protected val child = context.actorOf(
    RoundRobinPool(5)
      .props(Props[HandlerActor])
      .withDispatcher("fork-join-dispatcher"),
    HandlerActor.name)

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
                  LoadBalancerHandler().handler)
  }

  private def getUrl = {
    val currentDate = DateTime.yesterday.toString(dtf)
    s"$url$currentDate&SK=21"
  }
}

object PostEventStatisticsFootballActor {
  val name = "PostEventStatisticsFootballActor"
}
