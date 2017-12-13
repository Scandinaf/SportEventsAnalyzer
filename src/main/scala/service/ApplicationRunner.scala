package service

import akka.http.scaladsl.model.HttpRequest
import model.Website
import service.http.HttpClient
import service.http.handler.WebsiteHandler
import service.http.model.HttpRequestCompanion
import service.mongo.DBLayer
import service.mongo.model.AnalyzedWebsite

/**
  * Created by serge on 13.12.2017.
  */
class ApplicationRunner {

  def start = {
    val websites = Config.websites
    val analyzedWebsites = DBLayer.analyzedWebsiteDAO.findByDomains(websites)
    val leftPart = calculateLeft(websites, analyzedWebsites)
    if (!leftPart.isEmpty)
      pullHttpRequests(createHttpRequests(leftPart))
  }

  private def createHttpRequests(websites: Seq[Website]) =
    websites.map(HttpRequestCompanion(_))

  private def pullHttpRequests(requests: Seq[HttpRequest]) =
    HttpClient.Get.callByStreamWithHandler(requests.iterator)(
      WebsiteHandler().handler)

  private def calculateLeft(websites: Seq[Website],
                            analyzedWebsites: Seq[AnalyzedWebsite]) =
    websites.filter(w =>
      !analyzedWebsites.exists(aw => aw.domain.equals(w.domain)))
}

object ApplicationRunner {
  def apply(): ApplicationRunner = new ApplicationRunner()
}
