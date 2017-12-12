package service.http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}

/**
  * Created by serge on 12.12.2017.
  */
object HttpClient {
  implicit val materializer = ActorMaterializer()
  private val http = Http(ActorSystem())
  private val threadCount = 5

  object Get {
    def singleCall(uri: String) = http.singleRequest(HttpRequest(uri = uri))

    def callByStream(requestIt: Iterator[HttpRequest]) =
      Source
        .fromIterator(() => requestIt)
        .mapAsync(threadCount)(http.singleRequest(_))
        .runWith(Sink.seq)

    def callByStream[T](requestIt: Iterator[HttpRequest])(
        handler: (HttpRequest, HttpResponse) => T) =
      Source
        .fromIterator(() => requestIt)
        .mapAsync(threadCount)(http.singleRequest(_))
        .map(handler(_))
        .runWith(Sink.ignore)
  }
}
