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

    <link href="https://fonts.googleapis.com/css?family=Poppins"
          rel="stylesheet">

    <#if additionalCSS?has_content>
        ${additionalCSS}
    </#if>
</head>
<body>
${content}
<!-- Again, we're serving up the unminified source for clarity. -->
<script src="<#if cssPath?has_content>${cssPath}</#if>js/jquery-2.1.1.js"></script>

<#if additionalScripts?has_content>
    ${additionalScripts}
</#if>

</body>
<!-- Side navigation -->
<div class="sidenav">
  <a href="#">Profile</a>
  <a href="#">Closet Items</a>
  <a href="#">User Outfits</a>
  <a href="#">Discover</a>
</div>

<!-- Page content -->
<div class="main">
</div>

</html>
