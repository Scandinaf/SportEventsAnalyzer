package service.analyzer.model

import akka.http.scaladsl.model.Uri
import model.Page

/**
  * Created by serge on 13.12.2017.
  */
case class DataMessage(uri: Uri, data: String, p: Page)
