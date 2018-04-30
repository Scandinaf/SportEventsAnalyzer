package service.analyzer

import _root_.model.ApplicationSettings.Actor.forkJoinEC
import _root_.model.Page
import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.HttpRequest
import akka.routing.SmallestMailboxPool
import org.mongodb.scala.Completed
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor.Message.ProcessCompleted
import service.analyzer.RoutingAnalyzerActor.Message.{StartAnalyze, WebsitesVector}
import service.collector.website.akka.WebsiteActor
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.http.HttpClient
import service.http.handler.WebsiteHandler
import service.http.model.HttpRequestCompanion
import service.mongo.DBLayer
import service.mongo.model.Website
import service.mongo.observer.CommonObserver

import scala.util.{Failure, Success}

class RoutingAnalyzerActor extends ActorTemplate {
  protected val analyzerActor = context.actorOf(
    SmallestMailboxPool(5)
      .props(Props[AnalyzerActor])
      .withDispatcher("fork-join-dispatcher"),
    AnalyzerActor.name)

  override def receive: Receive = {
    case StartAnalyze                                   => getWebsites
    case WebsitesVector(elements) if (elements.isEmpty) => collectInformation
    case WebsitesVector(elements)                       => analyzeInformation(elements)
    case ProcessCompleted =>
      self ! StartAnalyze
      context.child(WebsiteActor.name).map(context.stop(_))
  }

  private def getWebsites =
    DBLayer.websiteDAO.getAllNotExpired.onComplete {
      case Success(elements) => self ! WebsitesVector(elements.toVector)
      case Failure(exception) =>
        logger.error("Couldn't retrieve data.", exception)
    }

  private def collectInformation = {
    logger.info("Information has expired or is missing.")
    DBLayer.websiteDAO.drop.subscribe(new CommonObserver[Completed])
    initializeWebsiteActor ! CollectInformation
  }

  private def initializeWebsiteActor =
    context.actorOf(Props[WebsiteActor].withDispatcher("fork-join-dispatcher"),
                    WebsiteActor.name)

  private def analyzeInformation(v: Vector[Website]) =
    pullHttpRequests(createHttpRequests(v))

  private def createHttpRequests(websites: Vector[Website]) =
    HttpRequestCompanion.buildHttpRequests(websites, analyzerActor)

  private def pullHttpRequests(
      requests: Vector[(HttpRequest, (Page, ActorRef))]) =
    HttpClient.Get.callByStreamWithHandler(requests.iterator)(
      WebsiteHandler().handler)
}

object RoutingAnalyzerActor {
  val name = "RoutingAnalyzerActor"
  object Message {
    case object StartAnalyze
    case class WebsitesVector(elements: Vector[Website])
  }
}
