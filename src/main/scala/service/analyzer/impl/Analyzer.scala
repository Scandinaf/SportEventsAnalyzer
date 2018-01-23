package service.analyzer.impl

import net.ruippeixotog.scalascraper.browser.Browser

/**
  * Created by serge on 22.01.2018.
  */
private[impl] trait Analyzer {
  private[impl] val keyWords: List[String]
  private[impl] val elementName: String

  def analyzeContent(doc: Browser#DocumentType)
}
