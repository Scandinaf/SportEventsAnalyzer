package service.collector.website.akka.parimatch.hockey

import akka.actor.{ActorRef, Props}
import net.ruippeixotog.scalascraper.model.Element
import service.Config.{Parimatch => config}
import service.collector.website.akka.parimatch.{Builder, UrlHandlerActor}
import service.mongo.model.Website

class HockeyUrlHandlerActor(protected val actorRef: ActorRef)
    extends UrlHandlerActor {
  override protected val cssQuery: String = config.hockeyConfig.cssQuery
  override protected val module: String = config.hockeyConfig.module
  override protected val tag: String = Website.Tag.hockey
  override protected val phrases: Vector[String] = Vector("Итоги", "Статистика")

  override protected def handler(el: Element) =
    if (necessaryExcludePartialComparison(el.text)) None
    else
      Builder.elementToEntity(el, cssQuery, module, tag, baseUrl)
}

object HockeyUrlHandlerActor {
  val name = "HockeyUrlHandlerActor"
  def props(actor: ActorRef): Props =
    Props(classOf[HockeyUrlHandlerActor], actor)
}
