package util

import java.util.Calendar

/**
  * Created by serge on 13.12.2017.
  */
object DateHelper {
  def getDate(month: Int = 1) = {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, month);
    calendar.getTime
  }

  def getCurrentDate =
    Calendar.getInstance().getTime
}
