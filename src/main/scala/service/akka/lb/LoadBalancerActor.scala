package service.akka.lb

import akka.actor.ActorRef
import net.ruippeixotog.scalascraper.model.Element
import service.akka.ActorTemplate
import service.akka.lb.LoadBalancerActor.Message.HtmlContentBalancing

class LoadBalancerActor extends ActorTemplate {
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
