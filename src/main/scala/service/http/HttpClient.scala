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
    def singleCall(uri: String) = http.singleRequest(HttpRequest(uri = uri))

    def callByStream(requestIt: Iterator[HttpRequest]) =
      Source
        .fromIterator(() => requestIt)
        .mapAsync(threadCount)(http.singleRequest(_))
        .runWith(Sink.seq)

    def callByStreamWithHandler[E, T](requestIt: Iterator[(HttpRequest, E)])(
        handler: (HttpRequest, HttpResponse, E) => T) =
      Source
        .fromIterator(() => requestIt)
        .mapAsync(threadCount)(el =>
          http.singleRequest(el._1).map((el._1, _, el._2)))
        .map(r => handler(r._1, r._2, r._3))
        .runWith(Sink.ignore)
  }
}
