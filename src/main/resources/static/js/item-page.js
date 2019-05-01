// map from id -> item array (?)
// let itemCache = new Map([]);
// username of current user
let username;
$(".name").html(localStorage.getItem('name'));

// generate html content (ie. item information) for card
function generateCardContent(item) {
  // item represented as array [id, name, color, type, pattern, season, formality, image src]
  let itemContent = '<img src="' + item[7] +'">';
  itemContent += '<h5>' + item[1] + '</h5><br>';
  itemContent += '<p>Color:' + item[2] + '</p></br>';
  itemContent += '<p>Type:' + item[3] + '</p></br>';
  itemContent += '<p>Pattern:' + item[4] + '</p></br>';
  itemContent += '<p>Season:' + item[5] + '</p></br>';
  itemContent += '<p>Formality:' + item[6] + '</p></br>';
  return itemContent;
}

// animate modal for popup functionality
function animateItemModal(itemId) {
  // id of the html elements
  let mId = '#modal-' + itemId;
  let bId = '#item-' + itemId;
  let sId = '#close-' + itemId;

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
function generateCards(listOfItems) {
  for (let i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html
    let id = item[0];
    let buttonHTML = '<button class="item" id="item-' + id + '">' + item[1] + '</button>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateCardContent(item);
    modalHTML += '<button id="delete-item-' + id + '">Delete Item</button>';
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('#items').append(buttonHTML);
    $('#items').append(modalHTML);

    // Set image
    let imageSource = item[7];
    $('#item-' + id).css("background", "url(" + imageSource + ") no-repeat");
    $('#item-' + id).css("background-size", "100%");

    // add popup functionality to given modal
    animateItemModal(id);

    // add delete button functionality
    deleteUserItem(id);
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
  let itemWidth = $('.item').width();
  $('.item').height(itemWidth * 1.2);
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
function displayUserItems(username) {
  // post request to /userItems to retrieve user's items
  let postParams = {
    username: username
  };

  $.post("/userItems", postParams, responseJSON => {
    //get the items of the user
    let userItems = JSON.parse(responseJSON).items;
    console.log(userItems);
    generateCards(userItems);
  });
}

// function that dynamically populates second type dropdown
function dynamicTypeDropdown() {
  $('#type-1').on('change', function(){
    $('#type-2').html('');
    if ($('#type-1').val() == "OUTER") {
        $('#type-2').append('<option value="outer-coat">Outer Coat</option>');
        $('#type-2').append('<option value="suit">Suit</option>');
    } else if ($('#type-1').val() == "TOP") {
      $('#type-2').append('<option value="shirt-blouse">Shirt/Blouse</option>');
      $('#type-2').append('<option value="tshirt">T-Shirt</option>');
      $('#type-2').append('<option value="sweater">Sweater</option>');
      $('#type-2').append('<option value="jacket">Jacket</option>');
    } else if ($('#type-1').val() == "BOTTOM") {
      $('#type-2').append('<option value="pants">Pants</option>');
      $('#type-2').append('<option value="skirt">Skirt</option>');
      $('#type-2').append('<option value="dress">Dress</option>');
      $('#type-2').append('<option value="shorts">Shorts</option>');
    } else {
      $('#type-2').append('<option value="sneakers">Sneakers</option>');
      $('#type-2').append('<option value="boots">Boots</option>');
      $('#type-2').append('<option value="sandals">Sandals</option>');
      $('#type-2').append('<option value="dress-shoes">Dress Shoes</option>');
    }
  });
}

// function that allows for users to add more colors to outfit
function additionalColorForm() {
  $('#addColor').on('click', function(e) {
    $('#item-colors').append('<input type="color" name="item-color" value="#ff0000"><br>');
    e.preventDefault();
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
    };
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
  displayUserItems(username);
  itemModalAnimation();
  addItemFormSubmit();
  $(".name").html(localStorage.getItem('name'));
});
