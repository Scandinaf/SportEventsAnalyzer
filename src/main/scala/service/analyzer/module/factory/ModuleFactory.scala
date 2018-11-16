package service.analyzer.module.factory
import model.Error
import service.analyzer.module.Module
import service.analyzer.module.impl.parimatch.football.{ModuleImpl => fModule}
import service.analyzer.module.impl.parimatch.hockey.{ModuleImpl => hModule}
import service.logging.Logger

/**
  * Created by serge on 24.03.2018.
  */
object ModuleFactory extends Logger {
  val moduleMap =
    Map(Modules.parimatchF -> new fModule, Modules.parimatchH -> new hModule)

  def getModule(moduleName: String): Either[Error, Module] =
    moduleMap.get(moduleName) match {
      case Some(m) => Right(m)
      case _       => Left(Error(s"$moduleName - Module could not be found."))
    }

  object Modules {
    val parimatchF = "parimatch.Football"
    val parimatchH = "parimatch.Hockey"
  }
}
