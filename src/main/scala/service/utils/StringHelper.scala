package service.utils

object StringHelper {

  def checkAndExcludeBracket(value: String) =
    if (value.contains(Dictionary.bracket))
      value.substring(0, value.indexOf(Dictionary.bracket)).trim
    else value

  private object Dictionary {
    val bracket = '('
  }
}
