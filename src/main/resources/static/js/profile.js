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

function createStatCircle(count, label) {
  let contents = '<div class="count"><h2>' + count + '</h2></div>';
  contents += '<div class="label"><h2>' + label + '</h2></div>';

  return contents;
}

function displayOutfitsInfo(username) {
    let postParams = {
        username: username
    };
    $.post("/userOutfits", postParams, responseJSON => {
        let userOutfits = JSON.parse(responseJSON).outfits;
        document.getElementById('outfit-count').innerHTML = createStatCircle(userOutfits.length, 'outfits');
    });
}

function displayItemCount(username) {
  let postParams = {
    username: username
  };
  $.post("/userItems", postParams, responseJSON => {
    let userItems = JSON.parse(responseJSON).items;
    document.getElementById('item-count').innerHTML = createStatCircle(userItems.length, 'items');
  } )
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
        visualizeItemSeason(listOfItems);
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
    // console.log(colors);
    const rgbArr = colors.map(hexToRgb);
    // console.log(rgbArr);

    // something went wrong
    const sortedRgbArr = sortColors(rgbArr);
    // console.log(sortedRgbArr);
    const finalArray = sortedRgbArr.map(rgbToHex);
    // console.log(finalArray);
    displayColors('clothingGradient', finalArray);
}



function visualizeItemSeason(seasonsList) {
    let outerTypeNumMap = {
        "SPRING": 0,
        "SUMMER": 0,
        "FALL": 0,
        "WINTER": 0
    };

    let topTypeNumMap = {
        "SPRING": 0,
        "SUMMER": 0,
        "FALL": 0,
        "WINTER": 0
    };


    let bottomTypeNumMap = {
        "SPRING": 0,
        "SUMMER": 0,
        "FALL": 0,
        "WINTER": 0
    };


    let shoesTypeNumMap = {
        "SPRING": 0,
        "SUMMER": 0,
        "FALL": 0,
        "WINTER": 0
    };

    seasonsList.forEach(function(item) {
        let type = item[3];
        let season = item[6];
        if (type === "OUTER") {
            outerTypeNumMap[season] += 1
        } else if (type === "TOP") {
            topTypeNumMap[season] += 1
        } else if (type === "BOTTOM") {
            bottomTypeNumMap[season] += 1
        } else if (type === "SHOES") {
            shoesTypeNumMap[season] += 1
        }
    });

    console.log(outerTypeNumMap);
    console.log(topTypeNumMap);
    console.log(bottomTypeNumMap);
    console.log(shoesTypeNumMap);

    // let typeNumMap = {
    //     "SPRING": 0,
    //     "SUMMER": 0,
    //     "FALL": 0,
    //     "WINTER": 0
    // };

    // for (let i = 0; i < seasonsList.length; i++) {
    //     typeNumMap[seasonsList[i]] = typeNumMap[seasonsList[i]] += 1;
    // }
    // console.log(typeNumMap);

    new Chart(document.getElementById("typePie"), {
        type: 'doughnut',
        data: {

            datasets: [{
                label: "one",
                labels: Object.keys(outerTypeNumMap),
                backgroundColor: ['#C5E1A5', '#FFE082', '#EF9A9A', '#90CAF9'],
                data: Object.values(outerTypeNumMap)
            },
                {
                    label: "two",
                    labels: Object.keys(topTypeNumMap),
                    backgroundColor: ['#C5E1A5', '#FFE082', '#EF9A9A', '#90CAF9'],
                    data: Object.values(topTypeNumMap)
                },
                {
                    label: "two",
                    labels: Object.keys(bottomTypeNumMap),
                    backgroundColor: ['#C5E1A5', '#FFE082', '#EF9A9A', '#90CAF9'],
                    data: Object.values(bottomTypeNumMap)
                },
                {
                    label: "two",
                    labels: Object.keys(shoesTypeNumMap),
                    backgroundColor: ['#C5E1A5', '#FFE082', '#EF9A9A', '#90CAF9'],
                    data: Object.values(shoesTypeNumMap)
                }],
            labels: Object.keys(topTypeNumMap)
        },
        options: {
            title: {
                display: true,
                text: 'OUTER > TOP > BOTTOM > SHOES'
            }

            // tooltips: {
            //     callbacks: {
            //         label: function(tooltipItem, data) {
            //             let dataset = data.datasets[tooltipItem.datasetIndex];
            //             let index = tooltipItem.index;
            //             return dataset.labels[index] + ': ' + dataset.data[index];
            //         }
            //     }
            // }
        }
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
    // console.log(formalityPercentMap);

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
                    pointBackgroundColor: "rgba(68,181,238,1)",
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
    $('#user-tab-button').css("color", "white");
    displayItemsInfo(localStorage.getItem('username'));
    displayOutfitsInfo(localStorage.getItem('username'));
    displayItemCount(localStorage.getItem('username'));
});
