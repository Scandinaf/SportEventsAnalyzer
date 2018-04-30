package service.akka

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, OneForOneStrategy, SupervisorStrategy}
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

trait ActorTemplate extends Actor with Logger {
  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
      case x => {
        logger.error("Something went wrong during the execution of the actor.",
                     x)
        Resume
      }
    }
}
