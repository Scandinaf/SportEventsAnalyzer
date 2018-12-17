package service.analyzer.module.impl.parimatch

import service.analyzer.module.ModuleSportEvent
import service.analyzer.module.builder.parimatch.Config.TeamSports.fields
import service.analyzer.module.builder.parimatch.teamsports.Builder
import service.analyzer.module.handler.SportEventDBHandler
import service.logging.Logger

trait Module
    extends ModuleSportEvent
    with SportEventDBHandler
    with Builder
    with Logger {

  override def validate[E](
      map: Map[String, E]): Either[List[String], Map[String, E]] =
    if (map.isEmpty) Left(fields)
    else compareKeys(map.keys.toList, map)

  protected def compareKeys[E](keys: List[String], map: Map[String, E]) =
    fields.diff(keys) match {
      case Nil => Right(map)
      case l   => Left(l)
    }

}
