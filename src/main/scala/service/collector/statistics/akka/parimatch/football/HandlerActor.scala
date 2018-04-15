package service.collector.statistics.akka.parimatch.football

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import org.mongodb.scala.BulkWriteResult
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage
import service.collector.statistics.akka.parimatch.Builder
import service.logging.Logger
import service.mongo.DBLayer
import service.mongo.observer.CommonObserver

import scala.concurrent.duration._
import scala.language.postfixOps

class HandlerActor extends Actor with Logger {

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error("Something went wrong during the execution of the actor.",
                     x)
        Resume
      }
    }

  override def receive: Receive = {
    case HtmlElementsMessage(elements) =>
      elements.map(el => Builder.elementToEntity(el)).flatten match {
        case elements @ Vector(_, _*) =>
          DBLayer.sportEventDAO_Football
            .updateResults(elements)
            .subscribe(new CommonObserver[BulkWriteResult])
        case _ => logger.warn("No items!!!")
      }

  }
}

object HandlerActor {
  val name = "FootballPostEventStatisticsHandler"
}
