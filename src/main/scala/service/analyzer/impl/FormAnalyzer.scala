package service.analyzer.impl
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

/**
  * Created by serge on 22.01.2018.
  */
class TableAnalyzer(override val tag: String) extends Analyzer {

  val elementName = "table"

  override def analyzeContent(doc: Browser#DocumentType): Unit = {
    val a = doc >> elementList(elementName)
    a.map(l => l.attrs)
  }
}
