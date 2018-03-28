package service.analyzer.exception

case class Error(msg: String, throwable: Option[Throwable] = None)
