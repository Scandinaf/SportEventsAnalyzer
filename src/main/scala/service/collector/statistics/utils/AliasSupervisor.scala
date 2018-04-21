package service.collector.statistics.utils

import service.collector.statistics.model.PostEventWinInformation

trait AliasSupervisor {
  protected val aliases: Map[String, String]

  protected def replaceAlias(el: PostEventWinInformation) =
    (aliases.get(el.firstTeam), aliases.get(el.secondTeam)) match {
      case (Some(fTAlias), Some(sTAlias)) =>
        el.copy(firstTeam = fTAlias, secondTeam = sTAlias)
      case (None, Some(sTAlias)) => el.copy(secondTeam = sTAlias)
      case (Some(fTAlias), None) => el.copy(firstTeam = fTAlias)
      case _                     => el
    }
}
