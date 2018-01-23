package service.analyzer

import org.scalatest.FlatSpec

/**
  * Created by serge on 22.01.2018.
  */
class SiteAnalyzerSpec extends FlatSpec {

  behavior of "SiteAnalyzer"

  "SiteAnalyzer" should "correctly parse pages where content placed in form" in {
    SiteAnalyzer("""<head>
                   |   <meta charset="utf-8">
                   |   <meta name="viewport" content="width=device-width, initial-scale=1">
                   |   <title>Test page</title>
                   |  </head>
                   |  <body>
                   |       <form name="f1" id="f1">
                   |	   </form>
                   |	   <div>
                   |		<form name="f21" id="f21">
                   |		</form>
                   |	   </div>
                   |  </body>"""").analyze
  }

}
