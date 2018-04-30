package service.collector.statistics.akka.parimatch.football

import org.mongodb.scala.BulkWriteResult
import service.akka.ActorTemplate
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.statistics.akka.parimatch.Builder
import service.collector.utils.AliasSupervisor
import service.mongo.DBLayer
import service.mongo.observer.CommonObserver

class HandlerActor extends ActorTemplate with AliasSupervisor {

  override protected val aliases: Map[String, String] = Map(
    "Зенит" -> "Зенит Ст.Петербург")

  override def receive: Receive = {
    case HtmlElementsMessage(elements) =>
      elements.map(el => Builder.elementToEntity(el)).flatten match {
        case elements @ Vector(_, _*) =>
          DBLayer.sportEventDAO_Football
            .updateResults(elements.map(replaceAlias))
            .subscribe(new CommonObserver[BulkWriteResult])
        case _ => logger.warn("No items!!!")
      }
  }
}

object HandlerActor {
  val name = "FootballPostEventStatisticsHandler"
}
