package service.collector.website.akka.parimatch.football

import akka.actor.{ActorRef, Props}
import service.Config.{Parimatch => config}
import service.collector.website.akka.parimatch.UrlHandlerActor
import service.mongo.model.Website

class FootballUrlHandlerActor(protected val actorRef: ActorRef)
    extends UrlHandlerActor {
  override protected val phrases: Vector[String] =
    Vector("Сравнение команд по индивидуальному тоталу",
           "Специальные предложения")
  val cssQuery = config.footballConfig.cssQuery
  val module = config.footballConfig.module
  val tag = Website.Tag.football
}

object FootballUrlHandlerActor {
  val name = "FootballUrlHandlerActor"
  def props(actor: ActorRef): Props =
    Props(classOf[FootballUrlHandlerActor], actor)
}
