var smallСhanceDraw = 1.20 //the difference between the rates(WinFD\SD), chance of draw is around ~4.2 percent
var smallChanceLose = 0.60 //the difference between the rates(WinFD\SD), chance of lose is around ~5 percent
var smallChanceLose1 = 2.50 //the difference between the rates(WinF\S), chance of lose is around ~? percent

var cursor = db.sportEvent_Football.aggregate([
    { $match : {$and : [{"date": {$gte : ISODate("2018-09-03T00:00:00.000+03:00")}},{"date": { $lt : ISODate("2018-09-04T00:00:00.000+03:00")}}],
        "eventResult" : {$ne : null}, 
        "eventResult.eventTookPlace" : true, 
        "event" : {$not : /Хозяева/}}},
    { $addFields: {"bet.differenceBetweenBets" : {$abs : {$subtract : ["$bet.winFD", "$bet.winSD"]}}}},
    { $addFields: {"bet.differenceBetweenBets1" : {$abs : {$subtract : ["$bet.winF", "$bet.winS"]}}}},
    { $match : {"bet.differenceBetweenBets" : {$gte : smallChanceLose}, "bet.differenceBetweenBets1" : {$lt : smallChanceLose1}}},
    ])
    
//rates that didn't play
// var notPlayedBets = [];
// cursor.map(function(record) {
//     if (getResult(getOurBetInformation(record.bet), record.eventResult.winner) == 0)
//         notPlayedBets.push(record);
// })

// notPlayedBets;

var count = cursor.count();
var resultWinCount = 0;
var resultWin = 0;
var bank = 100;
cursor.forEach(function(record) { 
        var result = getResult(getOurBetInformation(record.bet), record.eventResult.winner);
        resultWin += result;
        if (result > 0) {
            resultWinCount++;
        }
    });

print(count);
print(resultWinCount);
print(resultWin);
print(resultWin - count);

function getResult(ourBetInformation, winner) {
    if ((winner == 3 && ourBetInformation.bet.satisfyDraw == true) || (winner == ourBetInformation.teamWinner)) 
        return ourBetInformation.bet.win;
    else return 0;
}

function getOurBetInformation(bet) {
    var teamWinner = getTeamWinner(bet)
    var ourBet = getOurBet(teamWinner, bet)
    return new OurBetInformation(teamWinner, ourBet);
}

function getTeamWinner(bet) {
    if (bet.winFD <= bet.winSD)
        return 1;
    else return 2;
}

function getOurBet(teamWinner, bet) {
    if (teamWinner == 1) 
        return _getOurBet(bet.winFD, bet.winF, bet.differenceBetweenBets);
    else return _getOurBet(bet.winSD, bet.winS, bet.differenceBetweenBets);
}

function _getOurBet(winD, win, differenceBetweenBets) {
    if (differenceBetweenBets >= smallСhanceDraw)
        return new OurBet(win, false);
    else return new OurBet(winD, true);
}

function OurBetInformation(teamWinner, bet) {
   this.teamWinner = teamWinner;
   this.bet = bet;
}

function OurBet(win, satisfyDraw) {
    this.win = win;
   this.satisfyDraw = satisfyDraw;
}

// db.sportEvent_Football.aggregate([
//     { $match : {"eventResult" : {$ne : null}, "eventResult.eventTookPlace" : true} },
//     { $addFields: {"differenceBetweenBets" : {$abs : {$subtract : ["$bet.winFD", "$bet.winSD"]}}}},
//     { $match : {"differenceBetweenBets" : {$gte : smallСhanceDraw}, "eventResult.winner" : 3}}
//     ])
    
// db.sportEvent_Football.aggregate([
//     { $match : {"eventResult" : {$ne : null}, "eventResult.eventTookPlace" : true} },
//     { $match : {"bet.draw" : {$gte : 4}, "eventResult.winner" : 3}}
//     ])    


//rates that didn't play
// var notPlayedBets = [];
// cursor.map(function(record) {
//     if (getResult(getOurBet(record.bet), record.eventResult.winner) == 0)
//         notPlayedBets.push(record);
// })

// notPlayedBets;