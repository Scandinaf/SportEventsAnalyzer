package service.http.handler

import akka.http.scaladsl.model.{HttpRequest, HttpResponse}

/**
  * Created by serge on 12.12.2017.
  */
trait BaseHttpHandler[T] {
  def handler(request: HttpRequest, response: HttpResponse): T
}
