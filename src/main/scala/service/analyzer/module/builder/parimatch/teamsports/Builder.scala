package service.analyzer.module.builder.parimatch.teamsports

import com.github.nscala_time.time.Imports.DateTimeFormat
import net.ruippeixotog.scalascraper.model.{Element, TextNode}
import service.analyzer.exception.IncorrectHtmlException
import service.analyzer.module.builder.ModelBuilder
import service.analyzer.module.builder.parimatch.Config.TeamSports._
import service.mongo.model.{Bet, SportEvent}
import service.utils.StringHelper.checkAndExcludeBracket

import scala.util.Try

/**
  * Created by serge on 08.03.2018.
  */
protected[module] trait Builder extends ModelBuilder[SportEvent] {
  protected val dtf = DateTimeFormat.forPattern("dd/MM HH:mm")
  private implicit val defaultBet = 1.0

  override def build(textPosMap: Map[String, Int],
                     element: Element): Try[SportEvent] =
    Try(buildSE(element.children.toVector, textPosMap))

  protected def buildSE(children: Vector[Element],
                        textPosMap: Map[String, Int]) = {
    val event_ = getEvent(getElement(getIndex(event, textPosMap), children))
    SportEvent(
      event = getEventName(event_),
      firstTeam = event_._1,
      secondTeam = event_._2,
      date = getDate(getElementText(getIndex(date, textPosMap), children)),
      bet = buildBet(children, textPosMap)
    )
  }

  protected def buildBet(children: Vector[Element],
                         textPosMap: Map[String, Int]) =
    Bet(
      winF =
        parseTextToDouble(getElementText(getIndex(winF, textPosMap), children)),
      draw = Some(
        parseTextToDouble(
          getElementText(getIndex(draw, textPosMap), children))),
      winS =
        parseTextToDouble(getElementText(getIndex(winS, textPosMap), children)),
      winFD = Some(
        parseTextToDouble(
          getElementText(getIndex(winFD, textPosMap), children))),
      winSD = Some(
        parseTextToDouble(
          getElementText(getIndex(winSD, textPosMap), children)))
    )

  protected def getEvent(el: Element) = {
    val aEl = el.select("a")
    if (aEl.size != 1)
      throw new IncorrectHtmlException(el)
    aEl.head.childNodes
      .filter(_.isInstanceOf[TextNode])
      .map(_.asInstanceOf[TextNode].content)
      .toVector match {
      case Vector(fT, sT) =>
        (checkAndExcludeBracket(fT), checkAndExcludeBracket(sT))
      case _ => throw new IncorrectHtmlException(el)
    }
  }
}
