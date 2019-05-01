function displayOutfitSuggestions(username) {
    const postParams = {
        username : username
    };

    $.post("/discover", postParams, responseJSON => {
        let response = JSON.parse(responseJSON);
        let completeOutfits = response.completeOutfits;
        let almostOutfits = response.almostOutfits;
    })
}

$(document).ready(() => {
    username = localStorage.username;
    // displayOutfitSuggestions(username);
});

let nolike = 0;
let like = 1;
let unlike = -1;
function incrementLikes(outfitId) {
    const postParams = {
        mode : like,
        outfitId : outfitId
    };
    console.log(postParams);
    $.post("/like", postParams, responseJSON => {
        console.log(JSON.parse(responseJSON).success);
    });
}