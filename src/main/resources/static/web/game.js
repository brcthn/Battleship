var number = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
var letter = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
var info = [];
var shipHeader = ["Type", "Lenght"]
var shipNumber = ["1", "1", "1", "1", "1"]
shipType = ["Aircraft Carrier", "Battleship", "Submarine", "Destroyer", "Patrol Boat"]
shipLength = ["5", "4", "3", "3", "2"]
var DOMAIN =" https://batttleship.herokuapp.com"
var API =" https://batttleship.herokuapp.com:8080"
// "http://localhost:8080";

const urlParams = new URLSearchParams(window.location.search);
const myParam = urlParams.get('gp');
var horizontal = true;

function refresh(){
    window.location.href= DOMAIN + "/web/game.html?gp="+myParam
}

fetch(API + "/api/game_view/" + myParam
).then(function (response) {
    if (response.status == 401) {
        alert("401")
    }
    return response.json();
}).then(function (response) {
    info = response;
    getShipLocation();
    getEmail();
    calculateHit();
  
  
    if(info.state!="Place Ship"){
        console.log("--------------salvo if------------------>")
        createSalvoGrid();   
        getPlayerSalvo();
        document.getElementById("SalvoButton").style.display='inline';
        document.getElementById("saveShipButton").style.display='none';   
    } else {
        document.getElementById("saveShipButton").style.display='inline';   
        document.getElementById("SalvoButton").style.display='none';
    }
    if(info.state=="Wait"){
        document.getElementById("SalvoButton").style.display='none';
        alert("Wait other player")
        setInterval(function(){
            refresh();
        }, 10000)
     } 
    if(info.state=="Enter Salvo"){
        document.getElementById("SalvoButton").style.display='inline';
     }
// if(info.state="Game Over"){
//          alert(" Game Over")
     
// }
    selectcellSalvo();
}).catch(function (error) {
    console.log("Request failed: " + error.message);
}
)
renderHeaders();
renderRows();
renderHeadersShip();
renderRowsShip();
fillCell();
selectcell();
//selectcellSalvo();

//map sirayla elemanlari gezer.
//Ship Grid
function getHeaderHtml() {
    return "<tr><th></th>" + letter.map(function (letter) {
        return "<th>" + letter + "</th>";
    }).join("") + "</tr>";
}
function renderHeaders() {
    var html = getHeaderHtml();
    document.getElementById("table-headers").innerHTML = html;
}
function getColumnsHtml() {
    return number.map(function () {
        return "<td>" + " " + "</td>";
    }).join("")
}
function getRowsHtml() {
    return number.map(function (number) {
        return "<tr><th>" + number + "</th>" + getColumnsHtml() + "</tr>";
    }).join("");
}
function renderRows() {
    var html = getRowsHtml();
    document.getElementById("table-rows").innerHTML = html;
}

function getShipLocation() {
    for (var i = 0; i < info.ship.length; i++) {
        for (var k = 0; k < info.ship[i].locations.length; k++) {
            var shipLocation = info.ship[i].locations[k];
            putShipImage(document.getElementById("gameTable").rows[indexRow(shipLocation)].cells[indexCell(shipLocation)], info.ship[i].shipType);
        }
    }
}
//n=H3
function indexCell(n) {
    var cellsnumber = parseInt(letter.indexOf(n.charAt(0).toString()) + 1)
    return cellsnumber;
}
function indexRow(n) {
    var rowsnumber = n.substring(1);
    return rowsnumber;
}

