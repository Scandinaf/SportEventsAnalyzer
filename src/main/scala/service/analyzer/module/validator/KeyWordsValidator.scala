package service.analyzer.module.validator

trait KeyWordsValidator {
  def validate[E](map: Map[String, E]): Either[List[String], Map[String, E]]
}
