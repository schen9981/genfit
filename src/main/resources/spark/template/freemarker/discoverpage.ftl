<#assign content>
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/discover.css">

    <h1>Community Suggested Outfits for <span class="name"></span></h1>
    <div id="top-container">
        <h2>Outfits with Items From Your Closet</h2>
        <hr></hr>
<#--        outfit cards go here-->
        <div id="complete-outfits"></div>
    </div>
    <div id="bottom-container">
        <h2>Outfits with Additional Items</h2>
        <hr></hr>
<#--        outfit cards go here-->
        <div id="incomplete-outfits"></div>
    </div>
</#assign>
<#include "main.ftl">
<script src="js/discover.js"></script>
