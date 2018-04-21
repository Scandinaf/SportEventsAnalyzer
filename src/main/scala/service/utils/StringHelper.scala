package service.utils

object StringHelper {

  def checkAndExcludeBracket(value: String, subStringIndex: Int = 0) =
    if (value.contains(Dictionary.bracket))
      value.substring(0, value.indexOf(Dictionary.bracket) - subStringIndex)
    else value

  private object Dictionary {
    val bracket = '('
  }
}
