package service.collector.statistics.akka.parimatch.hockey

import org.mongodb.scala.BulkWriteResult
import service.akka.ActorTemplate
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.statistics.akka.parimatch.Builder
import service.mongo.DBLayer
import service.mongo.observer.CommonObserver

class HandlerActor extends ActorTemplate {
  override def receive: Receive = {
    case HtmlElementsMessage(elements) =>
      elements.map(el => Builder.elementToEntity(el)).flatten match {
        case elements @ Vector(_, _*) =>
          DBLayer.sportEventDAO_Hockey
            .updateResults(elements)
            .subscribe(new CommonObserver[BulkWriteResult])
        case _ => logger.warn("No items!!!")
      }
  }
}

object HandlerActor {
  val name = "HockeyPostEventStatisticsHandler"
}
