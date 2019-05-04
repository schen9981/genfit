let currentSuggestTab = 0;

// add the items to each specific tab for suggest (when adding top, bottom,
// etc.)
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
            let imageSource = item[8];
            $('.tab-suggest #item-' + id).css("background", "url(" + imageSource + ") no-repeat");
            $('.tab-suggest #item-' + id).css("background-size", "100%");

            // add event listener for focus (ie user selection)
            $('.tab-suggest #item-' + id).focus(function () {
                $selected = this;
            });
        }
    }
    // TODO: remove
    // set dimensions of cards
    // $('.item').css("width", "20%");
    // let itemWidth = $('.item').width();
    // $('.item').height('200px');
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
            let imageSource = item[8];
            $('.tab-suggest #item-' + id).css("background", "url(" + imageSource + ") no-repeat");
            $('.tab-suggest #item-' + id).css("background-size", "100%");

            // add event listener for focus (ie user selection)
            $('.tab-suggest #item-' + id).focus(function () {
                $selected = $(this);
            })
        }
    }
    // TODO: remove
    // set dimensions of cards
    // $('.item').css("width", "20%");
    // let itemWidth = $('.item').width();
    // $('.item').height('200px');
}

// function that populates the tab with items of that type when adding items
function populateSuggestTabItems(compId, currTabId) {
    // generate html components for each item in the tab
    let postParams = {
        username: username,
        component: compInd[compId]
    }
    console.log(postParams);
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

function getCompId(idName) {
    if (idName == 'display-outer-suggestions') {
        return 0;
    } else if (idName == 'display-top-suggestions') {
        return 1;
    } else if (idName == 'display-bottom-suggestions') {
        return 2;
    } else {
        return 3;
    }
}

function addItemFromSuggestions(event) {
    let compId = getCompId($selected.parent().attr('id'));
    let compDiv = document.getElementsByClassName("add-suggest")[compId];
    compDiv.innerHTML = $selected.context.outerHTML;
    navigateToSuggestTab(event, 0);
}

// displays tab corresponding to the component (outer: 1, top: 2, bottom: 3,
// shoes: 4)
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
        document.getElementById("addOutfitSuggest").style.display = "inline";
        document.getElementById("suggestOutfit").style.display = "inline";
    } else if (compId == 5) {
        //populateSuggestTabItems(compId, tabs[compId].id);
        tabs[compId].style.display = "table";
        document.getElementById("addItemSuggest").style.display = "none";
        document.getElementById("backSuggest").style.display = "inline";
        document.getElementById("suggest").style.display = "none";
        // document.getElementById("suggestOutfit").style.display = "none";
        document.getElementById("addFromSuggest").style.display = "inline";
        document.getElementById("addOutfitSuggest").style.display = "none";
    } else {
        populateSuggestTabItems(compId, tabs[compId].id);
        tabs[compId].style.display = "table";
        document.getElementById("addItemSuggest").style.display = "inline";
        document.getElementById("backSuggest").style.display = "inline";
        document.getElementById("suggest").style.display = "none";
        // document.getElementById("suggestOutfit").style.display = "none";
        document.getElementById("addOutfitSuggest").style.display = "none";
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
    btn.click(function () {
        modal.css("display", "block");
    });

    // close modal when user clicks 'x'
    span.click(function () {
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

function resetForm() {
    $("#outer-item").html('<button id="suggest-outer-item"onclick="navigateToSuggestTab(event, 1)">Add Outer</button>');
    $("#top-item").html('<button id="suggest-top-item"onclick="navigateToSuggestTab(event, 2)">Add Top</button>');
    $("#bottom-item").html('<button id="suggest-bottom-item"onclick="navigateToSuggestTab(event, 3)">Add Bottom</button>');
    $("#shoes-item").html('<button id="suggest-shoes-item"onclick="navigateToSuggestTab(event, 4)">Add Shoes</button>');
}

// function that adds a fully constructed outfit to the database
function addOutfit() {
    $('#addOutfitSuggest').on("click", function (e) {
        e.preventDefault();
        // get all items in the div
        let outer = document.getElementsByClassName("add-suggest")[0].getElementsByClassName("item")[0];
        let top = document.getElementsByClassName("add-suggest")[1].getElementsByClassName("item")[0];
        let bottom = document.getElementsByClassName("add-suggest")[2].getElementsByClassName("item")[0];
        let shoes = document.getElementsByClassName("add-suggest")[3].getElementsByClassName("item")[0];

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
    showSuggestTab(0);
    getSuggestions();
});
