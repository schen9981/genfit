<#assign content>
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/frontpage.css">
    <script>
        $(".sidenav").remove();
        if (getLoginStatus()) {
            window.location.replace("/user");
        }
    </script>

    <div class="container">
        <div id="left-wrapper">
            <h1>Welcome to GenFit</h1>
            <h3>Feature One</h3>
            <h3>Feature Two</h3>
            <h3>Feature Three</h3>
        </div>

        <div id="right-wrapper">
            <div id="login-wrapper">
                <h2>Log In</h2>
                <form id = "login-form" method="POST" action="home">
                    <input id = "loginemail" type="email" placeholder="Email" required>
                    <input id = "loginpwd" type="password" placeholder="Password" required>
                    <input class="btn" id="login" type="submit" value="Log In">
                </form>
            </div>

            <div id="signup-wrapper">
                <h2>Sign Up</h2>
                <form id="signup-form" method="POST" action="home" autocomplete="off">
                    <input id="signupname" type="text" placeholder="Name" required>
                    <input id ="signupemail" type="email" placeholder="Email" required>
                    <input id ="signuppwd" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" placeholder="Password" required>
                    <input class="btn" id="signup" type="submit" value="Sign Up">
                </form>


                <div id="message">
                    <h3>Password must contain the following:</h3>
                    <p id="letter" class="invalid">A <b>lowercase</b> letter</p>
                    <p id="capital" class="invalid">A <b>capital (uppercase)</b> letter</p>
                    <p id="number" class="invalid">A <b>number</b></p>
                    <p id="length" class="invalid">Minimum <b>8 characters</b></p>
                </div>
            </div>
        </div>
    </div>
</#assign>
<#include "main.ftl">
<script src="js/user-form.js"></script>