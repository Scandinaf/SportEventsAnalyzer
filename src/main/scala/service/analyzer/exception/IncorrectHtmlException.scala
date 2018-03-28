package service.analyzer.exception

import net.ruippeixotog.scalascraper.model.Element

/**
  * Created by serge on 08.03.2018.
  */
class IncorrectHtmlException(el: Element)
    extends RuntimeException(
      s"Element Name - ${el.tagName}, HTML - ${el.innerHtml}")
