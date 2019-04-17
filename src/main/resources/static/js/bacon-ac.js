function dealWithCorrections(rawSuggestions, startOrDest) {
    let splitResp = rawSuggestions.replace("[\n]+", "\n").split("\n");
    let tagPrefix = "#" + startOrDest + "-actor-ac li.";

    if (splitResp.length > 0 && splitResp[0].startsWith("ERROR:")) {
        $(tagPrefix + "suggestion-1").text("Bacon database has not been" +
            " loaded yet");
        for (let j = 2; j < 4; j++) {
            $(tagPrefix + "suggestion-" + j).hide();
        }
        return;
    } else {
        // no suggestions
        if (splitResp.length == 1 || splitResp.length >= 1 && splitResp[1].trim() === "") {
            $(tagPrefix + "suggestion-1").text("No suggestions!");
            for (let j = 2; j < 4; j++) {
                $(tagPrefix + "suggestion-" + j).hide();
            }
            return;
        }
        let i;
        for (i = 1; i < splitResp.length; i++) {
            if (splitResp[i].length > 0) {
                $(tagPrefix + "suggestion-" + i).show();
                $(tagPrefix + "suggestion-" + i.toString()).text(splitResp[i]);
            } else {
                $(tagPrefix + "suggestion-" + i.toString()).hide();
            }
        }
        while (i < 4) {
            $(tagPrefix + "suggestion-" + i.toString()).hide();
            i++;
        }
    }
}

// update settings on page and display suggestions
$(document).keyup(event => {
    let startActorText = document.getElementById("start-actor").value;
    let destActorText = document.getElementById("dest-actor").value;

    let postParams = {
        startActorText: startActorText,
        destActorText: destActorText
    };

    $.post("/baconACQuery", postParams, function (response) {
        let responseObj = JSON.parse(response);
        let startCorrections = responseObj.startCorrections;
        let destCorrections = responseObj.destCorrections;

        dealWithCorrections(startCorrections, "start");
        dealWithCorrections(destCorrections, "dest");
    })
});

// replaces user input with suggestions if clicked
let $startInputArea = $("textarea#start-actor");
let $destInputArea = $("textarea#dest-actor");
$("div#start-actor-ac li[class^=suggestion-]").click(function () {
    $startInputArea.val($(this).text());
});
$("div#dest-actor-ac li[class^=suggestion-]").click(function () {
    $destInputArea.val($(this).text());
});