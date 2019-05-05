<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <#if title?has_content><title>${title}</title></#if>
    <link rel="icon" href="/img/logo.png">
    <link rel="shortcut icon" href="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/normalize.css">
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/html5bp.css">
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/main.css">
    <link href="https://fonts.googleapis.com/css?family=Rubik" rel="stylesheet">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.1/css/all.css" integrity="sha384-50oBUHEmvpQ+1lW4y57PTFmhCaXp0ML5d60M1M7uH2+nqUivzIebhndOJK28anvf" crossorigin="anonymous">

    <#if additionalCSS?has_content>
        ${additionalCSS}
    </#if>
</head>
<body>
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="<#if cssPath?has_content>${cssPath}</#if>js/jquery-2.1.1.js"></script>
<script src="<#if cssPath?has_content>${cssPath}</#if>js/main.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"></script>
<script src="/js/jquery-2.1.1.js"></script>
<script src="js/authentication.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.bundle.js"></script>
<script src="https://sdk.amazonaws.com/js/aws-sdk-2.283.1.min.js"></script>

<#if additionalScripts?has_content>
    ${additionalScripts}
</#if>

</body>
<!-- Top header -->
<div class="top">
    <img id="logo" src="/img/logo.svg"/>
    <h1> GENFIT </h1>
    <div id='welcome-message'>
    </div>
</div>

<!-- Side navigation -->
<div class="bottom">
    <div class="sidenav">
        <div class="sidenav-tab" id="first">
            <img id="dashboard-img" src="/img/dashboard.svg"/>
            <a id='user-tab-button' href="/user">Dashboard</a>
        </div>

        <div class="sidenav-tab">
            <img id="items-img" src="/img/items.svg"/>
            <a id='items-tab-button'href="/items">Items</a>
        </div>

        <div class="sidenav-tab">
            <img id="outfits-img" src="/img/closet.svg"/>
            <a id='outfits-tab-button'href="/outfits">Outfits</a>
        </div>

        <div class="sidenav-tab" id="last">
            <img id="discover-img" src="/img/discover.svg"/>
            <a id='discover-tab-button'href="/discover">Discover</a>
        </div>

        <div class="signout-div">
            <a id="signout-img" href="/" onclick="logout()">Sign Out</a>
        </div>

    </div>

    <!-- Page content -->
    <div class="main">
        ${content}
    </div>
</div>

</html>
