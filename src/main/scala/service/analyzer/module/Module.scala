package service.analyzer.module

import net.ruippeixotog.scalascraper.model.{Element, ElementQuery}
import service.analyzer.exception.Error
import service.analyzer.module.validator.KeyWordsValidator

/**
  * Created by serge on 24.03.2018.
  */
protected[module] trait Module extends KeyWordsValidator {
  def process(elementQuery: ElementQuery[Element]): Either[Error, Unit]
}
