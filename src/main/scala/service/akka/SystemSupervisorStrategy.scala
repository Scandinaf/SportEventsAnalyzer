package service.akka

import akka.actor.SupervisorStrategy.Restart
import akka.actor.{OneForOneStrategy, SupervisorStrategy, SupervisorStrategyConfigurator}
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by serge on 24.03.2018.
  */
class SystemSupervisorStrategy
    extends SupervisorStrategyConfigurator
    with Logger {
  override def create(): SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error(s"Exception - ${x}")
        Restart
      }
    }
}
