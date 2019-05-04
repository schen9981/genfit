// username of current user
let username;

// save selected item that user wants to add to an oufit
let $selected;

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
    };

    // post request to get outfit components as array
    $.post("/outfitComponents", postParams, responseJSON => {
        let $outfitContent = $('#modal-' + id + ' .modal-content' +
            ' div.fullOutfit');
        $outfitContent.append(generateItemContent(JSON.parse(responseJSON).outer, outfit[2]));
        $outfitContent.append(generateItemContent(JSON.parse(responseJSON).top, outfit[3]));
        $outfitContent.append(generateItemContent(JSON.parse(responseJSON).bottom, outfit[4]));
        $outfitContent.append(generateItemContent(JSON.parse(responseJSON).shoes, outfit[5]));
        $outfitContent.append('</div>');
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
        let imageSource = item[8];
        itemContent = generateItemIcon(item, id, imageSource);

        // $('#item-' + id).css("background", "url(" + imageSource + ")
        // no-repeat"); $('#item-' + id).css("background-size", "100%");
    }
    return itemContent;
}

// generate item icons to display when users are adding to an outfit
function generateItemIcon(item, id, imageSource) {
    return '<div tabindex="-1" class="item" id="item-' + id + '" ' +
        'style="background-image: url(' + imageSource + '); ' +
        'background-size: 100%"><span>' + item[1] + '</span></div>';
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
        let outfitName = outfit[1];

        if (typeof outfitName === "undefined"
            || outfitName === null
            || outfitName === "") {
            outfitName = "<em>Unnamed outfit</em>";
        }

        let buttonHTML = '<div class="outfit-card">' +
            '<button class="outfit-btn" id="outfit-' + id + '">' + outfitName
            + '</button></div>';
        let modalHTML = '<div class="modal" id="modal-' + id + '">';
        modalHTML += '<div class="modal-content">';
        modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
        modalHTML += '<div class="fullOutfit"></div>'
        modalHTML += '<button class="delete" id="delete-outfit-' + id + '">Delete Outfit</button>';
        modalHTML += '</div></div>';


        $outfitsDiv = $('#outfits-div');

        // add modal to div id='outfits'
        $outfitsDiv.append(buttonHTML);
        $outfitsDiv.append(modalHTML);

        let outfitCard = document.getElementById("outfit-" + id);

        displayLikes(username, id, 0, outfitCard);
        generateOutfitContent(outfit, id);

        // add popup functionality to given modal
        animateOutfitModal(id);

        // add delete button functionality
        deleteUserOutfit(id);
    }
}

// function to retrieve and display a user's outfits
function displayUserOutfits(username) {
    let postParams = {
        username: username
    };

    $.post("/userOutfits", postParams, responseJSON => {
        let userOutfits = JSON.parse(responseJSON).outfits;
        if (typeof userOutfits !== "undefined" && userOutfits.length > 0) {
            generateOutfitCards(userOutfits);
        } else {
            $outfitsDiv = $("div#outfits-div");
            $outfitsDiv.append('<div class="outfit-card"' +
                ' id="no-outfits-card">' +
                'No outfits yet :)</div>')
            $noOutfitsCard = $("#no-outfits-card");

            $noOutfitsCard.css("text-align", "center");
            $noOutfitsCard.css("border", "1px solid grey");
        }
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

// function that returns the id of an item div
function getIntId(str) {
    return str.split('-')[1];
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
            // console.log(likes + " " + likeClass);
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
        })
    }

}


$(document).ready(() => {
    $('#outfits-tab-button').css("color", "white");
    username = localStorage.username;
    displayUserOutfits(username);
    $(".name").html(localStorage.getItem('name'));
});
