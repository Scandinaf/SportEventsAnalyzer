package service.akka.combiner

import akka.actor.{ActorRef, FSM, Props}
import service.akka.combiner.ProcessDoneActor.Message.{PartWorkPerformed, ProcessCompleted}
import service.akka.combiner.ProcessDoneActor._
import service.logging.Logger

import scala.concurrent.duration._
import scala.language.postfixOps

class ProcessDoneActor(processName: String, actor: ActorRef)
    extends FSM[State, Data]
    with Logger {
  startWith(Active, NoData)

  when(Active, stateTimeout = 3 minute) {
    case Event(StateTimeout, _) =>
      logger.info(s"The process $processName is completed.")
      actor ! ProcessCompleted
      stop
    case Event(PartWorkPerformed(logMessage), _) =>
      logger.info(s"ProcessName - $processName. LogMessage - $logMessage.")
      stay
  }

  initialize()
}

object ProcessDoneActor {
  def props(processName: String, actor: ActorRef): Props =
    Props(classOf[ProcessDoneActor], processName, actor)

  object Message {
    case class PartWorkPerformed(logMessage: String)
    case object ProcessCompleted
  }

  sealed trait State
  case object Active extends State

  sealed trait Data
  case object NoData extends Data
}