function getEmail() {
    var email = "";
    for (var i = 0; i < info.gamePlayers.length; i++) {
        if (info.id == info.gamePlayers[i].id) {
            email = info.gamePlayers[i].player.email;
            document.getElementById("playerName1").innerHTML = info.gamePlayers[i].player.firstName + " " + info.gamePlayers[i].player.lastName
            document.getElementById("gameinformation1").innerHTML = email + " " + "(you)"
        }
        else {
            email = info.gamePlayers[i].player.email;
            document.getElementById("playerName2").innerHTML = info.gamePlayers[i].player.firstName + " " + info.gamePlayers[i].player.lastName
            document.getElementById("gameinformation2").innerHTML = email;
        }
    }
}
//Salvo Grid
function createSalvoGrid() {
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

var turn=0;
function getPlayerSalvo() {
    var currentPlayerId;
    for(var i = 0; i< info.gamePlayers.length; i++){
        if(info.gamePlayers[i].id == info.id){
            currentPlayerId = info.gamePlayers[i].player.id;
            // console.log("Ben: " + currentPlayerId);
        }
    }
    for (var i = 0; i < info.salvoes.length; i++) {
        turn = info.salvoes.length;
        for (var k = 0; k < info.salvoes[i].locations.length; k++) {
            var salvoLocation = info.salvoes[i].locations[k];
            
            if (currentPlayerId == info.salvoes[i].player) {
                // console.log("---row---"+indexRow(salvoLocation) + "----cell---"+indexCell(salvoLocation));
                document.getElementById("gameTableSalvo")
                .rows[indexRow(salvoLocation)]
                .cells[indexCell(salvoLocation)].style.backgroundColor = "#426585";
            } else {
                var isHit = false;
                info.ship.forEach(s => {
                    if (!isHit) {
                        if (s.locations.includes(info.salvoes[i].locations[k])) {
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].innerHTML ='<img src="giphy.gif"height=30px width=40px ></img>' 
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor = "#78C0EF";                            
                            isHit = true;
                        } else {
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor = "#DC7E4C";
                        }
                    }
                });
            }
        }
    }
}
//shipTable

function getHeaderHtmlShip() {
    return "<tr><th>Number</th> " + shipHeader.map(function (shipHeader) {
        return "<th>" + shipHeader + "</th>";
    }).join("") + "</tr>";
}
function renderHeadersShip() {
    var html = getHeaderHtmlShip();
    document.getElementById("table-headers-ship").innerHTML = html;
}
function getColumnsHtmlShip() {
    return shipHeader.map(function () {
        return "<td>" + "" + "</td>";
    }).join("")
}
function getRowsHtmlShip() {
    return shipNumber.map(function (shipNumber) {
        return "<tr><th>" + shipNumber + "</th>" + getColumnsHtmlShip() + "</tr>";
    }).join("");
}

function renderRowsShip() {
    var html = getRowsHtmlShip();
    document.getElementById("table-rows-ship").innerHTML = html;
}

//Ship Information Table
function fillCell() {
    var maps = new Map();
    for (var k = 0; k < shipType.length; k++) {
        maps.set(shipType[k], shipLength[k]);
    }
    var i = 1;
    for (let [shipType, shipLength] of maps.entries()) {
        document.getElementById("gameTableShip").rows[i].cells[1].innerHTML = shipType;
        document.getElementById("gameTableShip").rows[i].cells[2].innerHTML = shipLength;
        i = i + 1;
    }

}

// putship
function saveShip() {
    console.log(">>>>"+JSON.stringify(shipList))
    const urlParams = new URLSearchParams(window.location.search);
    const gpIdShip = urlParams.get('gp');
if(shipList.length==5){
    var body = shipList
}else{
    alert("you should put 5 ships")
}
    fetch(API + "/api/games/players/" + gpIdShip + "/ships", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(body)
    })
        .then(function (response) {
            refresh();
        })
}

//Get list of rows in the table
var table = document.getElementById("gameTableShip");
var rows = table.getElementsByTagName("tr");
var selectedRow;
var selectedShipLength;
var numberOfSelectedShip;
var shipType;
var shipList=[];
var shipListInfo;
var selectedShipOnShipTable;

