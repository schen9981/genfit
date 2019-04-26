function hash(password) {
    let bcrypt = require('bcrypt.min.js');
    return bcrypt.hashSync(password, bcrypt.genSaltSync(10));
}

function login(username, password) {

    // let hashPwd = hash(password);
    let hashPwd = password;
    const postParameters = {
        username : username.toLowerCase(),
        password : hashPwd
    };
    $.post("/login", postParameters, responseJSON => {
        // Parse response we got from backend
        let responseObject = JSON.parse(responseJSON);

        let success = responseObject.success;
        if (!success) {
            alert("Wrong email or password");
        } else {
            localStorage.setItem("username", username);
            localStorage.setItem("pwdhash", hashPwd);
            // redirect to other page
            window.location.replace("/user");
            // console.log("Login Successfully");
            // console.log(localStorage);
        }
    });
}


function signup(name, username, password) {
    // let hashPwd = hash(password);
    let hashPwd = password;
    const postParameters = {
        name : name,
        username : username.toLowerCase(),
        password : hashPwd
    };
    $.post("/signup", postParameters, responseJSON => {
        // Parse response we got from backend
        let responseObject = JSON.parse(responseJSON);
        let success = responseObject.success;
        if (!success) {
            alert("Username already exists");
        } else {
            alert("You have signed up successfully!")
        }
    });
}

function getLoginStatus() {
    // true if logged in, false otherwise
    return (localStorage.getItem('username') != null);
}

function redirect() {
    if (!getLoginStatus()) {
        window.location.replace("/");
    } else {
        const postParameters = {
            username : localStorage.getItem('username'),
            password : localStorage.getItem('pwdhash')
        };
        $.post("/login", postParameters, responseJSON => {
            // Parse response we got from backend
            let responseObject = JSON.parse(responseJSON);

            let success = responseObject.success;
            if (!success) {
                alert("Needs to re-login");
                window.location.replace("/");
            }
        });
    }
}