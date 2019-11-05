var game=[];
fetch(
    "http://localhost:8080/api/games"
    )
        .then(function(response){
            return response.json();
        }).then(function(response){
          game=response;
          renderList(game);
          renderWord("information")  
        })
        .catch(function (error) {
            console.log("Request failed: " + error.message);
        }   
)
function getItemHtml(game){ 
var emailList= "";
for(var i=0;i<game.gamePlayer.length;i++){
    emailList=emailList+(game.gamePlayer[i].player.email + ",");  
}
   return "<li class='list-group-item' >" + game.id + "&nbsp&nbsp"+game.created+"&nbsp&nbsp"+ emailList+ "</li>"   
}

function getListHtml(data) {
    return data.map(getItemHtml).join("");
}

function renderList(data) {
    var html = getListHtml(data);
    document.getElementById("list").innerHTML = html;
}

function renderWord(word) {
    document.getElementById("word").innerHTML = word;
}