package service.analyzer

import java.util.concurrent.ForkJoinPool

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import service.logging.Logger

import scala.concurrent.ExecutionContext

/**
  * Created by serge on 19.12.2017.
  */
protected[analyzer] class SiteAnalyzer(content: String) extends Logger {
  private val doc = JsoupBrowser().parseString(content)
  private val parallelism = 5
  implicit val ec: ExecutionContext =
    ExecutionContext.fromExecutor(new ForkJoinPool(parallelism))

  lazy val analyze = {
    TagAnalyzer(doc).tag.map(r => {
      logger.info(s"KeyWords - $r")
      println(r)
    })
  }
}

object SiteAnalyzer {
  def apply(content: String): SiteAnalyzer = new SiteAnalyzer(content)
}
