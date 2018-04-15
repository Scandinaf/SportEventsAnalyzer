package service.collector.statistics.akka.parimatch

import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import net.ruippeixotog.scalascraper.model.Element
import service.collector.statistics.model.PostEventWinInformation
import service.logging.Logger
import service.mongo.model.EventResult

object Builder extends Logger {
  private val dtf = DateTimeFormat.forPattern("dd.MM.yy HH:mm")

  def elementToEntity(el: Element): Option[PostEventWinInformation] =
    el.children.toVector match {
      case Vector(data, fT, sT, r) =>
        val firstTeam = fT.text
        val secondTeam = sT.text
        calculateWinTeam(r.text, firstTeam, secondTeam).map(
          v =>
            PostEventWinInformation(
              date =
                DateTime.parse(data.text.replace('\u00A0', ' '), dtf).toDate,
              firstTeam = firstTeam,
              secondTeam = secondTeam,
              eventResult = v
          ))
      case v =>
        logger.warn(
          s"Couldn't handle data for the winner calculation process. Value: $v")
        None
    }

  private def calculateWinTeam(scoreString: String,
                               firstTeam: String,
                               secondTeam: String) =
    scoreString.split(" ").apply(0).split(":") match {
      case Array(scoreFirstTeam, scoreSecondTeam) =>
        (scoreFirstTeam.toInt, scoreSecondTeam.toInt) match {
          case (scoreFirstTeam, scoreSecondTeam)
              if scoreFirstTeam == scoreSecondTeam =>
            Some(EventResult(score = Some(scoreString)))
          case (scoreFirstTeam, scoreSecondTeam)
              if scoreFirstTeam > scoreSecondTeam =>
            Some(
              EventResult(score = Some(scoreString), winner = Some(firstTeam)))
          case (scoreFirstTeam, scoreSecondTeam)
              if scoreFirstTeam < scoreSecondTeam =>
            Some(
              EventResult(score = Some(scoreString), winner = Some(secondTeam)))
        }
      case _ =>
        logger.warn(
          s"Couldn't handle data for the winner calculation process. FirstTeam: $firstTeam, SecondTeam: $secondTeam, Score: $scoreString.")
        None
    }
}
