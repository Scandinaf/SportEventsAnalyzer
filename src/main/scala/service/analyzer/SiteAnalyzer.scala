package service.analyzer

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import service.analyzer.module.factory.ModuleFactory
import service.logging.Logger

/**
  * Created by serge on 19.12.2017.
  */
protected[analyzer] class SiteAnalyzer(content: String, cssQuery: String)
    extends Logger {
  private val doc = JsoupBrowser().parseString(content)
  lazy val analyze = ModuleFactory
    .getModule(ModuleFactory.Modules.parimatchTS)
    .fold(err => logger.error(err.msg), m => m.process(doc >> cssQuery))
}

object SiteAnalyzer {
  def apply(content: String, cssQuery: String): SiteAnalyzer =
    new SiteAnalyzer(content, cssQuery)
}
