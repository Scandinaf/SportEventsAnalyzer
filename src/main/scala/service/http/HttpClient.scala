package service.http

import _root_.model.ApplicationSettings.Actor.{forkJoinEC, materializer, system}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.{Sink, Source}

/**
  * Created by serge on 12.12.2017.
  */
object HttpClient {
  private val http = Http(system)
  private val threadCount = 5

  object Get {
    def singleCall[E, T](request: HttpRequest,
                         v: E,
                         handler: (HttpRequest, HttpResponse, E) => T) =
      http.singleRequest(request).map(handler(request, _, v))

    def callByStreamWithHandler[E, T](requestIt: Iterator[(HttpRequest, E)])(
        handler: (HttpRequest, HttpResponse, E) => T) =
      Source
        .fromIterator(() => requestIt)
        .mapAsyncUnordered(threadCount)(el =>
          http.singleRequest(el._1).map((el._1, _, el._2)))
        .map(r => handler(r._1, r._2, r._3))
        .runWith(Sink.ignore)
  }
}
