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


function displayItemsInfo(username) {
    let postParams = {
        username: username
    };

    $.post("/userItems", postParams, responseJSON => {
        //get the items of the user
        let allTypes = [];
        let allColors = [];
        let allFormality = [];
        let listOfItems = JSON.parse(responseJSON).items;
        // console.log(listOfItems);
        for (let i = 0; i < listOfItems.length; i++) {
            let item = listOfItems[i];
            allTypes.push(item[3]);
            allFormality.push(item[6]);
        }
        console.log(allFormality);
        visualizeItemTypes(allTypes);
        visualizeFormality(allFormality);
    });
}

function visualizeItemTypes(typesList) {
    let typeNumMap = {
        "TOP" : 0,
        "BOTTOM" : 0,
        "SHOES" : 0,
        "OUTER" : 0
    };

    for(let i = 0; i < typesList.length; i++) {
        typeNumMap[typesList[i]] = typeNumMap[typesList[i]] += 1;
    }

    new Chart(document.getElementById("typePie"), {
        type: 'pie',
        data: {
            labels: Object.keys(typeNumMap),
            datasets: [{
                backgroundColor: ['#EF9A9A', '#90CAF9', '#C5E1A5', '#FFE082'],
                data: Object.values(typeNumMap)
            }]
        },
        options: {
        }
    });

}

function visualizeFormality(formalityList) {
    let formalityNumMap = {
        "FORMAL" : 0,
        "BUSINESS_CASUAL" : 0,
        "CASUAL" : 0,
        "ULTRA_CASUAL" : 0
    };

    for(let i = 0; i < formalityList.length; i++) {
        formalityNumMap[formalityList[i]] = formalityNumMap[formalityList[i]] += 1;
    }

    let values = Object.values(formalityNumMap);
    let sum = values.reduce((accumulator, currentValue) => accumulator + currentValue);

    let percentages = [];

    for(let i = 0; i < values.length; i++) {
        percentages.push((values[i]/sum) * 100);
    }
    new Chart(document.getElementById("formalityRadar"), {
        type: 'radar',
        data: {
            labels: Object.keys(formalityNumMap),
            datasets: [
                {
                    label: 'Formality',
                    fill: true,
                    backgroundColor: "rgba(179,181,198,0.2)",
                    borderColor: "rgba(179,181,198,1)",
                    pointBorderColor: "#fff",
                    pointBackgroundColor: "rgba(179,181,198,1)",
                    data: Object.values(percentages)
                }
            ]
        }
    });
}

$(document).ready(() => {
   displayItemsInfo(localStorage.getItem('username'));
});