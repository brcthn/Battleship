var game=[];
fetch(
    "http://localhost:8080/api/games"
)
        .then(function(response){
            return response.json();
        }).then(function(response){
          game=response;
          console.log(game)
          
          renderList(game);
          renderWord("information")
        })
        .catch(function (error) {
            console.log("Request failed: " + error.message);
        })

function getItemHtml(game){
   return "<li>" + game.id + "&nbsp&nbsp" + game.created + "</li>";
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



// function gamesList(){
 
// for(var i=0;i< game.length;i++){
//     var liId=document.createElement("li");
//     document.getElementById('list').appendChild(liId);
//     liId.innerHTML="id"+" "+game[i].id;
//     var liCreate= document.createElement("li");
//     document.getElementById('list').appendChild( liCreate);
//     liCreate.innerHTML="created"+" "+game[i].created;
//     var liGamePlayer=document.createElement("li");
//     document.getElementById('list').appendChild(liGamePlayer);
//     liGamePlayer.innerHTML="gamePlayer";
      
// }





  