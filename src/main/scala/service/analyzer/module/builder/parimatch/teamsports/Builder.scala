package service.analyzer.module.builder.parimatch.teamsports

import java.text.SimpleDateFormat

import net.ruippeixotog.scalascraper.model.{Element, TextNode}
import service.analyzer.exception.IncorrectHtmlException
import service.analyzer.module.builder.ModelBuilder
import service.analyzer.module.builder.parimatch.Config.TeamSports._
import service.mongo.model.{Bet, SportEvent}

import scala.util.Try

/**
  * Created by serge on 08.03.2018.
  */
protected[module] trait Builder extends ModelBuilder[SportEvent] {
  protected val dFormat: SimpleDateFormat = new SimpleDateFormat("dd/MM HH:mm")
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
      date = getDate(getElement(getIndex(date, textPosMap), children)),
      bet = buildBet(children, textPosMap)
    )
  }

  protected def buildBet(children: Vector[Element],
                         textPosMap: Map[String, Int]) =
    Bet(
      winF = getDoubleV(getElement(getIndex(winF, textPosMap), children)),
      draw = Some(getDoubleV(getElement(getIndex(draw, textPosMap), children))),
      winS = getDoubleV(getElement(getIndex(winS, textPosMap), children)),
      winFD =
        Some(getDoubleV(getElement(getIndex(winFD, textPosMap), children))),
      winSD =
        Some(getDoubleV(getElement(getIndex(winSD, textPosMap), children)))
    )

  protected def getEvent(el: Element) = {
    val aEl = el.select("a")
    if (aEl.size != 1)
      throw new IncorrectHtmlException(el)
    aEl.head.childNodes
      .filter(_.isInstanceOf[TextNode])
      .map(_.asInstanceOf[TextNode].content)
      .toVector match {
      case Vector(fT, sT) => (fT, sT)
      case _              => throw new IncorrectHtmlException(el)
    }
  }
}
