<#assign content>
  <h1>Outfits in <span class="name"></span>'s Closet</h1>

<div id="outfits">
</div>

<div id="add">
  <button id="addOutfit">Add Outfit</button>

  <div id="addOutfitModal" class="modal">
    <div class="modal-content">
      <span id="addSpan" class="close">&times;</span>
      <form id="addOutfitForm">
        <div class="tab">Select an outer:
        </div>

        <div class="tab">Select a top:
        </div>

        <div class="tab">Select a bottom:
        </div>

        <div class="tab">Select a pair of shoes:
        </div>

        <div style="overflow:auto;">
        <div style="float:right;">
          <button type="button" id="addItem" onclick="addItemToOutfit()">Add to Outfit</button>
          <button type="button" id="back" onclick="back()">Back</button>
        </div>
        </div>
      </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
