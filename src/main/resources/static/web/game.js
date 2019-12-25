var number = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
var letter = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]
var info = [];
var shipHeader = ["Type", "Lenght"]
var shipNumber = ["1", "1", "1", "1", "1"]
shipType = ["Aircraft Carrier", "Battleship", "Submarine", "Destroyer", "Patrol Boat"]
shipLength = ["5", "4", "3", "3", "2"]

const urlParams = new URLSearchParams(window.location.search);
const myParam = urlParams.get('gp');

console.log("http://localhost:8080/api/game_view/" + myParam)

fetch("http://localhost:8080/api/game_view/" + myParam
).then(function (response) {
    if (response.status == 401) {
        alert("401")
    }
    return response.json();
}).then(function (response) {
    info = response;
    console.log(info)
    getShipLocation();
    getEmail();
    getPlayerSalvo();


}).catch(function (error) {
    console.log("Request failed: " + error.message);
}
)
renderHeaders();
renderRows();
createSalvoGrid();
renderHeadersShip();
renderRowsShip();
fillCell();
selectcell();


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
        for (var k = 0; k < info.ship[i].ships.length; k++) {
            var shipLocation = info.ship[i].ships[k];
            document.getElementById("gameTable").rows[indexRow(shipLocation)].cells[indexCell(shipLocation)].style.backgroundColor = "#8296a1"
        }
    }
}
//n=H3
function indexCell(n) {
    var cellsnumber = parseInt(letter.indexOf(n.charAt(0).toString()) + 1)
    return cellsnumber;
}
function indexRow(n) {
    var rowsnumber = (n.charAt(1));
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

            console.log(email)
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

function getPlayerSalvo() {
    for (var i = 0; i < info.salvoes.length; i++) {
        for (var k = 0; k < info.salvoes[i].locations.length; k++) {
            var salvoLocation = info.salvoes[i].locations[k];
            if (info.id == info.salvoes[i].player) {
                document.getElementById("gameTableSalvo").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor = "#426585";
            } else {
                var isHit = false;
                info.ship.forEach(s => {
                    if (!isHit) {
                        if (s.ships.includes(info.salvoes[i].locations[k])) {
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor = "#000000";
                            isHit = true;
                        } else {
                            document.getElementById("gameTable").rows[indexRow(salvoLocation)].cells[indexCell(salvoLocation)].style.backgroundColor = "#dc6900";
                        }
                    }
                });
            }
        }
    }
}
//shipTable

// var shipHeader=["Number","Type","Lenght"]
// var shipType=["1","1","1","1","1"]
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
        console.log(maps)
    }
    var i = 1;
    for (let [shipType, shipLength] of maps.entries()) {
        document.getElementById("gameTableShip").rows[i].cells[1].innerHTML = shipType;
        document.getElementById("gameTableShip").rows[i].cells[2].innerHTML = shipLength;
        i = i + 1;
    }

}

// putship
function putShip() {
    const urlParams = new URLSearchParams(window.location.search);
    const gpIdShip = urlParams.get('gp');

    var body = [{ "shipType": "destroyer", "locations": ["A1", "B1", "C1"] }]
    fetch("http://localhost:8080/api/games/players/" + gpIdShip + "/ships", {
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify(body)
    })
        .then(function (response) {
            // console.log(response)
            return response;
        })
}

//Get list of rows in the table
var table = document.getElementById("gameTableShip");
var rows = table.getElementsByTagName("tr");
var selectedRow;
var selectedShipLength;
var numberOfSelectedShip;
function selectRow(row) {
    if (selectedRow !== undefined) {
        selectedRow.style.backgroundColor = "transparent";
    }
    if (selectedRow = row) {
        selectedRow.style.backgroundColor = "#A9E2F3";
        for (var i = 1; i < table.rows.length; i++) {
            if (selectedRow.rowIndex == i &&  table.rows[i].cells[0].innerHTML!=0){
                selectedShipLength = table.rows[i].cells[2].innerHTML
                numberOfSelectedShip = table.rows[i].cells[0].innerHTML;
                table.rows[i].cells[0].innerHTML=0
                
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

function clickedCells(clickedCell, i) {
    //horizantal green
    var table = document.getElementById("gameTable");
    if(numberOfSelectedShip>0){
        rowId = i.path[1].getElementsByTagName("th")[0].innerHTML;
        cellId = clickedCell.cellIndex;
    }
    if (selectedShipLength != null && numberOfSelectedShip>0 && parseInt(cellId) + parseInt(selectedShipLength) <= 11) {
        for (var k = 0; k < selectedShipLength; k++) {
            table.rows[rowId].cells[cellId + k].style.backgroundColor = 'green';
            numberOfSelectedShip = parseInt(numberOfSelectedShip)-1;
        }

    }
}

var horizontal = true;
function returnShipButton() {
    var table = document.getElementById("gameTable");
    if (parseInt(rowId) + parseInt(selectedShipLength) > 11) {
        return;
    }
    if (horizontal == true) {
        for (var k = 0; k < selectedShipLength; k++) {
            table.rows[rowId].cells[cellId + k].style.backgroundColor = 'transparent';
            table.rows[parseInt(rowId) + k].cells[cellId].style.backgroundColor = 'green';
        }
        horizontal = false;
    } else {
        for (var k = 1; k < selectedShipLength; k++) {
            table.rows[rowId].cells[cellId + k].style.backgroundColor = 'green';
            table.rows[parseInt(rowId) + k].cells[cellId].style.backgroundColor = 'transparent';
        } 
        horizontal = true;

    }

}
