var game=[];
// setInterval(function(){
//     window.location.href= DOMAIN + "/web/games.html"
// }, 5000)


//var DOMAIN = "http://localhost:8080"
var DOMAIN = "https://batttleship.herokuapp.com"

var data=0;
function loginFetch(){
    const email = document.getElementById("username").value
    const password = document.getElementById("password").value
    
   fetch(DOMAIN + "/api/login?"+"email="+email+"&"+"password="+password,{
   method:'POST'})
   .then(function(response){
      if(response.status==200){
        //redirect
        window.location.href = DOMAIN + "/web/games.html";
      }else{
      alert(response.status)
      }
    }).catch(function(error){
      console.log("Request failed: " + error.message);
    })
}

function logoutFetch(){
    fetch(DOMAIN + "/api/logout?")
    .then(function(response){
        if(response.status==200){
            window.location.href = DOMAIN + "/web/index.html";
        }
    else{
        alert("eror")
    }}).catch(function(error){
        console.log("Request failed: " + error.message);
    })
}

function signup(){
    const firstName = document.getElementById("signup-firstname").value
    const lastName = document.getElementById("signup-lastname").value
    const email = document.getElementById("signup-email").value
    const password = document.getElementById("signup-password").value
    console.log("------------>"+firstName+lastName+email+password);
    fetch(DOMAIN + "/api/signup?"+"firstName="+firstName +"&"+"lastName="+lastName+"&"+"email="+email +"&"+"password="+ password ,{
        method:'POST'
    }).then(function(response){
        if(response.status==403){
            alert(response.status)
        }
    }).catch(function(error){
        console.log("Request failed: " + error.message);
      })
}

 function createGameFetch(){
     const email=game.player.email
     
     fetch(DOMAIN + "/api/games?username="+email,{
        method:'POST'
    }).then(function(response){
        var data=response.json();
        if(response.status==201){
            alert("Player save in the new game")
            return data;
        }
    }).then(function(n){
         window.location.href= DOMAIN + "/web/game.html?gp="+n
    }).catch(function(error){
        console.log("Request failed: " + error.message);
    })
}

fetch(
    DOMAIN + "/api/games"
    )
        .then(function(response){
            return response.json();
        }).then(function(response){
          game=response;
          console.log(game);
           renderList(game);
           
        //   renderWord("information") 
          leaderboard(response.games);
          renderHeaders();
          renderRows();
        //   insideCell()
        fillCell();
        // linkForJoinGame();
        })
        .catch(function (error) {
            console.log("Request failed: " + error.message);
        }   
)

function linkForJoinGame(n){
    for(var k=0;k<n.gamePlayer.length;k++){
        if( n.gamePlayer[k].player.id==game.player.id){
            var str="Return Game"
            var result = str.link(DOMAIN + "/web/game.html?gp="+n.gamePlayer[k].id);
            return result;
        }       
    }
    return "<button onclick='joinGame("+n.id+")'>"+"Join"+"</button>"
}

 var gpId;
 var res;
function joinGame(n){
    fetch(DOMAIN + "/api/game/"+n+"/players",{
        method:'POST'
    }).then(function(response){
        res=response.status;
        console.log(res+" res1")
        gpId=response.json();
            return gpId;
        } )
      .then(function(gpId){
        console.log(res)
        console.log(gpId+"-----2----")
        if(res==201){
            alert("Player save in the new game")
             window.location.href= DOMAIN + "/web/game.html?gp="+gpId
        }
   }).catch(function(error){
    if(res==403){
        alert("Game is full")
    }
    console.log("Request failed: " + error.message);})
}


function getItemHtml(game){ 
var emailList= "";
for(var i=0;i<game.gamePlayer.length;i++){
    emailList=emailList+(game.gamePlayer[i].player.email + ",");  
}  
 return "<li class='list-group-item'>" + game.id 
 + "&nbsp&nbsp&nbsp&nbsp"+game.created
 +"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+ emailList
 +"&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"+linkForJoinGame(game)+"</li>"   
}

function getListHtml(data) {
    return data.games.map(getItemHtml).join("");
}
function renderList(data) {
    var html = getListHtml(data);
    document.getElementById("list").innerHTML = html;
}
// key: burcu@gmailcom     value: {w=1, l=0, t=1}
// key: burcu@gmailcom     value: {w=1, l=1, t=0}

var map = new Map();
function leaderboard(response){
    response.forEach(g => {
        g.gamePlayer.forEach(gp => {
       
            if(!map.has(gp.player.email)){

                scoreCalculation(gp.score);
                map.set(gp.player.email, gp.score);
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
}

function scoreCalculation(obj){
    if(obj.wins!=0||obj.loses!=0||obj.ties!=0){
    var total= (obj.wins*1)+(obj.ties*0.5)-(obj.loses*1);
    obj.score = total;
   }
   else{
       obj.score="playing"
   }
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