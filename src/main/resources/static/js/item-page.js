// generate html content (ie. item information) for card
function generateCardContent(item) {
  // item represented as array [name, color, type, pattern, season, formality]
  let itemContent = '<h5>' + item[0] + '</h5><br>';
  itemContent += '<p>Color:' + item[1] + '</p></br>';
  itemContent += '<p>Type:' + item[2] + '</p></br>';
  itemContent += '<p>Pattern:' + item[3] + '</p></br>';
  itemContent += '<p>Season:' + item[4] + '</p></br>';
  itemContent += '<p>Formality:' + item[5] + '</p></br>';
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
  for (i = 0; i < listOfItems.length; i++) {
    // get current item
    let item = listOfItems[i];

    // generate modal html
    let id = i + 1
    let buttonHTML = '<button class="item" id="item-' + id + '">Item' + id + '</button>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateCardContent(item);
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('#items').append(buttonHTML);
    $('#items').append(modalHTML);

    // add popup functionality to given modal
    animateItemModal(id);
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
}

// function to retrieve and display user items
function displayUserItems(userId) {
  // post request to /userItems to retrieve user's items
  // let postParams = {
  //   userId: userId
  // };

  userItems = [
    ["Item 1", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 2", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 3", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 4", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 5", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 6", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 7", "red", "tshirt", "solid", "summer", "casual"],
    ["Item 8", "red", "tshirt", "solid", "summer", "casual"],
  ]

  generateCards(userItems);

  // $.post("/userItems", postParams, responseJSON => {
  //   get the items of the user
  //   let userItems = JSON.parse(responseJSON).items;
  //   generateCards(userItems);
  // });
}

// function that dynamically populates second type dropdown
function dynamicTypeDropdown() {
  $('#type-1').on('change', function(){
    $('#type-2').html('');
    if ($('#type-1').val() == "outer") {
        $('#type-2').append('<option value="outer-coat">Outer Coat</option>');
        $('#type-2').append('<option value="suit">Suit</option>');
    } else if ($('#type-1').val() == "top") {
      $('#type-2').append('<option value="shirt-blouse">Shirt/Blouse</option>');
      $('#type-2').append('<option value="tshirt">T-Shirt</option>');
      $('#type-2').append('<option value="sweater">Sweater</option>');
      $('#type-2').append('<option value="jacket">Jacket</option>');
    } else if ($('#type-1').val() == "bottom") {
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


$(document).ready(() => {
  displayUserItems(1);
  itemModalAnimation();
});
