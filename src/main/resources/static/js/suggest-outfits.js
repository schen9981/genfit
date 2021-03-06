let currentSuggestTab = 0;

// add the items to each specific tab for suggest (when adding top, bottom,
// etc.)
function generateSuggestItemCards(listOfItems, tabId) {
    for (i = 0; i < listOfItems.length; i++) {
        // get current item json
        let item = listOfItems[i];

        // generate modal html
        let id = item[0];

        let $itemSelector = $('.tab-suggest #item-' + id);

        // check if item already exists on page
        if (typeof $itemSelector.html() === "undefined") {
            // set item html
            // function from outfit-page.js
            let divHTML = generateItemIcon(item, id);
            $('#' + tabId + '.tab-suggest .item-display').append(divHTML);

            // add image for icon
            let imageSource = item[8];

            // reselect item
            $itemSelector = $('.tab-suggest #item-' + id);
            $itemSelector.css("background",
                "url(\"" + imageSource + "\") no-repeat");
            $itemSelector.css("background-size", "100%");
            $itemSelector.css("background-position", "center");

            // add event listener for focus (ie user selection)
            $itemSelector.focus(function () {
                $selected = this;
            });
        }
    }
}

function generateSuggestionsCards(listOfItems, tabId) {
    $('#' + tabId + " div.suggestionsDisplayDiv").empty();
    for (i = 0; i < listOfItems.length; i++) {
        // get current item json
        let item = listOfItems[i];

        // generate modal html
        let id = item[0];

        // set item html
        let divHTML = generateItemIcon(item, id);
        // remove already existing items from previous suggestion queries
        $('#' + tabId + " div.suggestionsDisplayDiv").append(divHTML);

        // add image for icon
        let imageSource = item[8];
        $('.tab-suggest #item-' + id).css("background", "url(\"" + imageSource + "\") no-repeat");
        $('.tab-suggest #item-' + id).css("background-size", "100%");

        // add event listener for focus (ie user selection)
        $('.tab-suggest #item-' + id).focus(function () {
            $selected = $(this);
        })
    }
}

// function that populates the tab with items of that type when adding items
function populateSuggestTabItems(compId, currTabId) {
    // generate html components for each item in the tab
    let postParams = {
        username: username,
        component: compInd[compId]
    };
    // TODO: remove
    // console.log("75");
    // console.log(postParams);
    $.post("/outfitByAttribute", postParams, responseJSON => {
        let itemList = JSON.parse(responseJSON).items;
        generateSuggestItemCards(itemList, currTabId);
    })
}

// function that replaces button with selected item, navigate back to home
function addItemToOutfitSuggest(event) {
    if ($selected !== null) {
        let compDiv = document.getElementsByClassName("add-suggest")[currentSuggestTab - 1];
        compDiv.innerHTML = $selected.outerHTML;
        // get current item div id
        let itemId = $($selected.outerHTML).attr('id');
        // add event listener to the new div
        $('#' + itemId).on('click', function(e) {
          // get id of parent div
          let parent = $(this).parent().attr("id");
          navigateToSuggestTab(event, determineParentNavId(parent));
        })
        $selected = null;
        navigateToSuggestTab(event, 0);
    }
}

function determineParentNavId(parentId) {
  if (parentId == 'outer-item') {
    return 1;
  } else if (parentId == 'top-item') {
    return 2;
  } else if (parentId == 'bottom-item') {
    return 3;
  } else if (parentId == 'shoes-item') {
    return 4;
  }
}

function getCompId(idName) {
    if (idName === 'display-outer-suggestions') {
        return 0;
    } else if (idName === 'display-top-suggestions') {
        return 1;
    } else if (idName === 'display-bottom-suggestions') {
        return 2;
    } else if (idName === 'display-shoes-suggestions') {
        return 3;
    } else {
        return null;
    }
}

function addItemFromSuggestions(event) {
    let compId = getCompId($selected.parent().parent().attr('id'));
    let compDiv = document.getElementsByClassName("add-suggest")[compId];
    compDiv.innerHTML = $selected.context.outerHTML;
    $selected = null;
    navigateToSuggestTab(event, 0);
}

// displays tab corresponding to the component (outer: 1, top: 2, bottom: 3,
// shoes: 4)
function showSuggestTab(compId) {
    // get all the tabs on the page, display the selected component
    let tabs = document.getElementsByClassName("tab-suggest");
    if (compId == 0) { // home tab
        tabs[0].style.display = "flex";
        $selected = null;
        // hide and show appropriate buttons
        document.getElementById('outfit-name-div').style.display = "flex";
        document.getElementById("addItemSuggest").style.display = "none";
        document.getElementById("backSuggest").style.display = "none";
        document.getElementById("addFromSuggest").style.display = "none";
        document.getElementById("suggest").style.display = "inline";
        document.getElementById("addOutfitSuggest").style.display = "inline";
        document.getElementById("suggestOutfitBtn").style.display = "inline";
        document.getElementById("outfit-name-label").style.display = "flex";
        document.getElementById("outfit-name").style.display = "flex";
    } else if (compId == 5) { // suggestions tab
        populateSuggestTabItems(compId, tabs[compId].id);
        tabs[compId].style.display = "table";
        document.getElementById("addItemSuggest").style.display = "none";
        document.getElementById("backSuggest").style.display = "inline";
        document.getElementById("suggest").style.display = "none";
        // document.getElementById("suggestOutfit").style.display = "none";
        document.getElementById("addFromSuggest").style.display = "inline";
        document.getElementById("addOutfitSuggest").style.display = "none";
        document.getElementById("outfit-name-label").style.display = "none";
        document.getElementById("outfit-name").style.display = "none";
    } else if (compId > 0 && compId < 5) { // specific outfit page
        populateSuggestTabItems(compId, tabs[compId].id);
        tabs[compId].style.display = "table";
        document.getElementById("addItemSuggest").style.display = "inline";
        document.getElementById("backSuggest").style.display = "inline";
        document.getElementById("suggest").style.display = "none";
        document.getElementById("addOutfitSuggest").style.display = "none";
        document.getElementById("outfit-name-label").style.display = "none";
        document.getElementById("outfit-name").style.display = "none";
    }
}

