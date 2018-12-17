package service.collector.statistics.akka.parimatch

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpRequest
import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import service.Config.{Parimatch => config}
import service.akka.ActorTemplate
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.http.HttpClient
import service.http.handler.LoadBalancerHandler

trait PostEventStatisticsActor extends ActorTemplate {
  protected val dtf = DateTimeFormat.forPattern("yyyyMMdd")
  protected val url =
    s"${config.baseUrl}${config.postEventStatisticsSettings.path}"
  protected val cssQuery = config.postEventStatisticsSettings.cssQuery
  protected val child: ActorRef
  protected val sport_key: String

  override def receive: Receive = {
    case CollectStatistics => process
    case um                => logger.error(s"Unhandled message!!! $um")
  }

  protected def getUrl = {
    val currentDate = DateTime.yesterday.toString(dtf)
    s"$url$currentDate&SK=$sport_key"
  }

  private def process = {
    logger.info(
      s"The process of collecting post-match $sport_key statistics began!!!")
    HttpClient.Get
      .singleCall(HttpRequest(uri = getUrl),
                  (cssQuery, child),
                  LoadBalancerHandler().handler)
  }
}
