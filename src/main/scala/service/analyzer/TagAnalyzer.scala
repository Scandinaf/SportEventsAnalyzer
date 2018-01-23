package service.analyzer

import _root_.model.KeyWord
import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import service.Config

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by serge on 23.01.2018.
  */
protected[analyzer] class TagAnalyzer(val doc: Browser#DocumentType)(
    implicit val ec: ExecutionContext) {
  private val keyWords = Config.keyWords

  lazy val tag = {
    val words = (doc >> allText).split(" ").toList
    Future
      .sequence(keyWords.map(kw => Future { calculateTag(kw, words) }))
      .map(
        res =>
          mapToList(
            res
              .foldLeft((0, Map.empty[String, Int])) { (r, m) =>
                compareChances(m, r)
              }
              ._2)
      )
  }

  protected[analyzer] def compareChances(v1: Map[String, Int],
                                         v2: (Int, Map[String, Int])) = {
    val chance = calculateChance(v1)
    if (chance > v2._1) (chance, v1)
    else v2
  }

  protected[analyzer] def mapToList(m: Map[String, Int]) =
    m.toList.sortWith(_._2 < _._2).map(_._1)

  protected[analyzer] def calculateChance(m: Map[String, Int]) = m.values.sum

  protected[analyzer] def calculateTag(kw: KeyWord, pageWords: List[String]) =
    pageWords.foldLeft(Map[String, Int]()) { (m, w) =>
      if (kw.words.exists(w == _))
        m + (w -> m.get(w).fold(1)(_ + 1))
      else m
    }
}

object TagAnalyzer {
  def apply(doc: Browser#DocumentType)(
      implicit ec: ExecutionContext): TagAnalyzer = new TagAnalyzer(doc)
}
