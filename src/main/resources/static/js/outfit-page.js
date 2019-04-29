// map from id -> item array (?)
// let itemCache = new Map([]);

// username of current user
let username;

// generate html content (ie. item information) for card
function generateCardContent(item) {
  // item represented as array [id, name, outer, top, bottom, shoes, season, formality]
  // outfit components (items) saved as id
  let itemContent = '<h5>' + item[1] + '</h5><br>';
  itemContent += '<p>Color:' + item[2] + '</p></br>';
  itemContent += '<p>Type:' + item[3] + '</p></br>';
  itemContent += '<p>Pattern:' + item[4] + '</p></br>';
  itemContent += '<p>Season:' + item[5] + '</p></br>';
  itemContent += '<p>Formality:' + item[6] + '</p></br>';
  return itemContent;
}

// animate modal for popup functionality
function animateOutfitModal(outfitId) {
  // id of the html elements
  let mId = '#modal-' + outfitId;
  let bId = '#item-' + outfitId;
  let sId = '#close-' + outfitId;

  // get the elements of the page
  let modal = $(mId);
  let btn = $(bId);
  let span = $(sId);

  // open modal when button clicked
  btn.click(function() {
    modal.css("display", "block");
  });

  // close modal when user clicks 'x'
  span.click(function() {
    modal.css("display", "none");
  });
}

// generates the modal/cards for each item from a list
function generateCards(listOfOutfits) {
  for (i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html
    let id = item[0];
    let buttonHTML = '<button class="item" id="item-' + id + '">' + id + '</button>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateCardContent(item);
    modalHTML += '<button id="delete-item-' + id + '">Delete Item</button>';
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('#items').append(buttonHTML);
    $('#items').append(modalHTML);

    // add popup functionality to given modal
    animateItemModal(id);

    // add delete button functionality
    deleteUserItem(id);
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
}

// add button functionality to remove an item
function deleteUserItem(itemId) {
  // event handler for removing item
  $('#delete-item-' + itemId).on('click', function(e) {
    let postParams = {
      username: username,
      itemId: itemId
    };

    // post request to remove item
    $.post("/deleteItem", postParams, responseJSON => {
      $('#item-' + itemId).remove();
      $('#modal-' + itemId).remove();
    });

    window.location.reload();
  });
}

// function to retrieve and display user items
function displayUserOutfits(username) {
  // post request to /userItems to retrieve user's items
  let postParams = {
    username: username
  };

  $.post("/userOutfits", postParams, responseJSON => {
    //get the items of the user
    let userItems = JSON.parse(responseJSON).items;
    console.log(userItems);
    generateCards(userItems);
  });
}

// function that animates the add item modal
function itemModalAnimation() {
  additionalColorForm();
  dynamicTypeDropdown();

  let modal = $('#addItemModal');
  let btn = $('#addItem');
  let span = $('#addSpan');

  // open modal when button clicked
  btn.click(function() {
    console.log('clicked');
    modal.css("display", "block");
  });

  // close modal when user clicks 'x'
  span.click(function() {
    modal.css("display", "none");
  });
}

function addItemFormSubmit() {
  $('#addItemForm').on("submit", function(e) {
    e.preventDefault();
    // get parameters for post request from form
    let postParams = {
      username: username,
      itemName: $('#item-name').val(),
      itemColor: $('#item-color').val(),
      itemType1: $('#type-1').val(),
      itemPattern: $('#item-pattern').val(),
      itemSeason: $('#item-season').val(),
      itemFormality: $('#item-formality').val()
    }
    // post request to addItems
    console.log(postParams);
    $.post("/addItem", postParams, responseJSON => {
      let item = JSON.parse(responseJSON);
      let itemList = [item];
      generateCards(itemList);
      $('#addItemModal').css("display", "none");
    });
  });
}


$(document).ready(() => {
  username = localStorage.username;
  // displayUserItems(username);
  // itemModalAnimation();
  // addItemFormSubmit();
  $(".name").html(localStorage.getItem('name'));
});
