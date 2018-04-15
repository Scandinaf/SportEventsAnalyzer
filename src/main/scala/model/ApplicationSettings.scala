package model

import akka.actor.{ActorSystem, Props}
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import service.akka.lb.LoadBalancerActor
import service.analyzer.AnalyzerActor
import service.collector.statistics.akka.PostEventStatisticsActor

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
      AnalyzerActor.name)
    val postEventStatisticsActor = system.actorOf(
      Props[PostEventStatisticsActor].withDispatcher("fork-join-dispatcher"),
      PostEventStatisticsActor.name)
    val lbActor = system.actorOf(SmallestMailboxPool(5)
                                   .props(Props[LoadBalancerActor])
                                   .withDispatcher("fork-join-dispatcher"),
                                 LoadBalancerActor.name)
  }
  val timeout = 1 minute
}
