// username of current user
let username;

// save the current tab index (generic outfit page)
let currentTab = 0;

// save selected item that user wants to add to an oufit
let $selected;

// save empty form html
let emptyForm;

// ordering of component types (for indices of tab)
let compInd = ["ALL", "OUTER", "TOP", "BOTTOM", "SHOES"];

// generate html content for modal popup when user clicks on an outfit
function generateOutfitContent(outfit, id) {
    // outfit represented as [id, name, outer, top, bottom, shoes]
    let postParams = {
        outer: outfit[2],
        top: outfit[3],
        bottom: outfit[4],
        shoes: outfit[5]
    }

    // post request to get outfit components as array
    $.post("/outfitComponents", postParams, responseJSON => {
        let outfitContent = '<div class="fullOutfit">';
        outfitContent += generateItemContent(JSON.parse(responseJSON).outer, outfit[2]);
        outfitContent += generateItemContent(JSON.parse(responseJSON).top, outfit[3]);
        outfitContent += generateItemContent(JSON.parse(responseJSON).bottom, outfit[4]);
        outfitContent += generateItemContent(JSON.parse(responseJSON).shoes, outfit[5]);
        outfitContent += '</div>';

        $('#modal-' + id + ' .modal-content').append(outfitContent);
    });
}

// generate individual item html (within the outfit html card) TODO: REMOVE?
function generateItemContent(item, id) {
    let itemContent;
    if (item.length == 0) {
        itemContent = '<div class="item" id="item-empty">' + N / A + "</div>";
    } else {
        // itemContent = '<div tabindex="-1" class="item" id="item-' + id +
        // '">'; itemContent += '<h5>' + item[1] + '</h5><br>'; itemContent +=
        // '<p>Color:' + item[2] + '</p><br>'; itemContent += '<p>Type:' +
        // item[3] + '</p><br>'; itemContent += '<p>Pattern:' + item[4] +
        // '</p><br>'; itemContent += '<p>Season:' + item[5] + '</p><br>';
        // itemContent += '<p>Formality:' + item[6] + '</p><br>'; itemContent
        // += '</div>';
        itemContent = generateItemIcon(item, id);

        let imageSource = item[7];
        $('#item-' + id).css("background", "url(" + imageSource + ") no-repeat");
        $('#item-' + id).css("background-size", "100%");
    }
    return itemContent;
}

// generate item icons to display when users are adding to an outfit
function generateItemIcon(item, id) {
    return '<div tabindex="-1" class="item" id="item-' + id + '">' + item[1] + '</div>';
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
    btn.click(function () {
        modal.css("display", "block");
    });

    // close modal when user clicks 'x'
    span.click(function () {
        modal.css("display", "none");
    });
}

// generates the modal/cards for each outfit from a list
function generateOutfitCards(listOfOutfits) {
    for (i = 0; i < listOfOutfits.length; i++) {
        // get current item json
        let outfit = listOfOutfits[i];

        // generate modal html
        let id = outfit[0];
        let buttonHTML = '<div class="outfit-card"><button class="outfit" id="outfit-' + id + '">' + outfit[1] + '</button></div>';
        let modalHTML = '<div class="modal" id="modal-' + id + '">';
        modalHTML += '<div class="modal-content">';
        modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
        modalHTML += '<button class="delete" id="delete-outfit-' + id + '">Delete Outfit</button>';
        modalHTML += '</div></div>';

        // add modal to div 'items'
        $('#outfits').append(buttonHTML);
        $('#outfits').append(modalHTML);

        let outfitCard = document.getElementById("outfit-" + id);

        displayLikes(username, id, 0, outfitCard);


        generateOutfitContent(outfit, id);

        // add popup functionality to given modal
        animateOutfitModal(id);

        // add delete button functionality
        deleteUserOutfit(id);
    }
    // set dimensions of cards
    $('.outfit').css("width", "100%");
}

