let currentSuggestTab = 0;

// add the items to each specific tab for suggest (when adding top, bottom, etc.)
function generateSuggestItemCards(listOfItems, tabId) {
  for (i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html
    let id = item[0];

    // check if item already exists on page
    if ($('.tab-suggest #item-' + id).html() == null) {
      // set item html
      let divHTML = generateItemIcon(item, id);
      $('#' + tabId + '.tab-suggest').append(divHTML);

      // add image for icon
      let imageSource = item[7];
      $('.tab-suggest #item-' + id).css("background", "url(" + imageSource + ") no-repeat");
      $('.tab-suggest #item-' + id).css("background-size", "100%");

      // add event listener for focus (ie user selection)
      $('.tab-suggest #item-' + id).focus(function() {
        $selected = this;
      })
    }
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
  let itemWidth = $('.item').width();
  $('.item').height('200px');
}

function generateSuggestionsCards(listOfItems, tabId) {
  for (i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html
    let id = item[0];

    // check if item already exists on page
    if ($('.tab-suggest #' + tabId + '#item-' + id).html() == null) {
      // set item html
      let divHTML = generateItemIcon(item, id);
      $('#' + tabId).append(divHTML);


      // add image for icon
      let imageSource = item[7];
      $('.tab-suggest #item-' + id).css("background", "url(" + imageSource + ") no-repeat");
      $('.tab-suggest #item-' + id).css("background-size", "100%");

      // add event listener for focus (ie user selection)
      $('.tab-suggest #item-' + id).focus(function() {
        $selected = this;
      })
    }
  }
  // set dimensions of cards
  $('.item').css("width", "20%");
  let itemWidth = $('.item').width();
  $('.item').height('200px');
}

// function that populates the tab with items of that type when adding items
function populateSuggestTabItems(compId, currTabId) {
  // generate html components for each item in the tab
  let postParams = {
    username: username,
    component: compInd[compId]
  }
  $.post("/outfitByAttribute", postParams, responseJSON => {
    let itemList = JSON.parse(responseJSON).items;
    generateSuggestItemCards(itemList, currTabId);
  })
}

// function that replaces button with selected item, navigate back to home
function addItemToOutfitSuggest(event) {
  let compDiv = document.getElementsByClassName("add-suggest")[currentSuggestTab - 1];
  compDiv.innerHTML = $selected.outerHTML;
  navigateToSuggestTab(event, 0);
}

function addItemFromSuggestions(event) {
  console.log($selected.parent().get(0));
}

// displays tab corresponding to the component (outer: 1, top: 2, bottom: 3, shoes: 4)
function showSuggestTab(compId) {
  // get all the tabs on the page, display the selected component
  let tabs = document.getElementsByClassName("tab-suggest");
  if (compId == 0) {
    // want the generic outfit page
    tabs[0].style.display = "block";
    // hide and show appropriate buttons
    document.getElementById("addItemSuggest").style.display = "none";
    document.getElementById("backSuggest").style.display = "none";
    document.getElementById("addFromSuggest").style.display = "none";
    document.getElementById("suggest").style.display = "inline";
  } else if (compId == 5) {
    populateSuggestTabItems(compId, tabs[compId].id);
    tabs[compId].style.display = "table";
    document.getElementById("addItemSuggest").style.display = "none";
    document.getElementById("backSuggest").style.display = "inline";
    document.getElementById("suggest").style.display = "none";
    document.getElementById("suggestOutfit").style.display = "none";
    document.getElementById("addFromSuggest").style.display = "inline";
  } else {
    populateSuggestTabItems(compId, tabs[compId].id);
    tabs[compId].style.display = "table";
    document.getElementById("addItemSuggest").style.display = "inline";
    document.getElementById("backSuggest").style.display = "inline";
    document.getElementById("suggest").style.display = "none";
    document.getElementById("suggestOutfit").style.display = "none";
  }
}

// navigate to appropriate tab for suggest modal upon click of button
function navigateToSuggestTab(event, tabInd) {
  event.preventDefault();
  let tabs = document.getElementsByClassName("tab-suggest");
  // hide the current tab
  tabs[currentSuggestTab].style.display = "none";
  // set current tab index to new tab index
  currentSuggestTab = tabInd;
  // show the correct tab
  showSuggestTab(tabInd);
}

// function that animates the suggest outfit modal
function outfitSuggestModal() {
  let modal = $('#suggestOutfitModal');
  let btn = $('#suggestOutfit');
  let span = $('#suggestSpan');

  // open modal when button clicked
  btn.click(function() {
    modal.css("display", "block");
  });

  // close modal when user clicks 'x'
  span.click(function() {
    modal.css("display", "none");
  });
}

// function that displays suggestions for missing
function getSuggestions() {
  $('#suggest').click(function(e) {
    e.preventDefault();

    let outer = document.getElementsByClassName("add-suggest")[0].getElementsByClassName("item")[0];
    let top = document.getElementsByClassName("add-suggest")[1].getElementsByClassName("item")[0];
    let bottom = document.getElementsByClassName("add-suggest")[2].getElementsByClassName("item")[0];
    let shoes = document.getElementsByClassName("add-suggest")[3].getElementsByClassName("item")[0];

    let postParams = {
      username: username,
      outer: null,
      top: null,
      bottom: null,
      shoes: null
    }

    if (typeof outer !== 'undefined') {
      postParams.outer = outer.id.split('-')[1];
    }

    if (typeof top !== 'undefined') {
      postParams.top = top.id.split('-')[1]
    }

    if (typeof bottom !== 'undefined') {
      postParams.bottom = bottom.id.split('-')[1]
    }

    if (typeof shoes !== 'undefined') {
      postParams.shoes = shoes.id.split('-')[1]
    }

    console.log(postParams);

    $.post("/itemSuggestions", postParams, responseJSON => {
      let outerSuggestions = JSON.parse(responseJSON).outerSuggestions;
      let topSuggestions = JSON.parse(responseJSON).topSuggestions;
      let bottomSuggestions = JSON.parse(responseJSON).bottomSuggestions;
      let shoesSuggestions = JSON.parse(responseJSON).shoesSuggestions;

      generateSuggestionsCards(outerSuggestions, 'display-outer-suggestions');
      generateSuggestionsCards(topSuggestions, 'display-top-suggestions');
      generateSuggestionsCards(bottomSuggestions, 'display-bottom-suggestions');
      generateSuggestionsCards(shoesSuggestions, 'display-shoes-suggestions');
      navigateToSuggestTab(e, 5);
    })
  })
}

$(document).ready(() => {
  outfitSuggestModal();
  showSuggestTab(0);
  getSuggestions();
});
