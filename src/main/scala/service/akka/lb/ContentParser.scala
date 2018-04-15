package service.akka.lb

import akka.actor.ActorRef
import akka.stream.scaladsl.{Sink, Source}
import model.ApplicationSettings.Actor.materializer
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}
import service.akka.lb.LoadBalancerActor.Message.HtmlElementsMessage

class ContentParser(html: String, cssQuery: String, actor: ActorRef) {
  private val doc = JsoupBrowser().parseString(html)

  def handleAndSent = splitByChunks(doc >> cssQuery)

  private def splitByChunks(elements: ElementQuery[Element],
                            chunkSize: Int = 100) = {
    Source(elements.toStream)
      .grouped(chunkSize)
      .map(elements => actor ! HtmlElementsMessage(elements.toVector))
      .runWith(Sink.ignore)
  }
}

object ContentParser {
  def apply(html: String, cssQuery: String, actor: ActorRef): ContentParser =
    new ContentParser(html, cssQuery, actor)
}
