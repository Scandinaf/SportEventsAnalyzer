package service.analyzer.module.impl.parimatch.teamsports

import service.analyzer.module.ModuleSportEvent
import service.analyzer.module.builder.parimatch.Config.TeamSports._
import service.analyzer.module.builder.parimatch.teamsports.Builder
import service.analyzer.module.handler.SportEventDBHandler
import service.logging.Logger
import service.mongo.DBLayer

/**
  * Created by serge on 24.03.2018.
  */
protected[module] class ModuleImpl
    extends ModuleSportEvent
    with SportEventDBHandler
    with Builder
    with Logger {

  override val dao: DBLayer.SportEventDAOComponent =
    DBLayer.sportEventDAO_Football

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
