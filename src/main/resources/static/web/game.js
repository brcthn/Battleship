var number= ["1","2","3","4","5","6","7","8","9","10"]
var letter= ["A","B","C","D","E","F","G","H","I","J"]
var shipInfo=[];

const urlParams = new URLSearchParams(window.location.search);
const myParam = urlParams.get('gp');

console.log("http://localhost:8080/api/game_view/"+myParam)

fetch("http://localhost:8080/api/game_view/"+myParam
).then(function(response){
    return response.json();
}).then(function(response){
  shipInfo=response;
  console.log(shipInfo)
  getShipLocation();
  getEmail();
}).catch(function(error){
    console.log("Request failed: " + error.message);
}
)
renderHeaders();
renderRows();

//map sirayla elemanlari gezer.
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
    for(var i=0;i< shipInfo.ship.length;i++){
       for(var k=0;k<shipInfo.ship[i].ships.length;k++){
        var shipLocation =shipInfo.ship[i].ships[k];
        document.getElementById("gameTable").rows[indexRow(shipLocation)].cells[indexCell(shipLocation)].style.backgroundColor="yellow"
        }
    }
}

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
    for(var i=0;i< shipInfo.gamePlayers.length;i++){ 
        if(shipInfo.id == shipInfo.gamePlayers[i].player.id){
            email = shipInfo.gamePlayers[i].player.email;
            document.getElementById("playerName1").innerHTML=shipInfo.gamePlayers[i].player.firstName+" "+shipInfo.gamePlayers[i].player.lastName
            document.getElementById("gameinformation1").innerHTML=email +" "+ "(you)" 
        }
        else{
            email = shipInfo.gamePlayers[i].player.email;
            document.getElementById("playerName2").innerHTML=shipInfo.gamePlayers[i].player.firstName+" "+shipInfo.gamePlayers[i].player.lastName
            document.getElementById("gameinformation2").innerHTML=email; 
        }
    }
}