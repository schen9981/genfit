$(document).ready(() => {
  let name = localStorage.getItem('name');
  let welcomeHTML = '<h3>Welcome, ' + name + '</h3>';

  $("#welcome-message").html(welcomeHTML);
});
