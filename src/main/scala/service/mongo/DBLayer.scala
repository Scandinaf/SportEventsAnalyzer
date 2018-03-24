package service.mongo

import service.mongo.dao.{AnalyzedWebsiteDAO, SportEventDAO}

/**
  * Created by serge on 11.12.2017.
  */
object DBLayer extends AnalyzedWebsiteDAO with SportEventDAO
