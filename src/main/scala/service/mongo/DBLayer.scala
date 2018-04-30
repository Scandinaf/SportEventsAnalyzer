package service.mongo

import service.mongo.dao.{SportEventDAO_Football, WebsiteDAO}

/**
  * Created by serge on 11.12.2017.
  */
object DBLayer extends SportEventDAO_Football with WebsiteDAO
