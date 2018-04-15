package model

case class Error(msg: String, throwable: Option[Throwable] = None)
