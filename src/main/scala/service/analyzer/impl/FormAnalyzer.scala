package service.analyzer.impl
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import service.mongo.model.AnalyzedWebsite

/**
  * Created by serge on 22.01.2018.
  */
class FormAnalyzer(override private[impl] val keyWords: List[String])
    extends Analyzer {
  override private[impl] val elementName = "form"

  override def analyzeContent(doc: Browser#DocumentType) =
    analyzeHtmlElement((doc >> elementList(elementName)).find(el =>
      elementContainsWords(getWords(el))))

  private def getWords(element: Element) =
    (element >> allText).split(" ")

  private def elementContainsWords(words: Array[String]) =
    words.exists(st => keyWords.exists(st == _))

  private def analyzeHtmlElement(
      element: Option[Element]): Option[AnalyzedWebsite] =
    element
      .map(mainEl => {
        mainEl.children
        None
      })
      .getOrElse(None)
}

object FormAnalyzer {
  def apply(keyWords: List[String]): FormAnalyzer = new FormAnalyzer(keyWords)
}
