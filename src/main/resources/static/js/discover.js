function displayOutfitSuggestions(username) {
    const postParams = {
        username : username
    };

    $.post("/discover", postParams, responseJSON => {
        let response = JSON.parse(responseJSON);
        let completeOutfits = response.completeOutfits;
        let almostOutfits = response.almostOutfits;
        let likedOutfitIds = response.likedOutfitIds;

        // console.log(likedOutfitIds);
        console.log(almostOutfits);
        console.log(completeOutfits);
    })
}

function generateSuggestionCards(listOfSuggestions) {
    for (let i = 0; i < listOfSuggestions.length; i++) {
        let outfitSuggestion = listOfSuggestions[i];

        let communityOutfitId = outfitSuggestion.communityOutfit.id;
        let communityOutfitName = outfitSuggestion.communityOutfit.name;
        let communityOutfitOuter = outfitSuggestion.communityOutfit.outer;
        let communityOutfitTop = outfitSuggestion.communityOutfit.top;
        let communityOutfitBottom = outfitSuggestion.communityOutfit.bottom;
        let communityOutfitFeet = outfitSuggestion.communityOutfit.feet;

        let buttonHTML = '<div class="outfit-card"><button class="outfit" id="outfit-' + communityOutfitId + '">' + communityOutfitName + '</button></div>';
        let modalHTML = '<div class="modal" id="modal-' + communityOutfitId + '">';
        modalHTML += '<div class="modal-content">';
        modalHTML += '<span class="close" id="close-' + communityOutfitId + '">&times;</span>';
        // modalHTML += '<button class="delete" id="delete-outfit-' + communityOutfitId + '">Delete Outfit</button>';
        modalHTML += '</div></div>';

        $('#complete-outfits').append(buttonHTML);
        $('#incomplete-outfits').append(modalHTML);

        // let outfitCard = document.getElementById("outfit-" + id);
        // displayLikes(username, id, 0, outfitCard);

        generateSuggestionContent(outfitSuggestion);

        animateOutfitModal(communityOutfitId);
    }
}


/ generate html content for modal popup when user clicks on an outfit
function generateSuggestionContent(suggestion) {
    let postParams = {
        outer: suggestion.communityOutfit.outer,
        top: suggestion.communityOutfit.top,
        bottom: suggestion.communityOutfit.bottom,
        shoes: suggestion.communityOutfit.feet
    };

    // post request to get outfit components as array
    $.post("/outfitComponents", postParams, responseJSON => {
        let communityOutfitContent = '<div class="communityOutfit">';
        communityOutfitContent += generateItemContent(JSON.parse(responseJSON).outer, suggestion.communityOutfit.outer);
        communityOutfitContent += generateItemContent(JSON.parse(responseJSON).top, suggestion.communityOutfit.top);
        communityOutfitContent += generateItemContent(JSON.parse(responseJSON).bottom, suggestion.communityOutfit.bottom);
        communityOutfitContent += generateItemContent(JSON.parse(responseJSON).shoes, communityOutfit.feet);
        communityOutfitContent += '</div>';

        $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append(communityOutfitContent);

        let postParams = {
            outer: suggestion.userItems.outer,
            top: suggestion.userItems.top,
            bottom: suggestion.userItems.bottom,
            shoes: suggestion.userItems.feet
        };

        $.post("/outfitComponents", postParams, responseJSON => {
            let potentialOutfitContent = '<div class="potentialUserOutfit">';
            potentialOutfitContent += generateItemContent(JSON.parse(responseJSON).outer, suggestion.userItems.outer);
            potentialOutfitContent += generateItemContent(JSON.parse(responseJSON).top, suggestion.userItems.top);
            potentialOutfitContent += generateItemContent(JSON.parse(responseJSON).bottom, suggestion.userItems.bottom);
            potentialOutfitContent += generateItemContent(JSON.parse(responseJSON).shoes, userItems.feet);
            potentialOutfitContent += '</div>';

            $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append(potentialOutfitContent);
        })

    });
}

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

function generateItemIcon(item, id) {
    return '<div tabindex="-1" class="item" id="item-' + id + '">' + item[1] + '</div>';
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


let nolike = 0;
let like = 1;
let unlike = -1;


function changeLikes(username, outfitId, change) {
    const postParams = {
        mode : change,
        username : username,
        outfitId : outfitId
    };
    console.log(postParams);
    $.post("/like", postParams, responseJSON => {
        console.log(JSON.parse(responseJSON).success);
        likes = JSON.parse(responseJSON.likes);
    });
}



$(document).ready(() => {
    username = localStorage.username;
    displayOutfitSuggestions(username);
});
