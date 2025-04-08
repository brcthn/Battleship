// var data=0;
// function loginFetch(){
//     const email = document.getElementById("username").value
//     const password = document.getElementById("password").value

//    fetch("https://battleship-azure.vercel.app/api/login?"+"email="+email+"&"+"password="+password,{
//    method:'POST'})
//    .then(function(response){
//       console.log("================> " + response.status);
//       if(response.status==200){
//         //redirect
//         window.location.href = "https://battleship-azure.vercel.app/web/games.html";
//       }else{
//       alert("ad,lsf,")
//       }
//     }).catch(function(error){
//       console.log("Request failed: " + error.message);
//     })
// }

// function login() {
//     var form = document.getElementById('login-form');

//   $.post("/api/login", {
//       username: form["username"].value,
//       password: form["password"].value
//     })
//     .done(function () {
//       console.log("logged in!");
//     })
//     .fail(function () {
//       console.log("failed to log in!");
//     });
// }

// function logout() {

//   $.post("/api/logout")
//     .done(function () {
//         console.log("logged in!");
//       })
//     .fail(function () {
//         console.log("failed to log in!");});
// }
