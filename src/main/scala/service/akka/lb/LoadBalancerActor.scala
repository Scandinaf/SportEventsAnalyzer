package service.akka.lb

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, ActorRef, OneForOneStrategy, SupervisorStrategy}
import net.ruippeixotog.scalascraper.model.Element
import service.akka.lb.LoadBalancerActor.Message.HtmlContentBalancing
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

class LoadBalancerActor extends Actor with Logger {

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error("Something went wrong during the execution of the actor.",
                     x)
        Resume
      }
    }

  override def receive: Receive = {
    case HtmlContentBalancing(html, cssQuery, child) =>
      ContentParser(html, cssQuery, child).handleAndSent
  }
}

object LoadBalancerActor {
  val name = "LoadBalancerActor"
  object Message {
    case class HtmlContentBalancing(html: String,
                                    cssQuery: String,
                                    child: ActorRef)
    case class HtmlElementsMessage(elements: Vector[Element])
  }
}
