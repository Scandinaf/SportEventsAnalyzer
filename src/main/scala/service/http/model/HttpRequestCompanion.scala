package service.http.model

import akka.http.scaladsl.model.HttpRequest
import model.Website

/**
  * Created by serge on 13.12.2017.
  */
object HttpRequestCompanion {

  def apply(website: Website): HttpRequest =
    HttpRequest(uri = buildUri(website))

  private def buildUri(w: Website) =
    s"${w.protocol}://${w.domain}${w.pagePath}"
}
