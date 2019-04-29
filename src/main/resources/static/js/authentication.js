let loginform = 0;
let signupform = 1;
let changepwdform = 2;

if (window.location.pathname !== "/") {
    redirect();
}

function hash(password) {
    // let bcrypt = require('bcrypt.min.js');
    // return bcrypt.hashSync(password, bcrypt.genSaltSync(10));

    // let CryptoJS = require(["crypto-js"]);
    // return CryptoJS.AES.encrypt("genfit", password);

    // return CryptoJS.AES.encrypt(password, "cs32genfit");
    return CryptoJS.MD5(password).toString();
}

function login(username, password) {

    let hashPwd = hash(password);
    // let hashPwd = password;
    const postParameters = {
        form : loginform,
        username : username.toLowerCase(),
        password : hashPwd
    };
    $.post("/userform", postParameters, responseJSON => {
        // Parse response we got from backend
        let responseObject = JSON.parse(responseJSON);

        let success = responseObject.success;
        if (!success) {
            alert("Incorrect email or password");
        } else {
            localStorage.setItem("username", username);
            localStorage.setItem("pwdhash", hashPwd);
            localStorage.setItem("name", responseObject.name);
            // redirect to other page
            window.location.replace("/user");
            // console.log("Login Successfully");
            // console.log(localStorage);
        }
    });
}


function signup(name, username, password) {
    let hashPwd = hash(password);
    // let hashPwd = password;
    const postParameters = {
        form : signupform,
        name : name,
        username : username.toLowerCase(),
        password : hashPwd
    };
    $.post("/userform", postParameters, responseJSON => {
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
            form : loginform,
            username : localStorage.getItem('username').toLowerCase(),
            password : localStorage.getItem('pwdhash')
        };
        $.post("/userform", postParameters, responseJSON => {
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
    }
}

function changePwd(username, currentPwd, newPwd) {
    const postParameters = {
        form : loginform,
        username : username.toLowerCase(),
        password : hash(currentPwd)
    };
    $.post("/userform", postParameters, responseJSON => {
        // Parse response we got from backend
        let responseObject = JSON.parse(responseJSON);

        let success = responseObject.success;
        if (!success) {
            alert("Incorrect current password");
        } else {
            const postParameters = {
                form : changepwdform,
                username : username.toLowerCase(),
                password : hash(newPwd)
            };
            $.post("/userform", postParameters, responseJSON => {
                // console.log(JSON.parse(responseJSON).success);
                if (JSON.parse(responseJSON).success) {
                    alert("Password updated!");
                } else {
                    alert("Sorry, try again later!");
                }

            });
        }
    });
}