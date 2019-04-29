<#assign content>
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/userpage.css">
    <h1 id="name">DSFDS</h1>
    <div id="item-overview">
        <h2>Items</h2>
        <p>Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.</p>
    </div>
    <div id="outfit-overview">
        <h2>Outfits</h2>
        <p>Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo.</p>
    </div>

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
