package service.utils

import java.util.{Calendar, Date}

import service.utils.ImplicitHelper.CalendarImplicits.CalendarBuilder

object TimeDateUtil {
  def getCalendarInstance = Calendar.getInstance()

  def checkDateInRange[T](l: Vector[T], range: DateRange, f: T => Date) = {
    val d = getDateByRange(range)
    l.filter(v => d.check(f(v)))
  }

  def checkDateInRange(date: Date, range: DateRange) =
    getDateByRange(range).check(date)

  private def getDateByRange(range: DateRange) = range match {
    case DayRange  => getDate(Calendar.DAY_OF_WEEK, 2)
    case WeekRange => getDate(Calendar.DAY_OF_WEEK, 8)
  }

  private def getDate(field: Int, amount: Int) = {
    val c = getCalendarInstance
    new DateRangeHelper(c.getTime,
                        setTimeBeginningDay(c)
                          .addB(field, amount)
                          .getTime)
  }

  private def setTimeBeginningDay(c: Calendar) =
    c.setB(Calendar.HOUR_OF_DAY, 0)
      .setB(Calendar.MINUTE, 0)
      .setB(Calendar.SECOND, 0)

  sealed trait DateRange
  object DayRange extends DateRange
  object WeekRange extends DateRange

  private class DateRangeHelper(startDate: Date, endDate: Date) {
    def check(date: Date) = date.after(startDate) && date.before(endDate)
  }
}
