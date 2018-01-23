package service.analyzer

import _root_.model.KeyWord
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.scalatest.FlatSpec

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by serge on 23.01.2018.
  */
class TagAnalyzerSpec extends FlatSpec {
  val tA = TagAnalyzer(JsoupBrowser().parseString(""))

  behavior of "TagAnalyzer"

  "TagAnalyzer->calculateChance" should "correctly work with empty Map" in {
    assert(tA.calculateChance(Map.empty) == 0)
  }

  "TagAnalyzer->calculateChance" should "correctly calculate chance" in {
    assert(tA.calculateChance(Map(("1", 1), ("2", 2), ("3", 3))) == 6)
  }

  "TagAnalyzer->mapToList" should "correctly work with empty Map" in {
    assert(tA.mapToList(Map.empty).size == 0)
  }

  "TagAnalyzer->mapToList" should "return list with elements in asc order" in {
    val l = tA.mapToList(
      Map(("test", 10), ("first", 1), ("last", 100), ("middle", 5)))
    assert(l.head == "first")
    assert(l.last == "last")
  }

  "TagAnalyzer->calculateTag" should "correctly work with empty List" in {
    val r = tA.calculateTag(KeyWord("test", List("test", "test1", "test2")),
                            List.empty)
    assert(r.isEmpty)
  }

  "TagAnalyzer->calculateTag" should "return map with keyWords" in {
    val r = tA.calculateTag(
      KeyWord("test", List("test", "test1", "test2")),
      List("a", "c", "e", "d", "test1", "asdTest", "tes", "test21"))
    assert(r.size == 1)
    assert(r.contains("test1"))
    assert(r.get("test1").getOrElse(0) == 1)
  }

  "TagAnalyzer->calculateTag" should "return empty map" in {
    val r = tA.calculateTag(
      KeyWord("test", List("test", "test1", "test2")),
      List("a", "c", "e", "d", "tests1", "asdTest", "tes", "test21"))
    assert(r.isEmpty)
  }

  "TagAnalyzer->compareChances" should "correctly comparing chances" in {
    val r = tA.compareChances(Map(("test", 20)), (1, Map.empty))
    assert(r._1 == 20)
    assert(r._2.contains("test"))
    assert(r._2.get("test").getOrElse(0) == 20)
  }
}