function selectRow(row) {
    if (selectedRow !== undefined) {
        selectedRow.style.backgroundColor = "transparent";
    }
    if (selectedRow = row) {
        selectedRow.style.backgroundColor = "#A9E2F3";
        for (var i = 1; i < table.rows.length; i++) {
            if (selectedRow.rowIndex == i && table.rows[i].cells[0].innerHTML != 0) {
                selectedShipLength = table.rows[i].cells[2].innerHTML
                shipType=table.rows[i].cells[1].innerHTML
                numberOfSelectedShip = table.rows[i].cells[0].innerHTML;
                selectedShipOnShipTable = table.rows[i].cells[0];
            }
        }
    }
}
var arr = Array.from(rows);
arr.map(function (index) {
    addEvent(index, "click", function () { selectRow(this) })
})
function addEvent(element, evt, callback) {
    if (element.addEventListener) {
        element.addEventListener(evt, callback, false);
    }
}
function selectcell() {
    var table = document.getElementById("gameTable");

    if (table != null) {
        for (var i = 1; i < table.rows.length; i++) {
            for (var j = 1; j < table.rows[i].cells.length; j++) {
                //i=MouseEvent
                table.rows[i].cells[j].onclick = function (i) {
                    clickedCells(this, i);

                }
            }
        }
    }
}

var rowId;
var cellId;
//create obje
function shipListAllInfo(shiptype,locations){
    this.shipType=shipType;
    this.locations=locations;

}
function clickedCells(clickedCell, i) {
    horizontal = true; 
    //geminin ilk pozisyonundan dolayi, yatay=true diyoruz. returnShipButtton ile gemi donduruldugunde ve yeni gemi kondugunda tekrar yatay pozisyonu tekrar set ediyoruz.
   
    //horizantal green
    var table = document.getElementById("gameTable");
    if (numberOfSelectedShip > 0) {
        rowId = i.path[1].getElementsByTagName("th")[0].innerHTML;
        cellId = clickedCell.cellIndex;
    }
   //control green(if there is a green cell dont paint)
    for (var k = 0; k < selectedShipLength; k++) {
        if (table.rows[rowId].cells[cellId + k].style.backgroundColor =="#78C0EF") {
            return;
        }
    }

    if (selectedShipOnShipTable != null && numberOfSelectedShip > 0 && parseInt(cellId) + parseInt(selectedShipLength) <= 11) {
        var shiplocation=[]
        for (var k = 0; k < selectedShipLength; k++) {
        
            putShipImage(table.rows[rowId].cells[cellId + k], shipType);
           // table.rows[rowId].cells[cellId + k].style.backgroundColor = 'green';
            numberOfSelectedShip = parseInt(numberOfSelectedShip) - 1;
            selectedShipOnShipTable.innerHTML = 0;
            //shipCellInfo as A3
            shipCellInfo= letter[cellId +k-1 ] + rowId
           shiplocation.push(shipCellInfo) 
        }
         
        shipListInfo = new shipListAllInfo(shipType,shiplocation)
        if(shipListInfo != null){
            shipList.push(shipListInfo);
        }
    }
}

