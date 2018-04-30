package service.collector.website.akka.parimatch.football

import java.util.Date

import akka.actor.{ActorRef, Props}
import net.ruippeixotog.scalascraper.model.Element
import org.mongodb.scala.Completed
import service.akka.ActorTemplate
import service.akka.combiner.ProcessDoneActor.Message.PartWorkPerformed
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.utils.ExcludeSupervisor
import service.collector.website.akka.parimatch.Builder
import service.mongo.DBLayer
import service.mongo.model.Website
import service.mongo.observer.CommonObserver
import service.utils.TimeDateUtil

class HandlerActor(actorRef: ActorRef)
    extends ActorTemplate
    with ExcludeSupervisor {
  override protected val phrases: Vector[String] =
    Vector("Сравнение команд по индивидуальному тоталу",
           "Специальные предложения")
  val cssQuery =
    "div#oddsList > form#f1 > div.container.gray > div.wrapper > table.dt.twp > tbody:not(.props,.spacer) > tr"
  val baseUrl = "https://www.parimatch.by"
  val module = "parimatch.TeamSports"
  val tag = Website.Tag.football

  override def receive: Receive = {
    case HtmlElementsMessage(elements) =>
      val expirationDate = TimeDateUtil.getFutureDate()
      elements
        .map(handler(_, expirationDate))
        .flatten match {
        case elements @ Vector(_, _*) =>
          saveWebsites(elements)
          actorRef ! PartWorkPerformed(
            s"Tag - $tag. Module - $module. BaseUrl - $baseUrl")
        case _ => logger.warn("No items!!!")
      }
  }

  private def handler(el: Element, expirationDate: Date) =
    if (necessaryExclude(el.text)) None
    else
      Builder.elementToEntity(el,
                              cssQuery,
                              module,
                              tag,
                              baseUrl,
                              expirationDate)

  private def saveWebsites(elements: Vector[Website]) =
    DBLayer.websiteDAO.insert(elements).subscribe(new CommonObserver[Completed])
}

object HandlerActor {
  val name = "FootballWebsitesHandlerActor"
  def props(actor: ActorRef): Props = Props(classOf[HandlerActor], actor)
}
