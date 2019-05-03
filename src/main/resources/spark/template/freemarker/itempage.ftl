<#assign content>
<h1>Items in <span class="name"></span>'s Closet</h1>

<div id="items">
</div>

<div id="add">
  <button id="addItem">Add Item</button>

  <div id="addItemModal" class="modal">
    <div class="modal-content">
      <span id="addSpan" class="close">&times;</span>
                  <form id="addItemForm">
                      <h3>Item Description: </h3>
                      <input type="text" id="item-name" name="item-name" required><br>

                      <h3>Color(s): </h3>
                      <div id="item-colors" style="display:inline-block;">
                          <input type="color" id="item-color" name="item-color" value="#ff0000"><br>
                      </div>
                      <button id="addColor">+</button><br>

                      <h3>Type: </h3>
                      <select id="type-1" name="item-type-1">
                          <option value="OUTER">Outerwear</option>
                          <option value="TOP">Top</option>
                          <option value="BOTTOM">Bottom</option>
                          <option value="SHOES">Shoes</option>
                      </select>
                      <select id="type-2" name="item-type-2">
                          <option value="OUTER_COAT">Outer Coat</option>
                          <option value="SUIT">Suit</option>
                      </select><br>

                      <h3>Pattern: </h3>
                      <select id="item-pattern" name="item-pattern">
                          <option value="SOLID">Solid</option>
                          <option value="STRIPED">Striped</option>
                          <option value="CHECKERED">Checkered</option>
                          <option value="OTHER">Other</option>
                      </select><br>

                      <h3>Season: </h3>
                      <select id="item-season" name="item-season">
                          <option value="SPRING">Spring</option>
                          <option value="SUMMER">Summer</option>
                          <option value="FALL">Fall</option>
                          <option value="WINTER">Winter</option>
                      </select><br>

                      <h3>Formality: </h3>
                      <select id="item-formality" name="item-formality">
                          <option value="FORMAL">Formal</option>
                          <option value="BUSINESS_CASUAL">Business Casual</option>
                          <option value="CASUAL">Casual</option>
                          <option value="ULTRA_CASUAL">Ultra Casual</option>
                      </select><br>

                      <h3>Image Upload: </h3>
                      <input id="image-input" type="file" onchange="previewFile()"><br>
                      <img id="image-preview" src="" alt=""><br>

                      <input type="submit" value="Submit">
                  </form>
    </div>
  </div>
</div>

</#assign>
<#include "main.ftl">
