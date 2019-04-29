<#assign content>
<h1>Items in User's Outfit</h1>

<div id="outfits">
</div>

<div id="add">
  <button id="addOutfit">Add Outfit</button>

  <div id="addOutfitModal" class="modal">
    <div class="modal-content">
      <span id="addSpan" class="close">&times;</span>
      <form id="addOutfitForm">
        Item Description:<br>
        <input type="text" id="item-name" name="item-name"><br>

        Color(s):<br>
        <div id="item-colors">
          <input type="color" id="item-color" name="item-color" value="#ff0000"><br>
        </div>
        <button id="addColor">+</button><br>

        Type:<br>
        <select id="type-1" name="item-type-1">
          <option value="OUTER">Outerwear</option>
          <option value="TOP">Top</option>
          <option value="BOTTOM">Bottom</option>
          <option value="SHOES">Shoes</option>
        </select>
        <select id="type-2" name="item-type-2">
          <option value="outer-coat">Outer Coat</option>
          <option value="suit">Suit</option>
        </select><br>

        Pattern:<br>
        <select id="item-pattern" name="item-pattern">
          <option value="SOLID">Solid</option>
          <option value="STRIPED">Striped</option>
          <option value="CHECKERED">Checkered</option>
          <option value="OTHER">Other</option>
        </select><br>

        Season:<br>
        <select id="item-season" name="item-season">
          <option value="SPRING">Spring</option>
          <option value="SUMMER">Summer</option>
          <option value="FALL">Fall</option>
          <option value="WINTER">Winter</option>
        </select><br>

        Formality:<br>
        <select id="item-formality" name="item-formality">
          <option value="FORMAL">Formal</option>
          <option value="BUSINESS_CASUAL">Business Casual</option>
          <option value="CASUAL">Casual</option>
          <option value="ULTRA_CASUAL">Ultra Casual</option>
        </select><br>

        <input type="submit" value="Submit">
      </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
