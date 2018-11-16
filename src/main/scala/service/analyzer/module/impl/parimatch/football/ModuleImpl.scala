package service.analyzer.module.impl.parimatch.football

import service.analyzer.module.impl.parimatch.Module
import service.mongo.DBLayer

/**
  * Created by serge on 24.03.2018.
  */
protected[module] class ModuleImpl extends Module {
  override val dao: DBLayer.SportEventDAOComponent =
    DBLayer.sportEventDAO_Football
}
