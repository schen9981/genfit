function displayOutfitSuggestions(username) {
    const postParams = {
        username: username
    };

    $.post("/discover", postParams, responseJSON => {
        let response = JSON.parse(responseJSON);
        let completeOutfits = response.complete;
        let incompleteOutfits = response.incomplete;
        let likedOutfitIds = response.likedOutfitIds;
        let suggestedOutfits = response.suggestion;

        // console.log(likedOutfitIds);

        // console.log(completeOutfits);
        // console.log(incompleteOutfits);
        // console.log(suggestedOutfits);
        generateSuggestionCards(suggestedOutfits);
    })
}

function generateSuggestionCards(listOfSuggestions) {
    for (let i = 0; i < listOfSuggestions.length; i++) {
        let outfitSuggestion = listOfSuggestions[i];
        // console.log(outfitSuggestion);
        let communityOutfitId = outfitSuggestion.communityOutfit.id;
        // console.log(communityOutfitId);
        let communityOutfitName = outfitSuggestion.communityOutfit.name;
        let buttonHTML = '<div class="outfit-card"><button class="outfit" id="outfit-' + communityOutfitId + '">' + communityOutfitName + '</button></div>';
        let modalHTML = '<div class="modal" id="modal-' + communityOutfitId + '">';
        modalHTML += '<div class="modal-content">';
        modalHTML += '<span class="close" id="close-' + communityOutfitId + '">&times;</span>';
        // modalHTML += '<button class="delete" id="delete-outfit-' +
        // communityOutfitId + '">Delete Outfit</button>';
        modalHTML += '</div></div>';

        if (outfitSuggestion.completeness) {
            $('#complete-outfits').append(buttonHTML);
            $('#complete-outfits').append(modalHTML);
            generateCompleteSuggestionContent(outfitSuggestion);
        } else {
            $('#incomplete-outfits').append(buttonHTML);
            $('#incomplete-outfits').append(modalHTML);
            generateIncompleteSuggestionContent(outfitSuggestion);

        }
        animateOutfitModal(communityOutfitId);
        let outfitCard = document.getElementById("outfit-" + communityOutfitId);
        displayLikes(username, communityOutfitId, 0, outfitCard);

    }
}


// generate html content for modal popup when user clicks on an outfit
function generateCompleteSuggestionContent(suggestion) {
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
        communityOutfitContent += generateItemContent(JSON.parse(responseJSON).shoes, suggestion.communityOutfit.feet);
        communityOutfitContent += '</div>';

        $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append("<h1>Someone's Outfit:</h1>");
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
            potentialOutfitContent += generateItemContent(JSON.parse(responseJSON).shoes, suggestion.userItems.feet);
            potentialOutfitContent += '</div>';

            $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append("<hr></h4><h1>Can Be Yours With:</h1>");
            $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append(potentialOutfitContent);
            $('#modal-' + suggestion.communityOutfit.id + ' .modal-content').append(
                "<button class='save-button' id='save-button-" + suggestion.communityOutfit.id + "'>Save Outfit</button>");
            document.getElementById("save-button-" + suggestion.communityOutfit.id).onclick = function () {
                saveSuggestion(username, suggestion.communityOutfit.name, suggestion.userItems.outer, suggestion.userItems.top, suggestion.userItems.bottom, suggestion.userItems.feet);
                alert("Oufit added to your closet!");
                window.location.replace("/discover");
            }
        })

    });
}

function saveSuggestion(username, outfitName, outerId, topId, bottomId, feetId) {
    let postParams = {
        username: username,
        name: outfitName,
        outer: outerId,
        top: topId,
        bottom: bottomId,
        shoes: feetId
    };

    $.post("/addOutfit", postParams, responseJSON => {
        let outfit = JSON.parse(responseJSON);
        let outfitList = [outfit];
        // console.log(outfit)
    })
}

function generateIncompleteSuggestionContent(suggestion) {

    let communityOutfit = suggestion.communityOutfit;
    let stillNeeded = suggestion.stillNeeded;
    let userItems = suggestion.userItems;
    // console.log(stillNeeded);
    // console.log(userItems);

    let communityOutfitContent = "<h1>You have these items:</h1><div class='itemListContainer' id='userItems-" + communityOutfit.id + "'></div>";
    $('#modal-' + communityOutfit.id + ' .modal-content').append(communityOutfitContent);
    // console.log(userItems);
    Object.values(userItems).forEach(function (id) {
        let postParams = {
            id: id
        };
        $.post("/singleItem", postParams, responseJSON => {
            let item = JSON.parse(responseJSON);
            let i = generateItemContent(item.item, id);
            $('#userItems-' + communityOutfit.id).append(i);
        })
    });


    let communityOutfitContentt = "<h1>You need these items:</h1><div class='itemListContainer' id='stillNeeded-" + communityOutfit.id + "'></div>";
    $('#modal-' + communityOutfit.id + ' .modal-content').append(communityOutfitContentt);
    // console.log(userItems);
    Object.values(stillNeeded).forEach(function (id) {
        let postParams = {
            id: id
        };
        $.post("/singleItem", postParams, responseJSON => {
            let item = JSON.parse(responseJSON);
            let i = generateItemContent(item.item, id);
            $('#stillNeeded-' + communityOutfit.id).append(i);
        })
    });
}

function generateItemContent(item, id) {
    // console.log(item);
    // console.log(id);
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
        //console.log(imageSource);
        itemContent = generateItemIcon(item, id, imageSource);

        // let imageSource = item[7];
        // console.log(imageSource);
        // console.log(item);
        // console.log("url(" + imageSource + ") no-repeat");
        // $('#item-' + id + "-" + outfitId).css("background", "url(" +
        // imageSource + ") no-repeat"); $('#item-' + id + "-"
        // +outfitId).css("background-size", "100%");
    }
    return itemContent;
}

function generateItemIcon(item, id, imageSource) {
    return '<div tabindex="-1" class="item" id="item-' + id + '" ' +
        'style="background: url("' + imageSource + '") no-repeat; background-size: 100%">' +
        '<span>' + item[1] + '</span></div>';
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
    $('#discover-tab-button').css("color", "white");
    username = localStorage.username;
    displayOutfitSuggestions(username);
});
