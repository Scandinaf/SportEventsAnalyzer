package service.analyzer.module.builder

import java.util.Calendar

import com.github.nscala_time.time.Imports.DateTime
import net.ruippeixotog.scalascraper.model.Element
import org.joda.time.format.DateTimeFormatter
import service.analyzer.exception.KeyNotFoundException

import scala.util.{Failure, Success, Try}

/**
  * Created by serge on 08.03.2018.
  */
protected[module] trait ModelBuilder[T] {
  protected val dtf: DateTimeFormatter
  def build(textPosMap: Map[String, Int], element: Element): Try[T]

  protected def getDate(text: String) = {
    val c = Calendar.getInstance()
    val currentMonth = c.get(Calendar.MONTH)
    val currentYear = c.get(Calendar.YEAR)
    c.setTime(DateTime.parse(text, dtf).toDate)
    c.set(Calendar.YEAR, currentYear)
    if (currentMonth > c.get(Calendar.MONTH))
      c.add(Calendar.YEAR, 1)
    c.getTime
  }

  protected def parseTextToDouble(text: String)(implicit defV: Double) =
    text match {
      case "" => defV
      case _  => tryParseTextToDouble(text, defV)
    }

  private def tryParseTextToDouble(text: String, defV: Double) =
    Try(
      BigDecimal
        .apply(text)
        .setScale(2, BigDecimal.RoundingMode.HALF_UP)
        .toDouble) match {
      case Success(convertedV) => convertedV
      case Failure(_)          => defV
    }

  protected def getEventName(t: (String, String)) =
    s"${t._1} - ${t._2}"

  protected def getIndex(key: String, textPosMap: Map[String, Int]) =
    textPosMap.get(key) match {
      case Some(v) => v
      case None    => throw new KeyNotFoundException(s"Key: $key")
    }

  protected def getElement(index: Int, children: Vector[Element]) =
    children.apply(index)

  protected def getElementText(index: Int, children: Vector[Element]) =
    children.apply(index).text
}
