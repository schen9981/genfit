document.getElementById("form-wrapper").style.display = "none";
$(".name").html(localStorage.getItem('name'));

function openForm() {
    document.getElementById("change-btn").style.display = "none";
    document.getElementById("form-wrapper").style.display = "block";
}

function closeForm() {
    document.getElementById("change-btn").style.display = "block";
    document.getElementById("form-wrapper").style.display = "none";
    document.getElementById("currentpwd").value = document.getElementById("currentpwd").defaultValue;
    document.getElementById("newpwd").value = document.getElementById("newpwd").defaultValue;
}

function displayOutfitsInfo(username) {
    let postParams = {
        username: username
    };
    $.post("/userOutfits", postParams, responseJSON => {
        let userOutfits = JSON.parse(responseJSON).outfits;
        document.getElementById('outfit-overview').innerHTML = "<hr></hr><h2><u>You have " + userOutfits.length + " outfits!</u></h2>";
    });
}

function displayItemsInfo(username) {
    let postParams = {
        username: username
    };

    $.post("/userItems", postParams, responseJSON => {
        //get the items of the user
        let allSeaons = [];
        let allColors = [];
        let allFormality = [];
        let listOfItems = JSON.parse(responseJSON).items;
        // console.log(listOfItems);
        for (let i = 0; i < listOfItems.length; i++) {
            let item = listOfItems[i];
            // console.log(item);
            allSeaons.push(item[6]);
            allFormality.push(item[7]);
            allColors.push(item[2]);
        }
        // console.log(allFormality);
        visualizeItemSeason(allSeaons);
        visualizeFormality(listOfItems);
        visualizeItemColors(allColors);
    });
}

function visualizeItemColors(colorsList) {
    // console.log(colorsList);
    let colors = [];
    // console.log(colors);


    colorsList.forEach(function (color) {
        if (color === "0") {
            colors.push("#000000");
        } else {
            let finalColor = color;
            if (color.length !== 6) {
                let zeroNum = 6 - color.length;
                for (let i = 1; i <= zeroNum; i++) {
                    finalColor = "0" + finalColor;
                }
            }
            colors.push("#" + finalColor);
        }
    });
    console.log(colors);
    const rgbArr = colors.map(hexToRgb);
    const sortedRgbArr = sortColors(rgbArr);
    const finalArray = sortedRgbArr.map(rgbToHex);
    displayColors('clothingGradient', finalArray);
}


function visualizeItemSeason(seasonsList) {
    let typeNumMap = {
        "SPRING": 0,
        "SUMMER": 0,
        "FALL": 0,
        "WINTER": 0
    };

    for (let i = 0; i < seasonsList.length; i++) {
        typeNumMap[seasonsList[i]] = typeNumMap[seasonsList[i]] += 1;
    }

    new Chart(document.getElementById("typePie"), {
        type: 'pie',
        data: {
            labels: Object.keys(typeNumMap),
            datasets: [{
                backgroundColor: ['#C5E1A5', '#FFE082', '#EF9A9A', '#90CAF9'],
                data: Object.values(typeNumMap)
            }]
        },
        options: {}
    });

}

function visualizeFormality(formalityList) {
    // console.log(formalityList);
    let formalityNumMap = {
        "OUTER": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "TOP": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "BOTTOM": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "SHOES": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        }
    };
    // console.log(formalityNumMap);

    formalityList.forEach(function (item) {
        formalityNumMap[item[3]][item[7]] = formalityNumMap[item[3]][item[7]] + 1;
    });

    // console.log(formalityNumMap);

    let formalityPercentMap = {
        "OUTER": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "TOP": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "BOTTOM": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        },
        "SHOES": {
            "FORMAL": 0,
            "BUSINESS_CASUAL": 0,
            "CASUAL": 0,
            "ULTRA_CASUAL": 0
        }
    };

    Object.keys(formalityNumMap).forEach(function (type) {
        let values = Object.values(formalityNumMap[type]);
        // console.log(values);
        let sum = values.reduce((accumulator, currentValue) => accumulator + currentValue);
        // console.log(sum);
        Object.keys(formalityNumMap[type]).forEach(function (formality) {
            // console.log(formality);
            formalityPercentMap[type][formality] = (formalityNumMap[type][formality] / sum) * 100;
            // console.log(formalityPercentMap[type][formality]);
        })
    });
    console.log(formalityPercentMap);

    new Chart(document.getElementById("formalityRadar"), {
        type: 'radar',
        data: {
            labels: Object.keys(formalityNumMap["TOP"]),
            datasets: [
                {
                    label: 'OUTER',
                    fill: true,
                    borderWidth: 2,
                    backgroundColor: "rgba(255, 204, 102, 0.2)",
                    borderColor: "rgba(255, 204, 102, 1)",
                    pointBackgroundColor: "rgba(255, 204, 102,1)",
                    pointBorderColor: "#fff",
                    data: Object.values(formalityPercentMap['OUTER'])
                }, {
                    label: 'TOP',
                    fill: true,
                    borderWidth: 2,
                    backgroundColor: "rgba(255,99,132,0.2)",
                    borderColor: "rgba(255,99,132,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(255,99,132,1)",
                    data: Object.values(formalityPercentMap['TOP'])
                }, {
                    label: 'BOTTOM',
                    fill: true,
                    borderWidth: 2,
                    backgroundColor: "rgba(68,181,238,0.2)",
                    borderColor: "rgba(68,181,238,1)",
                    pointBackgroundColor: "rgba(179,181,198,1)",
                    pointBorderColor: "#fff",
                    data: Object.values(formalityPercentMap['BOTTOM'])
                }, {
                    label: 'SHOES',
                    fill: true,
                    borderWidth: 2,
                    backgroundColor: "rgba(75,192,192,0.3)",
                    borderColor: "rgba(75,192,192,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(75,192,192,0.5)",
                    data: Object.values(formalityPercentMap['SHOES'])
                }
            ]
        }
    });
}

$(document).ready(() => {
    displayItemsInfo(localStorage.getItem('username'));
    displayOutfitsInfo(localStorage.getItem('username'));
});