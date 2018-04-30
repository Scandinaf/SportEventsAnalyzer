package service.http.model

import akka.actor.ActorRef
import akka.http.scaladsl.model.HttpRequest
import model.Page
import service.mongo.model.Website

/**
  * Created by serge on 13.12.2017.
  */
object HttpRequestCompanion {
  def buildHttpRequests(websites: Vector[Website], actor: ActorRef) =
    websites.map(
      el =>
        (HttpRequest(uri = el.url),
         (Page(el.url, el.module, el.cssQuery), actor)))
}
