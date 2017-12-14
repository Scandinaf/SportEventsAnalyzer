package service.analyzer

import akka.actor.Actor
import service.analyzer.model.DataMessage

/**
  * Created by serge on 13.12.2017.
  */
class AnalyzerActor extends Actor {
  override def receive: Receive = {
    case DataMessage(uri, data) => println(s"Actor, $uri")
    case _                      => println("something wrong!!!")
  }
}
