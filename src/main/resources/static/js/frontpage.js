$(document).ready(function() {

    let signUpPassword = document.getElementById("signuppwd");
    let letter = document.getElementById("letter");
    let capital = document.getElementById("capital");
    let number = document.getElementById("number");
    let length = document.getElementById("length");
    // document.getElementById("message").style.display = "none";
    document.getElementById("message").style.display = "none";

    $("#signuppwd").focus(function() {
        document.getElementById("message").style.display = "block";
    });

    $("#signuppwd").blur(function() {
        document.getElementById("message").style.display = "none";
    });


    $("#signuppwd").keyup(event => {
        let lowerCaseLetters = /[a-z]/g;
        if(signUpPassword.value.match(lowerCaseLetters)) {
            letter.classList.remove("invalid");
            letter.classList.add("valid");
        } else {
            letter.classList.remove("valid");
            letter.classList.add("invalid");
        }

        // Validate capital letters
        let upperCaseLetters = /[A-Z]/g;
        if(signUpPassword.value.match(upperCaseLetters)) {
            capital.classList.remove("invalid");
            capital.classList.add("valid");
        } else {
            capital.classList.remove("valid");
            capital.classList.add("invalid");
        }

        // Validate numbers
        let numbers = /[0-9]/g;
        if(signUpPassword.value.match(numbers)) {
            number.classList.remove("invalid");
            number.classList.add("valid");
        } else {
            number.classList.remove("valid");
            number.classList.add("invalid");
        }

        // Validate length
        if(signUpPassword.value.length >= 8) {
            length.classList.remove("invalid");
            length.classList.add("valid");
        } else {
            length.classList.remove("valid");
            length.classList.add("invalid");
        }
    });

    $("#login-form").submit(function(e) {
        e.preventDefault();
        let username = $("#loginemail").val();
        let password = $("#loginpwd").val();
        login(username, password);
    });

    $("#signup-form").submit(function(e) {
        e.preventDefault();
        let name = $("#signupname").val();
        let username = $("#signupemail").val();
        let password = $("#signuppwd").val();
        signup(name, username, password);
    });

    // $(".sidenav").remove();
});
