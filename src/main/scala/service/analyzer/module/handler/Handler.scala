package service.analyzer.module.handler

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait Handler[T] {
  def handle(Obj: T)
}