function returnShipButton() {
    var table = document.getElementById("gameTable");
    if (parseInt(rowId) + parseInt(selectedShipLength) > 11) {
        return;
    }
    if (horizontal == true) {
        //control green(if there is a green cell dont paint)
        for (var k = 1; k < selectedShipLength; k++) {
            if(table.rows[parseInt(rowId) + k].cells[cellId].style.backgroundColor == "#78C0EF"){
               // table.rows[parseInt(rowId) + k].cells[cellId].innerHTML="1";
                return;
            }
        }

        var shiplocation=[]
        for (var k = 0; k < selectedShipLength; k++) {
            table.rows[rowId].cells[cellId + k].style.backgroundColor = 'transparent';
           // table.rows[parseInt(rowId) + k].cells[cellId].style.backgroundColor = 'green';
           table.rows[rowId].cells[cellId + k].innerHTML=null
            putShipImage(table.rows[parseInt(rowId) + k].cells[cellId],shipType)
            shipCellInfo= letter[cellId-1] + (parseInt(rowId)+k)
            
           shiplocation.push(shipCellInfo)
           console.log(shipCellInfo + "-------h--------"+ shiplocation);

        }
        horizontal = false;
        
        shipListInfo = new shipListAllInfo(shipType,shiplocation)
        shipList.pop();//son elemani cikartir
        shipList.push(shipListInfo);
    } else {
        // console.log("-------vert--------");

         //control green(if there is a green cell dont paint)
         for (var k = 1; k < selectedShipLength; k++) {
            if(table.rows[rowId].cells[cellId + k].style.backgroundColor == '#78C0EF'){
                return;
            }
        }
        var shiplocation=[]
        for (var k = 0; k < selectedShipLength; k++) {
            //table.rows[rowId].cells[cellId + k].style.backgroundColor = 'green';
            putShipImage(table.rows[rowId].cells[cellId + k],shipType)
            table.rows[parseInt(rowId) + k+1].cells[cellId].style.backgroundColor = 'transparent';
            table.rows[parseInt(rowId) + k+1].cells[cellId].innerHTML=null;
            shipCellInfo= letter[cellId +k-1] +parseInt(rowId)
            
           shiplocation.push(shipCellInfo)
           console.log(shipCellInfo + "--------v-------"+ shiplocation);
        }
        horizontal = true;
        shipListInfo = new shipListAllInfo(shipType,shiplocation)
        shipList.pop(); //son elemani cikartir
        shipList.push(shipListInfo);
    }
}
//SALVO

var salvo=0;
function selectcellSalvo() {
    var table = document.getElementById("gameTableSalvo");
    if (table != null) {
        for (var i = 1; i < table.rows.length; i++) {
            for (var j = 1; j < table.rows[i].cells.length; j++) {
                //i=MouseEvent
                // console.log(table.rows[i].cells[j])
                table.rows[i].cells[j].onclick = function (i) {
                    clickedCellsSalvo(this, i);
                }
            }
        }
    }
}

salvoList=[];
function clickedCellsSalvo(clickedCell, i) {
    if(salvo<5){
        var rowId = i.path[1].getElementsByTagName("th")[0].innerHTML;
        var cellId = clickedCell.cellIndex;
        var l = letter[cellId-1] + (parseInt(rowId));
        
        if(clickedCell.style.backgroundColor != ''){
            alert("Cannot selectable!")
        } else {
            salvoList.push(l);
            clickedCell.style.backgroundColor = '#DC7E4C';
            salvo= salvo+1;
        }
    }
}

function salvoBody(turn,locations){
    this.turn=turn;
    this.locations=locations;
}

function saveSalvo() {
    if(info.ship == 'undefined' || info.ship.length == 0 || info.ship.length < 0){
        alert("You should put your 5 ships first");
        return;
    }
    const salvogp = new URLSearchParams(window.location.search);
    const gp = salvogp.get('gp');
    if(salvoList.length==5){
    var body = new salvoBody(turn+1,salvoList)
    }
    else{
        alert("You should enter 5 salvo")
    }

    fetch(API+"/api/games/players/" + gp + "/salvos", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(body)
    })
        .then(function (response) {
           refresh();
        })
}
var lookup = new Map();
function calculateHit(){

    info.history.forEach(hist=>{
        if(hist.type=="Aircraft Carrier"){ 
            merge(hist)
        }
        if(hist.type=="Battleship"){ 
            merge(hist)
        }
        if(hist.type=="Submarine"){ 
            merge(hist)
        }
        if(hist.type=="Destroyer"){ 
            merge(hist)
        }
        if(hist.type=="Patrol Boat"){ 
            merge(hist)
        }
    })
    paintHitShip()
}

function merge(hist){
    if(!lookup.has(hist.type)){
        lookup.set(hist.type, hist.hit)
    } else {
        var oldhist= lookup.get(hist.type)
        lookup.set(hist.type, oldhist+hist.hit)
    }
}

