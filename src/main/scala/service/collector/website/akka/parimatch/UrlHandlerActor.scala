package service.collector.website.akka.parimatch

import akka.actor.ActorRef
import net.ruippeixotog.scalascraper.model.Element
import org.mongodb.scala.Completed
import service.Config.{Parimatch => config}
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor.Message.PartWorkPerformed
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.utils.ExcludeSupervisor
import service.mongo.DBLayer
import service.mongo.model.Website
import service.mongo.observer.CommonObserver

trait UrlHandlerActor extends ActorTemplate with ExcludeSupervisor {
  protected val baseUrl = config.baseUrl
  protected val actorRef: ActorRef
  protected val cssQuery: String
  protected val module: String
  protected val tag: String

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

  protected def handler(el: Element) =
    if (necessaryExclude(el.text)) None
    else
      Builder.elementToEntity(el, cssQuery, module, tag, baseUrl)

  private def saveWebsites(elements: Vector[Website]) =
    DBLayer.websiteDAO.insert(elements).subscribe(new CommonObserver[Completed])
}