// add the items to each specific tab (when adding top, bottom, etc.)
function generateItemCards(listOfItems, tabId) {
    for (i = 0; i < listOfItems.length; i++) {
        // get current item json
        let item = listOfItems[i];

        // generate modal html
        let id = item[0];

        // check if item already exists on page
        if ($('.tab-add #item-' + id).html() == null) {
            // set item html
            let divHTML = generateItemIcon(item, id);
            $('#' + tabId).append(divHTML);

            // add image for icon
            let imageSource = item[7];
            $('.tab-add #item-' + id).css("background", "url(" + imageSource + ") no-repeat");
            $('.tab-add #item-' + id).css("background-size", "100%");

            // add event listener for focus (ie user selection)
            $('.tab #item-' + id).focus(function () {
                // console.log(id);
                $('.tab-add #item-' + id).focus(function () {
                    console.log(id);
                    $selected = this;
                })
            })
        }
        // set dimensions of cards
        $('.item').css("width", "20%");
        let itemWidth = $('.item').width();
        $('.item').height('200px');
    }
}


// function to retrieve and display a user's outfits
function displayUserOutfits(username) {

    let postParams = {
        username: username
    };

    $.post("/userOutfits", postParams, responseJSON => {
        let userOutfits = JSON.parse(responseJSON).outfits;
        generateOutfitCards(userOutfits);
    });
}

// add button functionality to remove an outfit
function deleteUserOutfit(outfitId) {
    // event handler for removing item
    $('#delete-outfit-' + outfitId).on('click', function (e) {
        let postParams = {
            username: username,
            outfitId: outfitId
        };

        // post request to remove item
        $.post("/deleteOutfit", postParams, responseJSON => {
            $('#outfit-' + outfitId).remove();
            $('#modal-' + outfitId).remove();
        });

        window.location.reload();
    });
}

// function that animates the add outfit modal
function outfitModalAnimation() {
    let modal = $('#addOutfitModal');
    let btn = $('#constructOutfit');
    let span = $('#addSpan');

    // open modal when button clicked
    btn.click(function () {
        modal.css("display", "block");
    });

    // close modal when user clicks 'x'
    span.click(function () {
        modal.css("display", "none");
    });
}

// function that populates the tab with items of that type when adding items
function populateTabItems(compId, currTabId) {
    // generate html components for each item in the tab
    let postParams = {
        username: username,
        component: compInd[compId]
    }
    $.post("/outfitByAttribute", postParams, responseJSON => {
        let itemList = JSON.parse(responseJSON).items;
        generateItemCards(itemList, currTabId);
    })
}

// function that replaces button with selected item, navigate back to home
function addItemToOutfit(event) {
    let compDiv = document.getElementsByClassName("add")[currentTab - 1];
    compDiv.innerHTML = $selected.outerHTML;
    navigateToTab(event, 0);
}

// displays tab corresponding to the component (outer: 1, top: 2, bottom: 3,
// shoes: 4)
function showTab(compId) {
    // get all the tabs on the page, display the selected component
    let tabs = document.getElementsByClassName("tab-add");
    if (compId == 0) {
        // want the generic outfit page
        tabs[0].style.display = "block";
        // hide and show appropriate buttons
        document.getElementById("addItem").style.display = "none";
        document.getElementById("back").style.display = "none";
        document.getElementById("addOutfit").style.display = "inline";
    } else {
        populateTabItems(compId, tabs[compId].id);
        tabs[compId].style.display = "table";
        document.getElementById("addItem").style.display = "inline";
        document.getElementById("back").style.display = "inline";
        document.getElementById("addOutfit").style.display = "none";
        document.getElementById("suggestOutfits").style.display = "none";
    }
}

// navigate to appropriate tab upon click of button
function navigateToTab(event, tabInd) {
    event.preventDefault();
    let tabs = document.getElementsByClassName("tab-add");
    // hide the current tab
    tabs[currentTab].style.display = "none";
    // set current tab index to new tab index
    currentTab = tabInd;
    // show the correct tab
    showTab(tabInd);
}

