<#assign content>
    <link rel="stylesheet"
          href="<#if cssPath?has_content>${cssPath}</#if>css/discover.css">

    <div id="top-container">
        <h2>You can try these</h2>
        <hr></hr>
<#--        outfit cards go here-->
        <div id="complete-outfits"></div>
    </div>
    <div id="bottom-container">
        <h2>You can make these happen</h2>
        <hr></hr>
<#--        outfit cards go here-->
        <div id="incomplete-outfits"></div>
    </div>
</#assign>
<#include "main.ftl">
<script src="js/discover.js"></script>