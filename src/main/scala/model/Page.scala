package model

import com.typesafe.config.Config
import exception.IncorrectConfigException
import service.analyzer.module.factory.ModuleFactory

case class Page(path: String, module: String, cssQuery: String)
object Page {
  def apply(path: String, module: String, cssQuery: String): Page =
    new Page(path, checkModule(module), cssQuery)

  def apply(config: Config, defCssQuery: String): Page =
    new Page(config.getString(Config.path),
             checkModule(config.getString(Config.module)),
             getCssQuery(config, defCssQuery))

  private def checkModule(moduleName: String): String =
    ModuleFactory.getModule(moduleName) match {
      case Right(_) => moduleName
      case _ =>
        throw new IncorrectConfigException(
          s"$moduleName - Module could not be found.")
    }

  private def getCssQuery(config: Config, defCssQuery: String) =
    if (!config.hasPath(Config.cssQuery)) defCssQuery
    else config.getString(Config.cssQuery)

  object Config {
    val path = "path"
    val module = "module"
    val cssQuery = "cssQuery"
  }
}
