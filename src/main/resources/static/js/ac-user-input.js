function ordinal(i) {
    if (i === 1) {
        return "st";
    } else if (i === 2) {
        return "nd";
    } else if (i === 3) {
        return "rd";
    } else {
        return "th"
    }
}

function onOffToString(isOn) {
    if (isOn) {
        return "on";
    } else {
        return "off";
    }
}

// replaces user input with suggestions if clicked
let $userInputArea = $("textarea#user-input");
$("li[id^=suggestion-]").click(function () {
    $userInputArea.val($(this).text());
});

// update settings on page and display suggestions
$(document).keyup(event => {
    let userText = document.getElementById("user-input").value;

    let postParams = {
        userInput: userText,
    };

    $.get("/acStateQuery", function (response) {
        stateObj = JSON.parse(response);
        let $ledButton = $("input#led-button");
        let $prefixButton = $("input#prefix-button");
        let $whitespaceButton = $("input#whitespace-button");
        let $smartButton = $("input#smart-button");

        $ledButton.prop("value", "Levenshtein edit distance " +
            onOffToString(stateObj["ledDist"] > 0));
        $("textarea#led-dist").prop("value", stateObj["ledDist"]);
        if (stateObj["ledDist"] > 0) {
            $ledButton.css("background-color", "green");
        } else {
            $ledButton.css("background-color", "#f41a0e");
        }

        $prefixButton.prop("value", "Prefix matching "
            + onOffToString(stateObj["prefixOn"]));
        if (stateObj["prefixOn"]) {
            $prefixButton.css("background-color", "green");
        } else {
            $prefixButton.css("background-color", "#f41a0e");
        }

        $whitespaceButton.prop("value", "Word splitting "
            + onOffToString(stateObj["wsOn"]));
        if (stateObj["wsOn"]) {
            $whitespaceButton.css("background-color", "green");
        } else {
            $whitespaceButton.css("background-color", "#f41a0e");
        }

        $smartButton.prop("value", "Smart matching " + onOffToString(stateObj["smartOn"]));
        if (stateObj["smartOn"]) {
            $smartButton.css("background-color", "green");
        } else {
            $smartButton.css("background-color", "#f41a0e");
        }
    });

    $.post("/acQuery", postParams, function (response) {
        let splitResp = response.replace("[\n]+", "\n").split("\n");
        if (splitResp.length > 0 && splitResp[0].startsWith("ERROR:")) {
            $("#suggestion-1").text(splitResp[0]);
            for (let j = 2; j < 6; j++) {
                $("#suggestion-" + j).hide();
            }
            return;
        } else {
            // no suggestions
            if (splitResp.length == 1 || splitResp.length >= 1 && splitResp[1].trim() === "") {
                $("#suggestion-1").text("No suggestions!");
                for (let j = 2; j < 6; j++) {
                    $("#suggestion-" + j).hide();
                }
                return;
            }
            let i;
            for (i = 1; i < splitResp.length; i++) {
                $("#suggestion-" + i).show();
                $("#suggestion-" + i.toString()).text(splitResp[i]);
            }
            while (i < 6) {
                $("#suggestion-" + i.toString()).hide();
                i++;
            }
        }
    })
});