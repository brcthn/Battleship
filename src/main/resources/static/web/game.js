var number= ["1","2","3","4","5","6","7","8","9","10"]
var letter= ["A","B","C","D","E","F","G","H","I","J"]
var info=[];

const urlParams = new URLSearchParams(window.location.search);
const myParam = urlParams.get('gp');

console.log("http://localhost:8080/api/game_view/"+myParam)

fetch("http://localhost:8080/api/game_view/"+myParam
).then(function(response){
    if(response.status==401){
        alert("401")
    }
    return response.json();
}).then(function(response){
  info=response;
  console.log(info)
  getShipLocation();
  getEmail();
  getPlayerSalvo();
}).catch(function(error){
    console.log("Request failed: " + error.message);
}
)
renderHeaders();
renderRows();
createSalvoGrid();

//map sirayla elemanlari gezer.
//Ship Grid
function getHeaderHtml(){
    return "<tr><th></th>" + letter.map(function(letter){
        return "<th>"+letter+"</th>";}).join("") +"</tr>";
}
function renderHeaders() {
    var html = getHeaderHtml();
    document.getElementById("table-headers").innerHTML = html;
}
function getColumnsHtml() {
    return number.map(function() {
       return "<td>" +" " + "</td>";
    }).join("")
}
function getRowsHtml(){
      return number.map(function(number){
        return "<tr><th>" + number + "</th>"+  getColumnsHtml()+"</tr>";}).join(""); 
}
function renderRows() {
    var html = getRowsHtml();
    document.getElementById("table-rows").innerHTML = html;
}

function getShipLocation(){
    for(var i=0;i< info.ship.length;i++){
       for(var k=0;k<info.ship[i].ships.length;k++){
        var shipLocation =info.ship[i].ships[k];
        document.getElementById("gameTable").rows[indexRow(shipLocation)].cells[indexCell(shipLocation)].style.backgroundColor="#8296a1"
        }
    }
}
//n=H3
function indexCell(n){
    var cellsnumber= parseInt(letter.indexOf(n.charAt(0).toString())+1) 
    return cellsnumber;
}
function indexRow(n){
    var rowsnumber=(n.charAt(1));
    return rowsnumber;
}

function getEmail(){
  var email=""; 
    for(var i=0;i< info.gamePlayers.length;i++){ 
        if(info.id == info.gamePlayers[i].player.id){
            email = info.gamePlayers[i].player.email;
            document.getElementById("playerName1").innerHTML=info.gamePlayers[i].player.firstName+" "+info.gamePlayers[i].player.lastName
            document.getElementById("gameinformation1").innerHTML=email +" "+ "(you)" 
        }
        else{
            email = info.gamePlayers[i].player.email;
            document.getElementById("playerName2").innerHTML=info.gamePlayers[i].player.firstName+" "+info.gamePlayers[i].player.lastName
            document.getElementById("gameinformation2").innerHTML=email; 
        }
    }
}
//Salvo Grid
function createSalvoGrid(){
   renderHeadersSalvo();
   renderRowsSalvo();
}
function renderHeadersSalvo() {
    var html = getHeaderHtml();
    document.getElementById("table-headers-salvo").innerHTML = html;
}
function renderRowsSalvo() {
    var html2 = getRowsHtml();
    document.getElementById("table-rows-salvo").innerHTML = html2;
}

function getPlayerSalvo(){
    for(var i=0;i<info.salvoes.length;i++){
        for( var k=0;k<info.salvoes[i].locations.length;k++){
            var salvoLocation = info.salvoes[i].locations[k];
            if(info.id == info.salvoes[i].player){
               document.getElementById("gameTableSalvo").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor="#426585";
            } else {
                var isHit = false;
                info.ship.forEach(s => {
                    if(!isHit){
                        if(s.ships.includes(info.salvoes[i].locations[k])){
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor="#000000";
                            isHit = true;
                        } else {
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor="#dc6900"; 
                        }
                    }
                });
            } 
        }
    }
}