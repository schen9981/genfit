const stateQueryRoute = "/acStateQuery";
const stateUpdateRoute = "/acStateUpdate";

function getStateUpdate() {
    $.get(stateQueryRoute, (responseJSON) => {
        console.log("getting update " + responseJSON);
        let obj;
        setClickHandlers(obj);
    });
}

function updateState(stateObj) {
    console.log("update server with " + stateObj);
    $.post(stateUpdateRoute, stateObj, (response) => {
        console.log(response);
    });
}

function onOffToString(isOn) {
    if (isOn) {
        return "on";
    } else {
        return "off";
    }
}

function setClickHandlers(stateObj) {
    $('input#prefix-button').click(function () {
        $(this).toggleClass('button-off button-on');
        $(this).prop("value", "Prefix matching " + onOffToString($(this).hasClass("button-on")));
        stateObj.prefixOn = $(this).hasClass("button-on");
        // stateObj.prefixOn = true;
        updateState(stateObj);
    });
    $("input#led-button").click(function () {
        $(this).toggleClass('button-off button-on');
        $(this).prop("value", "Levenshtein edit distance " + onOffToString($(this).hasClass("button-on")));
        if ($(this).hasClass("button-on")) {
            stateObj.ledDist = parseInt($("input#led-dist").prop("value"));
        } else {
            stateObj.ledDist = 0;
        }
        updateState(stateObj);
    });
    $("input#whitespace-button").click(function () {
        $(this).toggleClass('button-off button-on');
        $(this).prop("value", "Word splitting " + onOffToString($(this).hasClass("button-on")));
        stateObj.wsOn = $(this).hasClass("button-on");
        updateState(stateObj);
    });
    $("input#smart-button").click(function () {
        $(this).toggleClass('button-off button-on');
        $(this).prop("value", "Smart matching " + onOffToString($(this).hasClass("button-on")));
        stateObj.isSmart = $(this).hasClass("button-on");
        updateState(stateObj);
    });
}

getStateUpdate();