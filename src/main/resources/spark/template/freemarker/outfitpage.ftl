<#assign content>
  <h1>Outfits in <span class="name"></span>'s Closet</h1>

<div id="outfits">
</div>

<div id="add">
  <button id="constructOutfit">Construct Full Outfit</button>
  <button id="suggestOutfit">Get Outfit Suggestions</button>

  <div id="addOutfitModal" class="modal">
    <div class="modal-content">
      <span id="addSpan" class="close">&times;</span>
      <form id="addOutfitForm">
        <div class="tab" id="outfit-info">
          Outfit Name:<br>
          <input type="text" id="outfit-name" name="outfit-name"><br>
          <div class="add" id="outer-item">
            <button id="add-outer-item" onclick="navigateToTab(event, 1)">Add Outer</button>
          </div>
          <div class="add" id="top-item">
            <button id="add-top-item" onclick="navigateToTab(event, 2)">Add Top</button>
          </div>
          <div class="add" id="bottom-item">
            <button id="add-bottom-item" onclick="navigateToTab(event, 3)">Add Bottom</button>
          </div>
          <div class="add" id="shoes-item">
            <button id="add-shoes-item" onclick="navigateToTab(event, 4)">Add Shoes</button>
          </div>
        </div>

        <div class="tab" id="outer-select">
          <div class="description">
            <p>Select an outer:</p><br>
          </div>
        </div>

        <div class="tab" id="top-select">
          <div class="description">
            <p>Select a top:</p><br>
          </div>
        </div>

        <div class="tab" id="bottom-select">
          <div class="description">
            <p>Select a bottom:</p><br>
          </div>
        </div>

        <div class="tab" id="shoes-select">
          <div class="description">
            <p>Select a pair of shoes:</p><br>
          </div>
        </div>

        <button id="addOutfit">Add Outfit</button>

        <div style="overflow:auto;">
        <div style="float:right;">
          <button type="button" id="addItem" onclick="addItemToOutfit(event)">Add to Outfit</button>
          <button type="button" id="back" onclick="navigateToTab(event, 0)">Back</button>
        </div>
        </div>
      </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
