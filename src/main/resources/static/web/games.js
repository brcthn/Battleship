var game=[];
fetch(
    "http://localhost:8080/api/games"
    )
        .then(function(response){
            return response.json();
        }).then(function(response){
          game=response;
          console.log(game);
          renderList(game);
        //   renderWord("information") 
          leaderboard(response);
          renderHeaders();
          renderRows();
        //   insideCell()
        fillCell();
        })
        .catch(function (error) {
            console.log("Request failed: " + error.message);
        }   
)
function getItemHtml(game){ 
   
var emailList= "";
for(var i=0;i<game.gamePlayer.length;i++){
    emailList=emailList+(game.gamePlayer[i].player.email + ",");  
    console.log(game)
}
   return "<li class='list-group-item'>" + game.id + "&nbsp&nbsp&nbsp&nbsp"+game.created+"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+ emailList+ "</li>"   
}

function getListHtml(data) {
    return data.map(getItemHtml).join("");
}

function renderList(data) {
    var html = getListHtml(data);
    document.getElementById("list").innerHTML = html;
}

// function renderWord(word) {
//     document.getElementById("word").innerHTML = word;
// }

// function playerList(){
//   var  playerList=[]
//    for(var i=0;i<game.length;i++){
//        for(var k=0;k<2;k++){
//        playerList.push(game[i].gamePlayer[k])
//        }
//    } 
//    console.log(playerList)
// }

// function totalWin(){
//     var list=new Map();
//     for(var j=0;j<this.playerList.length;j++){
//         for(var k=0;k<this.game[j].gamePlayer.length;k++){
//             list.set(game[j].gamePlayer[k].player.email, game[j].gamePlayer[k].score)
//         }
//     } 
// }

// function total(wins,lost,tie){
//   var total= (wins*1)+(tie*0.5)-(lost*1);
//   return total;
// }

// function ornek(){
//     var  playerList=[]
//     for(var i=0;i<game.length;i++){
//         for(var k=0;k<2;k++){
//             if(playerList.indexOf(game[i].gamePlayer[k].player.id)===-1){ //playerList in icinde bu playerid yoksa
//                   playerList.push(game[i].gamePlayer)       //playerListte ekle
//             }
//             else{
//                 (playerList.find(game[i].gamePlayer[k].player.id)).score.wins + game[i].gamePlayer[k].player.score.wins;// eger varsa bu playerListteki kisinin 
//             }                                                                       //wins ile digerini topla
//         }
//     }
//     console.log(playerList)
// }


// key: hasan@gmailcom     value: {w=1, l=0, t=1}
// key: hasan@gmailcom     value: {w=1, l=1, t=0}

var map = new Map();
function leaderboard(response){

    response.forEach(g => {
        g.gamePlayer.forEach(gp => {
            if(!map.has(gp.player.email)){

                scoreCalculation(gp.score);
                map.set(gp.player.email, gp.score);
                // console.log(gp.player.email+"-------map e yeni oyuncu---->"+JSON.stringify(gp.score))
            } else {
                // console.log(gp.player.email+"-------map e yeni oyuncu---->"+JSON.stringify(gp.score))

                var score = map.get(gp.player.email);
                // console.log(gp.player.email+"-------map de bulunan oyuncu---->"+score.ties)

                score.wins = score.wins + gp.score.wins; 
                score.loses = score.loses + gp.score.loses; 
                score.ties = score.ties + gp.score.ties; 

                scoreCalculation(score);

                map.set(gp.player.email, score);
                // console.log("-------map de bulunan oyuncunun yeni score---->"+score)

            }
        });
    });
    for (let [email,score] of map.entries()) {
        console.log(email +":"+ JSON.stringify(score));
    }
}

function scoreCalculation(obj){
    var total= (obj.wins*1)+(obj.ties*0.5)-(obj.loses*1);
    obj.score = total;
}

var scoreHeader=["Player","Won","Lost","Tied","Total"];

// for header


function getHeaderHtml(){
    return "<tr>"+ scoreHeader.map(function(header){
        return "<th>"+header+"</th>";}).join("")+"</tr>";
}

function renderHeaders(){
     var html=getHeaderHtml();
     document.getElementById("scoreHeader").innerHTML=html;
}

function getColumnsHtml(){
    return scoreHeader.map(function(){
         return "<td>"+""+"</td>"}).join("")
}
function getRowsHtml(){
    return Array.from(map).map(function(){
        return "<tr>"+getColumnsHtml()+"<tr>"}).join("");
}

function renderRows(){
    var html=getRowsHtml();
    document.getElementById("scoreBody").innerHTML=html;
}

 
function fillCell(){
    var i = 1;
    for (let [email,score] of map.entries()) {
        document.getElementById("table").rows[i].cells[0].innerHTML=email;
        document.getElementById("table").rows[i].cells[1].innerHTML=score.wins;
        document.getElementById("table").rows[i].cells[2].innerHTML=score.loses;
        document.getElementById("table").rows[i].cells[3].innerHTML=score.ties;
        document.getElementById("table").rows[i].cells[4].innerHTML=score.score;
        i = i + 2;
    }

}