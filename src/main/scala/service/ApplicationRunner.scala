package service

import _root_.akka.http.scaladsl.model.HttpRequest
import model.Website
import service.http.HttpClient
import service.http.handler.WebsiteHandler
import service.http.model.HttpRequestCompanion
import service.logging.Logger
import service.mongo.DBLayer
import service.mongo.model.AnalyzedWebsite

/**
  * Created by serge on 13.12.2017.
  */
class ApplicationRunner extends Logger {

  def start = {
    val websites = Config.websites
    val analyzedWebsites = DBLayer.analyzedWebsiteDAO.findByDomains(websites)
    val leftPart = calculateLeft(websites, analyzedWebsites)
    if (!leftPart.isEmpty)
      pullHttpRequests(createHttpRequests(leftPart))
    else
      logger.info("All sites have already been analyzed!!!")
  }

  private def createHttpRequests(websites: Vector[Website]) =
    websites.map(HttpRequestCompanion(_))

  private def pullHttpRequests(requests: Vector[HttpRequest]) =
    HttpClient.Get.callByStreamWithHandler(requests.iterator)(
      WebsiteHandler().handler)

  private def calculateLeft(websites: Vector[Website],
                            analyzedWebsites: Vector[AnalyzedWebsite]) =
    websites.filter(w => !analyzedWebsites.exists(aw => aw.domain == w.domain))
}

object ApplicationRunner {
  def apply(): ApplicationRunner = new ApplicationRunner()
}
