package service.collector.website.akka.parimatch

import net.ruippeixotog.scalascraper.model.Element
import service.logging.Logger
import service.mongo.model.Website

object Builder extends Logger {
  private val tagNameA = "a"
  private val attributeHref = "href"

  def elementToEntity(el: Element,
                      cssQuery: String,
                      module: String,
                      tag: String,
                      baseUrl: String): Option[Website] =
    el.tagName match {
      case `tagNameA` =>
        Some(
          Website(url = buildUrl(baseUrl, el.attr(attributeHref)),
                  module = module,
                  cssQuery = cssQuery,
                  tag = tag))
      case tN =>
        logger.warn(
          s"Couldn't find the handler for TagName - $tN. Element - $el.")
        None
    }

  private def buildUrl(baseUrl: String, additionalPart: String) =
    s"$baseUrl$additionalPart"
}
