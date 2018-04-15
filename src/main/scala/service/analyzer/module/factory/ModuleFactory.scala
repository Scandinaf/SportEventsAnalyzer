package service.analyzer.module.factory
import model.Error
import service.analyzer.module.Module
import service.analyzer.module.impl.parimatch.teamsports.ModuleImpl
import service.logging.Logger

/**
  * Created by serge on 24.03.2018.
  */
object ModuleFactory extends Logger {
  val moduleMap = Map(Modules.parimatchTS -> new ModuleImpl)

  def getModule(moduleName: String): Either[Error, Module] =
    moduleMap.get(moduleName) match {
      case Some(m) => Right(m)
      case _       => Left(Error(s"$moduleName - Module could not be found."))
    }

  object Modules {
    val parimatchTS = "parimatch.TeamSports"
  }
}
