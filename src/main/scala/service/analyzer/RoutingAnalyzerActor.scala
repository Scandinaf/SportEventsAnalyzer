package service.analyzer

import _root_.model.ApplicationSettings.Actor.forkJoinEC
import _root_.model.Page
import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.HttpRequest
import akka.routing.SmallestMailboxPool
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor.Message.ProcessCompleted
import service.analyzer.RoutingAnalyzerActor.Message.{GetWebsites, StartAnalyze, WebsitesVector}
import service.collector.website.akka.WebsiteActor
import service.collector.website.akka.WebsiteActor.Message.CollectInformation
import service.http.HttpClient
import service.http.handler.WebsiteHandler
import service.http.model.HttpRequestCompanion
import service.mongo.DBLayer
import service.mongo.model.Website

import scala.util.{Failure, Success}

class RoutingAnalyzerActor extends ActorTemplate {
  protected val analyzerActor = context.actorOf(
    SmallestMailboxPool(5)
      .props(Props[AnalyzerActor])
      .withDispatcher("fork-join-dispatcher"),
    AnalyzerActor.name)

  override def receive: Receive = {
    case StartAnalyze             => collectInformation
    case GetWebsites              => getWebsites
    case WebsitesVector(elements) => analyzeInformation(elements)
    case ProcessCompleted =>
      self ! GetWebsites
      context.child(WebsiteActor.name).map(context.stop(_))
  }

  private def getWebsites =
    DBLayer.websiteDAO.getAll.onComplete {
      case Success(elements) => self ! WebsitesVector(elements.toVector)
      case Failure(exception) =>
        logger.error("Couldn't retrieve data.", exception)
    }

  private def collectInformation =
    DBLayer.websiteDAO.drop.onComplete {
      case Success(_) => initializeWebsiteActor ! CollectInformation
      case Failure(exception) =>
        logger.error("Couldn't drop the collection 'websites'.", exception)
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
    case object GetWebsites
    case class WebsitesVector(elements: Vector[Website])
  }
}
