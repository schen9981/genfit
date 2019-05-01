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
        <div class="tab-add" id="outfit-info">
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

        <div class="tab-add" id="outer-select">
          <div class="description">
            <p>Select an outer:</p><br>
          </div>
        </div>

        <div class="tab-add" id="top-select">
          <div class="description">
            <p>Select a top:</p><br>
          </div>
        </div>

        <div class="tab-add" id="bottom-select">
          <div class="description">
            <p>Select a bottom:</p><br>
          </div>
        </div>

        <div class="tab-add" id="shoes-select">
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

  <div id="suggestOutfitModal" class="modal">
    <div class="modal-content">
      <span id="suggestSpan" class="close">&times;</span>
      <form id="suggestOutfitForm">
        <div class="tab-suggest" id="outfit-info">
          <div class="add-suggest" id="outer-item">
            <button id="suggest-outer-item" onclick="navigateToSuggestTab(event, 1)">Add Outer</button>
          </div>
          <div class="add-suggest" id="top-item">
            <button id="suggest-top-item" onclick="navigateToSuggestTab(event, 2)">Add Top</button>
          </div>
          <div class="add-suggest" id="bottom-item">
            <button id="suggest-bottom-item" onclick="navigateToSuggestTab(event, 3)">Add Bottom</button>
          </div>
          <div class="add-suggest" id="shoes-item">
            <button id="suggest-shoes-item" onclick="navigateToSuggestTab(event, 4)">Add Shoes</button>
          </div>
        </div>

        <div class="tab-suggest" id="outer-select">
          <div class="description">
            <p>Select an outer:</p><br>
          </div>
        </div>

        <div class="tab-suggest" id="top-select">
          <div class="description">
            <p>Select a top:</p><br>
          </div>
        </div>

        <div class="tab-suggest" id="bottom-select">
          <div class="description">
            <p>Select a bottom:</p><br>
          </div>
        </div>

        <div class="tab-suggest" id="shoes-select">
          <div class="description">
            <p>Select a pair of shoes:</p><br>
          </div>
        </div>

        <div class="tab-suggest" id="display-suggestions">
        </div>

        <button id="suggest">Generate Suggestions</button>

        <div style="overflow:auto;">
        <div style="float:right;">
          <button type="button" id="addItemSuggest" onclick="addItemToOutfitSuggest(event)">Add to Outfit</button>
          <button type="button" id="backSuggest" onclick="navigateToSuggestTab(event, 0)">Back</button>
        </div>
        </div>
      </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
