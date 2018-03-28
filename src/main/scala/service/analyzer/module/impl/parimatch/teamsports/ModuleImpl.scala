package service.analyzer.module.impl.parimatch.teamsports

import service.analyzer.module.ModuleSportEvent
import service.analyzer.module.builder.parimatch.teamsports.Builder
import service.analyzer.module.handler.SportEventDBHandler
import service.logging.Logger

/**
  * Created by serge on 24.03.2018.
  */
protected[module] class ModuleImpl
    extends ModuleSportEvent
    with SportEventDBHandler
    with Builder
    with Logger
