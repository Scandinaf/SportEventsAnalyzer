package service.analyzer.module

import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait Module {
  def process(elementQuery: ElementQuery[Element])
}
