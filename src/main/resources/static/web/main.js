login();
logout();
function login(evt) {
    evt.preventDefault();
    var form = evt.target.form;
    $.post("/app/login", 
           { name: form["username"].value,
             pwd: form["password"].value })
     .done(console.log("logged in!"))
     .done(function() { console.log("logged in!")})
     .fail(function() { console.log("logged out!")});
  }
  
  function logout(evt) {
    evt.preventDefault();
    $.post("/app/logout")
     .done(function() {console.log("logged in!")})
     .fail(function() { console.log("logged out!")});
  }