function paintHitShip(){

    for (let [type,hit] of lookup.entries()) {
        console.log("******t*******"+type)
        console.log("******h*******"+hit)

        if(type=="Aircraft Carrier"){
            for(var i=1;i<=hit;i++){
                document.getElementById("hitShipTable").rows[0].cells[i].style.backgroundColor="#DC7E4C";
            }
        }

        if(type=="Battleship"){
            for(var i=1;i<=hit;i++){
                document.getElementById("hitShipTable").rows[1].cells[i].style.backgroundColor="#DC7E4C";
            }
        }
        if(type=="Submarine"){
            for(var i=1;i<=hit;i++){
                document.getElementById("hitShipTable").rows[2].cells[i].style.backgroundColor="#DC7E4C";
            }
        }
        if(type=="Destroyer"){
            for(var i=1;i<=hit;i++){
                document.getElementById("hitShipTable").rows[3].cells[i].style.backgroundColor="#DC7E4C";
            }
        }
        if(type=="Patrol Boat"){
            for(var i=1;i<=hit;i++){
                document.getElementById("hitShipTable").rows[4].cells[i].style.backgroundColor="#DC7E4C";
            }
        }
    }


}


//  function paintHitShip(){
//      for(var i=0;i<info.history.length;i++){
//             var t=null;
//             for(var j=1;j<= info.history[i].hit;j++){
//                 if(info.history[i].type=="Aircraft Carrier"){ 
//                     if(t == null){
//                         t = availableRed(0); 
//                 console.log("Aircraft Carrier")   
//                     }     
//                     document.getElementById("hitShipTable").rows[0].cells[j+t].style.backgroundColor="#DC7E4C";
//                 }
//                 if(info.history[i].type=="Battleship"){ 
//                     if(t == null){
//                         t = availableRed(1);  
//                         console.log("battleship")   
//                     }            
//                     document.getElementById("hitShipTable").rows[1].cells[j+t].style.backgroundColor="#DC7E4C";
//                 }
//                 if(info.history[i].type=="Submarine"){    
//                     if(t == null){
//                         t = availableRed(2);    
//                     } 
//                     document.getElementById("hitShipTable").rows[2].cells[j+t].style.backgroundColor="#DC7E4C";
//                 }
//                 if(info.history[i].type=="Destroyer"){  
//                     if(t == null){
//                         t = availableRed(3);    
//                     }    
//                     document.getElementById("hitShipTable").rows[3].cells[j+t].style.backgroundColor="#DC7E4C";
//                 }
//                 if(info.history[i].type=="Patrol Boat"){
//                     if(t == null){
//                         t = availableRed(4);    
//                     }     
//                     document.getElementById("hitShipTable").rows[4].cells[j+t].style.backgroundColor="#DC7E4C" ;
//                 }
//             }
//      }
//  }
//  function availableRed(rowIndex){
//      for(var i=1;i<=5;i++){
//         if(document.getElementById("hitShipTable").rows[rowIndex].cells[i].style.backgroundColor!='red'){
//             return i-1;
//         }
//     }
//  }
 function putShipImage(cell, shipType){
    if( shipType=="Aircraft Carrier"){
        cell.innerHTML='<img src="Aircraft carrier.png"height=20px width=30px></img>'
        cell.style.backgroundColor = "#78C0EF"
    }
    if( shipType=="Battleship"){
        cell.innerHTML='<img src="Battleship.png"height=20px width=30px></img>'
        cell.style.backgroundColor = "#78C0EF"
    }
    if( shipType=="Submarine"){
        cell.innerHTML='<img src="Submarine.png"height=20px width=30px></img>'
        cell.style.backgroundColor = "#78C0EF"
    }
    if( shipType=="Destroyer"){
        cell.innerHTML='<img src="Destroyer.png"height=20px width=30px></img>'
        cell.style.backgroundColor = "#78C0EF"
    }
    if( shipType=="Patrol Boat"){
        cell.innerHTML='<img src="Patrol Boat.png"height=20px width=30px</img>'
        cell.style.backgroundColor = "#78C0EF"
    }
 }
