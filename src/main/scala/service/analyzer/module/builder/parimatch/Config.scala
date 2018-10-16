package service.analyzer.module.builder.parimatch

/**
  * Created by serge on 24.03.2018.
  */
object Config {
  object TeamSports {
    val event = "Событие"
    val date = "Дата"
    val winF = "П1"
    val draw = "X"
    val winS = "П2"
    val winFD = "1X"
    var winSD = "X2"
    val total = "Т"
    val individual_total = "iТ"
    val fields = List(event, date, total, winF, draw, winS, winFD, winSD, individual_total)
  }
}