// navigate to appropriate tab for suggest modal upon click of button
function navigateToSuggestTab(event, tabInd) {
    if (event !== null) {
        event.preventDefault();
    }
    let tabs = document.getElementsByClassName("tab-suggest");
    // hide the current tab
    tabs[currentSuggestTab].style.display = "none";
    // hide the name div
    document.getElementById('outfit-name-div').style.display = "none";
    // set current tab index to new tab index
    currentSuggestTab = tabInd;
    // show the correct tab
    showSuggestTab(tabInd);
}

// function that animates the suggest outfit modal
function outfitSuggestModal() {
    let modal = $('#suggestOutfitModal');
    // to add a new outfit button
    let btn = $('#suggestOutfitBtn');
    let span = $('#suggestSpan');

    // open modal when button clicked
    btn.click(function () {
        modal.css("display", "block");
    });

    // close modal when user clicks 'x'
    span.click(function () {
        navigateToSuggestTab(null, 0);
        modal.css("display", "none");
        resetForm();
    });
}

// function that displays suggestions for missing
function getSuggestions() {
    $('#suggest').click(function (e) {
        e.preventDefault();

        let outer = document.getElementsByClassName("add-suggest")[0].getElementsByClassName("item")[0];
        let top = document.getElementsByClassName("add-suggest")[1].getElementsByClassName("item")[0];
        let bottom = document.getElementsByClassName("add-suggest")[2].getElementsByClassName("item")[0];
        let shoes = document.getElementsByClassName("add-suggest")[3].getElementsByClassName("item")[0];

        // need at least one item to get suggestions
        if (typeof outer === "undefined"
            && typeof top === "undefined"
            && typeof bottom === "undefined"
            && typeof shoes === "undefined") {
            alert("You must input at least one item to get suggestions.");
            return;
        }

        let postParams = {
            username: username,
            outer: null,
            top: null,
            bottom: null,
            shoes: null
        };

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

        $.post("/itemSuggestions", postParams, responseJSON => {
            let outerSuggestions = JSON.parse(responseJSON).outerSuggestions;
            let topSuggestions = JSON.parse(responseJSON).topSuggestions;
            let bottomSuggestions = JSON.parse(responseJSON).bottomSuggestions;
            let shoesSuggestions = JSON.parse(responseJSON).shoesSuggestions;

            console.log("here");

            generateSuggestionsCards(outerSuggestions, 'display-outer-suggestions');
            generateSuggestionsCards(topSuggestions, 'display-top-suggestions');
            generateSuggestionsCards(bottomSuggestions, 'display-bottom-suggestions');
            generateSuggestionsCards(shoesSuggestions, 'display-shoes-suggestions');
            navigateToSuggestTab(e, 5);
        })
    })
}

// repopulates the from with the initial buttons
function resetForm() {
    $("#outer-item").html('<button id="suggest-outer-item" ' +
        'class="addButton" '
        + 'onclick="navigateToSuggestTab(event, 1)" '
        + '>Add Outer</button>');
    $("#top-item").html('<button id="suggest-top-item" ' +
        'class="addButton" '
        + 'onclick="navigateToSuggestTab(event, 2)">Add Top</button>');
    $("#bottom-item").html('<button id="suggest-bottom-item" ' +
        'class="addButton" ' +
        'onclick="navigateToSuggestTab(event, 3)">Add Bottom</button>');
    $("#shoes-item").html('<button id="suggest-shoes-item" ' +
        'class="addButton" ' +
        'onclick="navigateToSuggestTab(event, 4)">Add Shoes</button>');
}

// function that adds a fully constructed outfit to the database
function addOutfit() {
    $('#addOutfitSuggest').on("click", function (e) {
        e.preventDefault();

        let outfitName = $("#outfit-name").val();

        if (typeof outfitName === "undefined"
            || outfitName === "") {
            alert("Please enter an outfit name.")
            return;
        }


        // get all items in the div
        let outer = document.getElementsByClassName("add-suggest")[0].getElementsByClassName("item")[0];
        let top = document.getElementsByClassName("add-suggest")[1].getElementsByClassName("item")[0];
        let bottom = document.getElementsByClassName("add-suggest")[2].getElementsByClassName("item")[0];
        let shoes = document.getElementsByClassName("add-suggest")[3].getElementsByClassName("item")[0];

        // TODO: make this accept fewer than four items
        // need at least these three categories of items to add outfit
        if (typeof outer === "undefined"
            || typeof top === "undefined"
            || typeof bottom === "undefined"
            || typeof shoes === "undefined") {
            // alert("You must input at least a top, bottom, and shoes.");
            alert("You must input all components of the outfit");
            return;
        }

        let postParams = {
            username: username,
            name: $('#outfit-name').val(),
            outer: outer.id.split('-')[1],
            top: top.id.split('-')[1],
            bottom: bottom.id.split('-')[1],
            shoes: shoes.id.split('-')[1]
        }

        // post request to addItems
        $.post("/addOutfit", postParams, responseJSON => {
            let outfit = JSON.parse(responseJSON);
            let outfitList = [outfit];
            generateOutfitCards(outfitList);
            $("#suggestOutfitModal").css("display", "none");
            resetForm();
        });
    });
}

$(document).ready(() => {
    outfitSuggestModal();
    addOutfit();
    showSuggestTab(0);
    getSuggestions();
});
