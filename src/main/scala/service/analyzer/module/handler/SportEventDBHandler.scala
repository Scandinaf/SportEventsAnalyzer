package service.analyzer.module.handler

import service.mongo.DBLayer
import service.mongo.dao.BaseDAOComponent
import service.mongo.model.SportEvent

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait SportEventDBHandler extends DBHandler[SportEvent] {
  override val dao: BaseDAOComponent[SportEvent] = DBLayer.sportEventDAO
}
