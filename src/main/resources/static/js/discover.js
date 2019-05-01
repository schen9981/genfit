function displayOutfitSuggestions(username) {
    const postParams = {
        username : username
    };

    $.post("/discover", postParams, responseJSON => {
        let response = JSON.parse(responseJSON);
        let completeOutfits = response.completeOutfits;
        let almostOutfits = response.almostOutfits;
        let likedOutfitIds = response.likedOutfitIds;

        console.log(likedOutfitIds);
    })
}

$(document).ready(() => {
    username = localStorage.username;
    displayOutfitSuggestions(username);
});

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
    });
}