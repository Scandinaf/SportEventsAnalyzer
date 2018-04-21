package service.collector.statistics.akka.parimatch

import com.github.nscala_time.time.Imports.{DateTime, DateTimeFormat}
import net.ruippeixotog.scalascraper.model.Element
import service.collector.statistics.model.PostEventWinInformation
import service.logging.Logger
import service.mongo.model.EventResult
import service.mongo.model.EventResult.{Draw, FirstTeam, SecondTeam}
import service.utils.StringHelper.checkAndExcludeBracket

object Builder extends Logger {
  private val dtf = DateTimeFormat.forPattern("dd.MM.yy HH:mm")

  def elementToEntity(el: Element): Option[PostEventWinInformation] =
    el.children.toVector match {
      case Vector(date, fT, sT, r) =>
        elementToEntity(r.text, fT.text, sT.text, date)
      case v =>
        logger.warn(
          s"Couldn't handle data for the winner calculation process. Value: $v")
        None
    }

  private def elementToEntity(score: String,
                              firstTeam: String,
                              secondTeam: String,
                              date: Element) =
    calculateWinTeam(score, firstTeam, secondTeam).map(
      createPostEventWinInformation(checkAndExcludeBracket(firstTeam, 1),
                                    checkAndExcludeBracket(secondTeam, 1),
                                    date,
                                    _)
    )

  private def createPostEventWinInformation(firstTeam: String,
                                            secondTeam: String,
                                            date: Element,
                                            eventResult: EventResult) =
    PostEventWinInformation(
      date = DateTime.parse(date.text.replace('\u00A0', ' '), dtf).toDate,
      firstTeam = firstTeam,
      secondTeam = secondTeam,
      eventResult = eventResult
    )

  private def calculateWinTeam(scoreString: String,
                               firstTeam: String,
                               secondTeam: String) =
    scoreString match {
      case Dictionary.matchNotTakePlace => Some(EventResult())
      case _ =>
        splitScore(scoreString) match {
          case Array(scoreFirstTeam, scoreSecondTeam) =>
            matchScore(scoreString, scoreFirstTeam.toInt, scoreSecondTeam.toInt)
          case _ =>
            logger.warn(
              s"Couldn't handle data for the winner calculation process. FirstTeam: $firstTeam, SecondTeam: $secondTeam, Score: $scoreString.")
            None
        }
    }

  private def matchScore(scoreString: String, scoreFT: Int, scoreST: Int) =
    (scoreFT, scoreST) match {
      case (scoreFirstTeam, scoreSecondTeam)
          if scoreFirstTeam == scoreSecondTeam =>
        Some(EventResult(score = scoreString, winner = Draw))
      case (scoreFirstTeam, scoreSecondTeam)
          if scoreFirstTeam > scoreSecondTeam =>
        Some(EventResult(score = scoreString, winner = FirstTeam))
      case (scoreFirstTeam, scoreSecondTeam)
          if scoreFirstTeam < scoreSecondTeam =>
        Some(EventResult(score = scoreString, winner = SecondTeam))
    }

  private def splitScore(score: String) =
    score.split(" ").apply(0).split(":")

  private object Dictionary {
    val matchNotTakePlace = "Матч не состоялся"
  }
}
