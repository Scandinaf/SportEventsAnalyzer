package service.analyzer

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import service.analyzer.module.factory.ModuleFactory

/**
  * Created by serge on 19.12.2017.
  */
protected[analyzer] class SiteAnalyzer(content: String) {
  private val doc = JsoupBrowser().parseString(content)

  def analyze = {
    val elements =
      doc >> "div#oddsList > form#f1 > div.container.gray > div.wrapper > table.dt.twp > tbody:not(.props,.spacer) > tr"
    ModuleFactory
      .getModule(ModuleFactory.Modules.parimatchTS)
      .process(elementQuery = elements)
  }
}

object SiteAnalyzer {
  def apply(content: String): SiteAnalyzer = new SiteAnalyzer(content)
}
