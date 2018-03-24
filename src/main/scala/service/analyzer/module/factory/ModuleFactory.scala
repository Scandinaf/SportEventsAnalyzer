package service.analyzer.module.factory
import service.analyzer.exception.ModuleNotFoundException
import service.analyzer.module.Module
import service.analyzer.module.impl.parimatch.teamsports.ModuleImpl
import service.logging.Logger

/**
  * Created by serge on 24.03.2018.
  */
object ModuleFactory extends Logger {
  val moduleMap = Map(Modules.parimatchTS -> new ModuleImpl)

  def getModule(moduleName: String): Module =
    moduleMap.get(moduleName) match {
      case Some(m) => m
      case _ =>
        throw new ModuleNotFoundException(
          s"$moduleName - Module could not be found.")
    }

  object Modules {
    val parimatchTS = "parimatch.TeamSports"
  }
}
