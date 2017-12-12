package service.http.handler
import java.nio.charset.StandardCharsets

import akka.http.scaladsl.model.{HttpRequest, HttpResponse, ResponseEntity}
import akka.util.ByteString

import scala.concurrent.Future
import scala.concurrent.duration._

/**
  * Created by serge on 12.12.2017.
  */
class WebsiteHandler extends BaseHttpHandler[Unit] {

  val timeout = 1 minute
  val defaultCharset = StandardCharsets.UTF_8

  override def handler(req: HttpRequest, res: HttpResponse): Unit = {
    val bs: Future[ByteString] = res.entity.toStrict(timeout).map { _.data }
    val s: Future[String] = bs.map(_.decodeString(getCharset(res.entity)))
  }

  private def getCharset(entity: ResponseEntity) =
    entity.getContentType().getCharsetOption match {
      case optional if optional.isPresent => optional.get().nioCharset()
      case _                              => defaultCharset
    }
}