// function that returns the id of an item div
function getIntId(str) {
    return str.split('-')[1];
}

// function that adds a fully constructed outfit to the database
function addOutfit() {
    $('#addOutfit').on("click", function (e) {
        e.preventDefault();
        // get all items in the div
        let outer = document.getElementById("outer-item").getElementsByClassName("item")[0];
        let top = document.getElementById("top-item").getElementsByClassName("item")[0];
        let bottom = document.getElementById("bottom-item").getElementsByClassName("item")[0];
        let shoes = document.getElementById("shoes-item").getElementsByClassName("item")[0];

        let postParams = {
            username: username,
            name: $('#outfit-name').val(),
            outer: getIntId(outer.id),
            top: getIntId(top.id),
            bottom: getIntId(bottom.id),
            shoes: getIntId(shoes.id)
        }

        // console.log(postParams);

        // post request to addItems
        $.post("/addOutfit", postParams, responseJSON => {
            let outfit = JSON.parse(responseJSON);
            let outfitList = [outfit];
            generateOutfitCards(outfitList);
            $("#addOutfitModal").css("display", "none");
            resetForm(e);
        });
    });
}

function resetForm(event) {
    $('#addOutfitForm').html(emptyForm);
    navigateToTab(event, 0);
}

function displayLikes(username, outfitId, change, outfitCard) {

    const postParams = {
        username: username
    };
    $.post("/liked", postParams, responseJSON => {
        let likedOutfits = JSON.parse(responseJSON).likedOutfitIds;
        const postParams = {
            mode: change,
            username: username,
            outfitId: outfitId
        };

        let likes = 0;
        $.post("/like", postParams, responseJSON => {
            // console.log(JSON.parse(responseJSON).success);
            likes = JSON.parse(responseJSON).likes;
            if (likedOutfits.includes(Number(outfitId))) {
                likeClass = "liked";
            } else {
                likeClass = "not-liked";
            }
            console.log(likes + " " + likeClass);
            outfitCard.insertAdjacentHTML("afterend", "<div class='like-wrapper'>" +
                "<button onclick='like(" + outfitId + ")' class='like-button " + likeClass + "' id='like-button-" + outfitId + "'>Like</button>" +
                "<p id='like-num-" + outfitId + "'>" + likes + " Likes</p>" +
                "</div>");
        });
    });
}

function like(outfitId) {
    // console.log(outfitId);
    let classes = document.getElementById('like-button-' + outfitId);
    let likeClass = classes.className.split(" ")[1];
    if (likeClass === "liked") {
        document.getElementById('like-button-' + outfitId).classList.remove('liked');
        document.getElementById('like-button-' + outfitId).classList.add('not-liked');

        const postParams = {
            mode: -1,
            username: username,
            outfitId: outfitId
        };

        $.post("/like", postParams, responseJSON => {
            let likes = JSON.parse(responseJSON).likes;
            document.getElementById('like-num-' + outfitId).innerHTML = likes + " Likes";
            // console.log(document.getElementById('like-num-' +
            // outfitId).innerHTML)
        })

    } else {
        document.getElementById('like-button-' + outfitId).classList.remove('not-liked');
        document.getElementById('like-button-' + outfitId).classList.add('liked');

        const postParams = {
            mode: 1,
            username: username,
            outfitId: outfitId
        };

        $.post("/like", postParams, responseJSON => {
            let likes = JSON.parse(responseJSON).likes;
            document.getElementById('like-num-' + outfitId).innerHTML = likes + " Likes";
            // console.log(document.getElementById('like-num-' +
            // outfitId).innerHTML)
        })
    }

}


$(document).ready(() => {
    username = localStorage.username;
    emptyForm = $('#addOutfitForm').html();
    displayUserOutfits(username);
    showTab(0);
    outfitModalAnimation();
    addOutfit();
    $(".name").html(localStorage.getItem('name'));
});
