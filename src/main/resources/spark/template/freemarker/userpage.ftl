<#assign content>
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/userpage.css">
    <h1 class="name"></h1>
    <hr>
    <div class='stats'>
      <div id="overall">
        <div id="outfit-count">
        </div>
        <div id="item-count">
        </div>
      </div>

      <div id="type">
        <div id="outer-count">
        </div>
        <div id="top-count">
        </div>
        <div id="bottom-count">
        </div>
        <div id="shoes-count">
        </div>
      </div>
    </div>
    <div id="item-overview">
        <div class="chart-container" id="chart-1">
            <h3>Item Season Distribution</h3>
            <canvas id="typePie"></canvas>
        </div>
        <div class="chart-container" id="chart-2">
            <h3>Item Formality Distribution</h3>
            <canvas id="formalityRadar"></canvas>
        </div>
    </div>
    <div id="colors-container">
        <h3>Item Color Distribution</h3>
        <div id="clothingGradient"></div>
    </div>

    <hr>
    <h2> User Settings </h2>
    <div id="change-wrapper">
        <button onclick="openForm()" id="change-btn">Change Password</button>
        <div id="form-wrapper">
            <button onclick="closeForm()" id="cancel-btn">Cancle</button>
            <form id="changepwd-form" method="POST" action="home" autocomplete="off">
                <input id ="currentpwd" type="password" placeholder="Current Password" required>
                <input id ="newpwd" type="password" pattern="(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}" placeholder="New Password" required>
                <input class="btn" id="changepwd" type="submit" value="Confirm Password">
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
</#assign>

<#include "main.ftl">
<script src="js/profile.js"></script>
<script src="js/color.js"></script>
<script src="js/user-form.js"></script>
