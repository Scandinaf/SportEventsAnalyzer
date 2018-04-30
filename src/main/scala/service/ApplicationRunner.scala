package service

import model.ApplicationSettings
import service.analyzer.RoutingAnalyzerActor.Message.StartAnalyze
import service.collector.statistics.akka.PostEventStatisticsActor.Message.CollectStatistics
import service.logging.Logger

/**
  * Created by serge on 13.12.2017.
  */
class ApplicationRunner extends Logger {
  def start = {
    ApplicationSettings.Actor.postEventStatisticsActor ! CollectStatistics
    ApplicationSettings.Actor.routingAnalyzerActor ! StartAnalyze
  }
}

object ApplicationRunner {
  def apply(): ApplicationRunner = new ApplicationRunner()
}
