package service.collector.website.akka.parimatch.football

import akka.actor.{ActorRef, Props}
import net.ruippeixotog.scalascraper.model.Element
import org.mongodb.scala.Completed
import service.Config.{Parimatch => config}
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor.Message.PartWorkPerformed
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.utils.ExcludeSupervisor
import service.collector.website.akka.parimatch.Builder
import service.mongo.DBLayer
import service.mongo.model.Website
import service.mongo.observer.CommonObserver

class HandlerActor(actorRef: ActorRef)
    extends ActorTemplate
    with ExcludeSupervisor {
  override protected val phrases: Vector[String] =
    Vector("Сравнение команд по индивидуальному тоталу",
           "Специальные предложения")
  val cssQuery = config.footballConfig.cssQuery
  val baseUrl = config.baseUrl
  val module = config.footballConfig.module
  val tag = Website.Tag.football

  override def receive: Receive = {
    case HtmlElementsMessage(elements) =>
      elements.map(handler(_)).flatten match {
        case elements @ Vector(_, _*) =>
          saveWebsites(elements)
          actorRef ! PartWorkPerformed(
            s"Tag - $tag. Module - $module. BaseUrl - $baseUrl")
        case _ => logger.warn("No items!!!")
      }
  }

  private def handler(el: Element) =
    if (necessaryExclude(el.text)) None
    else
      Builder.elementToEntity(el, cssQuery, module, tag, baseUrl)

  private def saveWebsites(elements: Vector[Website]) =
    DBLayer.websiteDAO.insert(elements).subscribe(new CommonObserver[Completed])
}

object HandlerActor {
  val name = "FootballWebsitesHandlerActor"
  def props(actor: ActorRef): Props = Props(classOf[HandlerActor], actor)
}
