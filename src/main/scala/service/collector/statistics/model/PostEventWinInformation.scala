package service.collector.statistics.model

import java.util.Date

import service.mongo.model.EventResult

case class PostEventWinInformation(firstTeam: String,
                                   secondTeam: String,
                                   date: Date,
                                   eventResult: EventResult)
