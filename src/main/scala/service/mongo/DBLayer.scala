package service.mongo

import service.mongo.dao.{SportEventDAO_Football, SportEventDAO_Hockey, WebsiteDAO}

/**
  * Created by serge on 11.12.2017.
  */
object DBLayer
    extends SportEventDAO_Football
    with SportEventDAO_Hockey
    with WebsiteDAO
