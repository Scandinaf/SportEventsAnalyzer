package model

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import service.analyzer.AnalyzerActor

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by serge on 13.12.2017.
  */
object ApplicationSettings {
  object Actor {
    implicit val system = ActorSystem("WebsiteAnalyzer")
    implicit val materializer = ActorMaterializer()
    implicit val forkJoinEC: ExecutionContext =
      system.dispatchers.lookup("fork-join-dispatcher-common")
    val analyzerActor = system.actorOf(
      Props[AnalyzerActor].withDispatcher("fork-join-dispatcher"),
      "analyzerActor")
  }
  val timeout = 1 minute
}
