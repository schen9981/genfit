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
function animateModal(itemId) {
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
    animateModal(id);
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
}


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

$(document).ready(() => {
  displayUserItems(1);
});
