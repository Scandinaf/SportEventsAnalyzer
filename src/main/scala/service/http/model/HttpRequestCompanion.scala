package service.http.model

import akka.http.scaladsl.model.HttpRequest
import model.{Page, Website}

/**
  * Created by serge on 13.12.2017.
  */
object HttpRequestCompanion {
  def apply(websites: Vector[Website]): Vector[(HttpRequest, Page)] =
    websites
      .map(w =>
        w.pages.map(p =>
          (HttpRequest(uri = buildUri(w.protocol, w.domain, p.path)), p)))
      .flatten

  private def buildUri(protocol: String, domain: String, path: String) =
    s"$protocol://$domain$path"
}
