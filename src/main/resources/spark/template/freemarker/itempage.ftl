<#assign content>
<h1>Items in User's Closet</h1>

<div id="items">
</div>

<div id="add">
  <button id="addItem">Add Item</button>

  <div id="addItemModal" class="modal">
    <div class="modal-content">
      <span id="addSpan" class="close">&times;</span>
      <form method="POST" action="/addItem">
        Item Description:<br>
        <input type="text" name="item-name"><br>

        Color(s):<br>
        <div id="item-colors">
          <input type="color" name="item-color" value="#ff0000"><br>
        </div>
        <button id="addColor">+</button><br>

        Type:<br>
        <select id="type-1" name="item-type-1">
          <option value="outer">Outerwear</option>
          <option value="top">Top</option>
          <option value="bottom">Bottom</option>
          <option value="shoes">Shoes</option>
        </select>
        <select id="type-2" name="item-type-2">
          <option>Please select a generic item type.</option>
        </select><br>

        Pattern:<br>
        <select name="item-pattern">
          <option value="solid">Solid</option>
          <option value="striped">Striped</option>
          <option value="checkered">Checkered</option>
          <option value="other">Other</option>
        </select><br>

        Season:<br>
        <select name="item-season">
          <option value="spring">Spring</option>
          <option value="summer">Summer</option>
          <option value="fall">Fall</option>
          <option value="winter">Winter</option>
        </select><br>

        Formality:<br>
        <select name="item-formality">
          <option value="formal">Formal</option>
          <option value="business-casual">Business Casual</option>
          <option value="casual">Casual</option>
          <option value="ultra-casual">Ultra Casual</option>
        </select><br>

        <input type="submit" value="Submit">
      </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
