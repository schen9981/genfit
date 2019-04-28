if (window.location.pathname !== "/") {
    redirect();
}

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

function logout() {
    localStorage.clear();
}

function getLoginStatus() {
    // true if logged in, false otherwise
    if (localStorage.getItem('username') == null || localStorage.getItem('pwdhash') == null) {
        // console.log("HIIIIIIIIIIII");
        return false;
    } else {
        const postParameters = {
            username : localStorage.getItem('username').toLowerCase(),
            password : localStorage.getItem('pwdhash')
        };
        $.post("/login", postParameters, responseJSON => {
            // Parse response we got from backend
            let responseObject = JSON.parse(responseJSON);
            console.log(responseObject.success);
            return responseObject.success;
        });
        return true;
    }

}

function redirect() {
    if (!getLoginStatus()) {
        window.location.replace("/");
        console.log("redirect");
    }
    // else {
    //     const postParameters = {
    //         username : localStorage.getItem('username').toLowerCase(),
    //         password : localStorage.getItem('pwdhash')
    //     };
    //     $.post("/login", postParameters, responseJSON => {
    //         // Parse response we got from backend
    //         let responseObject = JSON.parse(responseJSON);
    //
    //         let success = responseObject.success;
    //         console.log(success);
    //         if (!success) {
    //             console.log("needs to re-login");
    //             alert("Needs to re-login");
    //             // window.location.replace("/");
    //         }
    //     });
    // }
}