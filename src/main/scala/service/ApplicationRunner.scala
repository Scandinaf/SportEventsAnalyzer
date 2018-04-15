package service

import _root_.akka.http.scaladsl.model.HttpRequest
import model.{ApplicationSettings, Page, Website}
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.http.HttpClient
import service.http.handler.WebsiteHandler
import service.http.model.HttpRequestCompanion
import service.logging.Logger

/**
  * Created by serge on 13.12.2017.
  */
class ApplicationRunner extends Logger {
  def start = {
    pullHttpRequests(createHttpRequests(Config.websites))
    ApplicationSettings.Actor.postEventStatisticsActor ! CollectStatistics
  }

  private def createHttpRequests(websites: Vector[Website]) =
    HttpRequestCompanion(websites)

  private def pullHttpRequests(requests: Vector[(HttpRequest, Page)]) =
    HttpClient.Get.callByStreamWithHandler(requests.iterator)(
      WebsiteHandler().handler)
}

object ApplicationRunner {
  def apply(): ApplicationRunner = new ApplicationRunner()
}
