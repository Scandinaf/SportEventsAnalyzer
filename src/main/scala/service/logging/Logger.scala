package service.logging

import org.slf4j.LoggerFactory

/**
  * Created by serge on 14.12.2017.
  */
trait Logger {
  val logger = LoggerFactory.getLogger(this.getClass)
}
