package service.analyzer

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import service.analyzer.module.factory.ModuleFactory

/**
  * Created by serge on 19.12.2017.
  */
protected[analyzer] class SiteAnalyzer(content: String,
                                       cssQuery: String,
                                       moduleName: String) {
  private val doc = JsoupBrowser().parseString(content)
  lazy val analyze = ModuleFactory
    .getModule(moduleName)
    .flatMap(_.process(doc >> cssQuery))
}

object SiteAnalyzer {
  def apply(content: String,
            cssQuery: String,
            moduleName: String): SiteAnalyzer =
    new SiteAnalyzer(content, cssQuery, moduleName)
}
