<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <#if title?has_content><title>${title}</title></#if>
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
    <!-- <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/frontpage.css"> -->

    <link href="https://fonts.googleapis.com/css?family=Poppins"
          rel="stylesheet">

    <#if additionalCSS?has_content>
        ${additionalCSS}
    </#if>
</head>
<body>
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="<#if cssPath?has_content>${cssPath}</#if>js/jquery-2.1.1.js"></script>
<script src="/js/bcrypt.min.js"></script>
<script src="/js/jquery-2.1.1.js"></script>
<!-- <script src="js/frontpage.js"></script> -->

<#if additionalScripts?has_content>
    ${additionalScripts}
</#if>

</body>
<!-- Side navigation -->
<div class="sidenav">
  <a href="/user">Profile</a>
  <a href="/items">Closet Items</a>
  <a href="/outfits">User Outfits</a>
  <a href="/discover">Discover</a>
</div>

<!-- Page content -->
<div class="main">
  ${content}
</div>

</html>
