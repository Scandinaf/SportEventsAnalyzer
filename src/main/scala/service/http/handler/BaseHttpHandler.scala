package service.http.handler

import _root_.model.ApplicationSettings.Actor.{forkJoinEC, materializer}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity}
import akka.http.scaladsl.unmarshalling.Unmarshal

/**
  * Created by serge on 12.12.2017.
  */
trait BaseHttpHandler[T, E] {
  def handler(request: HttpRequest, response: HttpResponse, v: E): T

  protected def getErrorMessage(req: HttpRequest, res: HttpResponse) =
    s"HttpResponse hasn't processed. Request - $req. Response - $res"

  protected def getData(entity: ResponseEntity) = Unmarshal(entity).to[String]
}
