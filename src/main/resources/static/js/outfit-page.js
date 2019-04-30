// username of current user
let username;

// ordering of component types (for indices of tab)
let compInd = ["OUTER", "TOP", "BOTTOM", "SHOES"];

// generate outfit html (multiple items and season/formality attributes)
function generateOutfitContent(outfit) {
  // outfit represented as [id, name, outer, top, bottom, shoes, season, formality]
  let postParams = {
    outer: outfit[2],
    top: outfit[3],
    bottom: outfit[4],
    shoes: outfit[5]
  }

  // post request to get outfit components as array
  $.post("/outfitComponents", postParams, responseJSON => {
    let outfitContent = generateItemContent(JSON.parse(responseJSON).outer, outerId);
    outfitContent += generateItemContent(JSON.parse(responseJSON).top, topId);
    outfitContent += generateItemContent(JSON.parse(responseJSON).bottom, bottomId);
    outfitContent += generateItemContent(JSON.parse(responseJSON).shoes, shoesId);
    outfitContent += '<p> Season: ' + outfit[6] + '</p>';
    outfitContent += '<p> Formality: ' + outfit[7] + '</p>';
  });

  return outfitContent
}

// generate individual item html (within the outfit html card)
function generateItemContent(item, id) {
  let itemContent;
  if (item.length == 0) {
    itemContent = '<div class="item" id="item-empty">' + N/A + "</div>";
  } else {
    itemContent = '<div class="item" id= "item-' + id + '">';
    itemContent += '<h5>' + item[1] + '</h5><br>';
    itemContent += '<p>Top:' + item[2] + '</p></br>';
    itemContent += '<p>Type:' + item[3] + '</p></br>';
    itemContent += '<p>Pattern:' + item[4] + '</p></br>';
    itemContent += '<p>Season:' + item[5] + '</p></br>';
    itemContent += '<p>Formality:' + item[6] + '</p></br>';
    itemContent += '</div>';
  }
  return itemContent;
}

// animate modal for popup functionality of outfit modal
function animateOutfitModal(outfitId) {
  // id of the html elements
  let mId = '#modal-' + outfitId;
  let bId = '#outfit-' + outfitId;
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

// generates the modal/cards for each outfit from a list
function generateCards(listOfOutfits) {
  for (i = 0; i < listOfOutfits.length; i++) {
    // get current item json
    let outfit = listOfOutfits[i];

    // generate modal html
    let id = outfit[0];
    let buttonHTML = '<button class="outfit" id="outfit-' + id + '">' + id + '</button>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateOutfitContent(outfit);
    modalHTML += '<button id="delete-outfit-' + id + '">Delete Outfit</button>';
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('#outfits').append(buttonHTML);
    $('#outfits').append(modalHTML);

    // add popup functionality to given modal
    animateItemModal(id);

    // add delete button functionality
    deleteUserItem(id);
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
}

// generates the cards for each item
function generateOutfitCards(listOfCards) {
  for (i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html
    let id = item[0];
    let buttonHTML = '<button class="item" id="item-' + id + '">' + id + '</button>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateItemContent(item);
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


// function to retrieve and display user items
function displayUserOutfits(username) {
  // post request to /userItems to retrieve user's items
  let postParams = {
    username: username
  };

  $.post("/userOutfits", postParams, responseJSON => {
    //get the items of the user
    let userOutfits = JSON.parse(responseJSON).outfits;
    generateCards(userOutfits);
  });
}

// add button functionality to remove an outfit
function deleteUserOutfit(outfitId) {
  // event handler for removing item
  $('#delete-item-' + itemId).on('click', function(e) {
    let postParams = {
      username: username,
      outfitId: outfitId
    };

    // post request to remove item
    $.post("/deleteOutfit", postParams, responseJSON => {
      $('#item-' + outfitId).remove();
      $('#modal-' + outfitId).remove();
    });

    window.location.reload();
  });
}

// function that animates the add item modal
function itemModalAnimation() {

  let modal = $('#addOutfitModal');
  let btn = $('#addOutfit');
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

function retrieveItemCorpus(component) {

  let postParams = {
    username: username,
    component: component
  }

  $.post("/outfitByAttribute", postParams, responseJSON => {

    let itemList = JSON.parse(responseJSON).items;
    generateCards(itemList);

  })

}

// displays tab corresponding to the component (outer: 0, top: 1, bottom: 2, shoes: 3)
function showTab(compId) {
  // get all the tabs on the page, display the selected component
  let tabs = document.getElementsByClassName("tab");
  x[compId].style.display = "block";
  // display the add item and back buttons
  $('#addItem').style.display = "inline";
}

// function that populates the tab with items of that type
function populateTabItems(compId, currTab) {
  // generate html components for each item in the tab

  let postParams = {
    username: username,
    component: compInd[compId]
  }

  $.post("/outfitByAttribute", postParams, responseJSON => {
    let itemList = JSON.parse(responseJSON).items;
    generateCards(itemList)
  })


}


$(document).ready(() => {
  username = localStorage.username;
  // displayUserItems(username);
  // itemModalAnimation();
  // addItemFormSubmit();
  $(".name").html(localStorage.getItem('name'));
});
