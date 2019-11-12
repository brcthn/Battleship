var number= ["1","2","3","4","5","6","7","8","9","10"]
var letter= ["A","B","C","D","E","F","G","H","I","J"]
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