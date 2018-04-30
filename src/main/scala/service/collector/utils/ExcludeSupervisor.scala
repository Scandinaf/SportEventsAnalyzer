package service.collector.utils

trait ExcludeSupervisor {
  protected val phrases: Vector[String]

  protected def necessaryExclude(v: String) = phrases.contains(v)

  protected def necessaryExcludePartialComparison(v: String) =
    phrases.exists(_.contains(v))
}
