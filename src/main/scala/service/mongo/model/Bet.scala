package service.mongo.model

/**
  * Created by serge on 08.03.2018.
  */
case class Bet(winF: Double,
               draw: Option[Double] = None,
               winS: Double,
               winFD: Option[Double] = None,
               winSD: Option[Double] = None,
               total: Option[Total] = None,
               individual_total: Option[IndividualTotal] = None)
