// map from id -> item array (?)
// let itemCache = new Map([]);
// username of current user

let bucketName = 'cs32-term-project-s3-bucket';
let bucketRegion = 'us-east-1';
let IdentityPoolId = 'us-east-1:36e7dd92-26e7-4a4e-ac8f-5b91ee5968ef';

AWS.config.update({
    region: bucketRegion,
    credentials: new AWS.CognitoIdentityCredentials({
        IdentityPoolId: IdentityPoolId
    })
});

let s3 = new AWS.S3({
    apiVersion: '2006-03-01',
    params: {Bucket: bucketName}
});

let username;
$(".name").html(localStorage.getItem('name'));

// generate html content (ie. item information) for card
function generateCardContent(item) {
  // item represented as array [id, name, color, type, subtype, pattern, season, formality, image src]
  let itemContent = '<div class="item-content">';
  itemContent += '<h1 id="name-item-' + item[0] + '">' +  item[1]  + '</h1><hr>';
  itemContent += '<div class="item-content-1">';
  itemContent += '<div class="content-elem">';
  itemContent += '<img src="' + item[8] + '"></div>';
  itemContent += '<div class="content-elem">';
  itemContent += '<div class="color-div">';
  itemContent += '<h3>Color</h3><hr>';
  itemContent += '<div class="rect-color" style="background-color:#' + parseHex(item[2]) + ';"></div>'
  itemContent += '<span>#' + parseHex(item[2]) + '</span></div>';
  itemContent += '<div class="type-div">';
  itemContent += '<h3>Type</h3><hr>';
  itemContent += '<p>' + parseItemText(item[3]) + ' - ' + parseItemText(item[4]) + '</p></div>';
  itemContent += '</div></div>';
  itemContent += '<div class="item-content-2">';
  itemContent += '<div class="pattern-div">';
  itemContent += '<h3>Pattern</h3><hr>';
  itemContent += '<p>' + parseItemText(item[5]) + '</p></div>';
  itemContent += '<div class="season-div">';
  itemContent += '<h3>Season</h3><hr>';
  itemContent += '<p>' + parseItemText(item[6]) + '</p></div>';
  itemContent += '<div class="formality-div">';
  itemContent += '<h3>Formality</h3><hr>';
  itemContent += '<p>' + parseItemText(item[7]) + '</p></div></div></div>';

  return itemContent;
}

// parse the item attribute enum into text to be displayed
function parseItemText(attr) {
  // remove all underscores and lowercase
  let text = attr.replace(/_/g, " ").toLowerCase();
  // convert first character into uppercase
  let toReturn = text.charAt(0).toUpperCase() + text.slice(1);

  return toReturn;
}

// parse the color string into a hex
function parseHex(colorStr) {
  let numMissing = 6 - colorStr.length;
  let hexStr = colorStr;
  for (i = 0; i < numMissing; i++) {
    hexStr = '0' + hexStr;
  }
  return hexStr;
}

// animate modal for popup functionality
function animateItemModal(itemId) {
  // id of the html elements
  let mId = '#modal-' + itemId;
  let bId = '#item-' + itemId;
  let sId = '#close-' + itemId;

  // get the elements of the page
  let modal = $(mId);
  let btn = $(bId);
  let span = $(sId);

  // open modal when button clicked
  btn.click(function() {
    modal.css("display", "block");
  });

  // close modal when user clicks 'x'
  span.click(function() {
    modal.css("display", "none");
  });
}

// generates the modal/cards for each item from a list
function generateCards(listOfItems) {
  for (let i = 0; i < listOfItems.length; i++) {
    // get current item json
    let item = listOfItems[i];

    // generate modal html

    let id = item[0];
    let imageWidth = $(window).width() * 0.1;
    let imageHeight = imageWidth * 1.2;
    let buttonHTML = '<div class="item"><button class="item-button" id="item-'
        + id + '"></button><div style="text-align:center;">' + item[1] + '</div></div>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateCardContent(item);
    modalHTML += "<div class='buttons'>";
    modalHTML += '<button class="edit-button" id="edit-item-' + id + '">Edit Item</button></div>';
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('.bottom .main #items').append(buttonHTML);
    $('.bottom .main #items').append(modalHTML);

    // Set image
    let imageSource = item[8];
    $('#item-' + id).css("background", "url(" + imageSource + ") no-repeat");
    $('#item-' + id).css("background-size", "contain");

    // add popup functionality to given modal
    animateItemModal(id);

    // add edit/delete button functionality
    editItem(id, item);
  }
}

// return input element for color
function populateColorEdit(color) {
  let colorEdit = '<h3>Color</h3><hr><div id="item-colors" style="display:inline-block;">';
  colorEdit += '<input type="color" id="item-color" name="item-color" value="#' + color + '"></div>';

  return colorEdit;
}

// return input element for type/subtype
function populateTypeEdit(type, subtype) {

  let typeEdit = '<h3>Type</h3><hr><select id="type-1" name="item-type-1">';

  if (type == 'OUTER') {
    typeEdit += '<option value="OUTER" selected>Outerwear</option>';
    typeEdit += '<option value="TOP">Top</option>';
    typeEdit += '<option value="BOTTOM">Bottom</option>';
    typeEdit += '<option value="SHOES">Shoes</option></select>';
    typeEdit += '<select id="type-2" name="item-type-2">';
    if (subtype == 'OUTER_COAT') {
      typeEdit += '<option value="OUTER_COAT" selected>Outer Coat</option>';
      typeEdit += '<option value="SUIT">Suit</option></select>';
    } else if (subtype == 'SUIT') {
      typeEdit += '<option value="OUTER_COAT">Outer Coat</option>';
      typeEdit += '<option value="SUIT" selected>Suit</option></select>';
    }
  } else if (type == 'TOP') {
    typeEdit += '<option value="OUTER" selected>Outerwear</option>';
    typeEdit += '<option value="TOP" selected>Top</option>';
    typeEdit += '<option value="BOTTOM">Bottom</option>';
    typeEdit += '<option value="SHOES">Shoes</option></select>';
    typeEdit += '<select id="type-2" name="item-type-2">';
    if (subtype == 'SHIRT_BLOUSE') {
      typeEdit += '<option value="SHIRT_BLOUSE" selected>Shirt/Blouse</option>';
      typeEdit += '<option value="T_SHIRT">T-Shirt</option>';
      typeEdit += '<option value="SWEATER">Sweater</option>';
      typeEdit += '<option value="JACKET">Jacket</option>';
    } else if (subtype == 'T_SHIRT') {
      typeEdit += '<option value="SHIRT_BLOUSE">Shirt/Blouse</option>';
      typeEdit += '<option value="T_SHIRT" selected>T-Shirt</option>';
      typeEdit += '<option value="SWEATER">Sweater</option>';
      typeEdit += '<option value="JACKET">Jacket</option>';
    } else if (subtype == 'SWEATER') {
      typeEdit += '<option value="SHIRT_BLOUSE">Shirt/Blouse</option>';
      typeEdit += '<option value="T_SHIRT">T-Shirt</option>';
      typeEdit += '<option value="SWEATER" selected>Sweater</option>';
      typeEdit += '<option value="JACKET">Jacket</option>';
    } else if (subtype == 'JACKET') {
      typeEdit += '<option value="SHIRT_BLOUSE">Shirt/Blouse</option>';
      typeEdit += '<option value="T_SHIRT">T-Shirt</option>';
      typeEdit += '<option value="SWEATER">Sweater</option>';
      typeEdit += '<option value="JACKET" selected>Jacket</option>';
    }
  } else if (type == 'BOTTOM') {
    typeEdit += '<option value="OUTER" selected>Outerwear</option>';
    typeEdit += '<option value="TOP">Top</option>';
    typeEdit += '<option value="BOTTOM" selected>Bottom</option>';
    typeEdit += '<option value="SHOES">Shoes</option></select>';
    typeEdit += '<select id="type-2" name="item-type-2">';
    if (subtype == 'PANTS') {
      typeEdit += '<option value="PANTS" selected>Pants</option>';
      typeEdit += '<option value="SKIRT">Skirt</option>';
      typeEdit += '<option value="DRESS">Dress</option>';
      typeEdit += '<option value="SHORTS">Shorts</option>';
    } else if (subtype == 'SKIRT') {
      typeEdit += '<option value="PANTS">Pants</option>';
      typeEdit += '<option value="SKIRT" selected>Skirt</option>';
      typeEdit += '<option value="DRESS">Dress</option>';
      typeEdit += '<option value="SHORTS">Shorts</option>';
    } else if (subtype == 'DRESS') {
      typeEdit += '<option value="PANTS">Pants</option>';
      typeEdit += '<option value="SKIRT">Skirt</option>';
      typeEdit += '<option value="DRESS" selected>Dress</option>';
      typeEdit += '<option value="SHORTS">Shorts</option>';
    } else if (subtype == 'SHORTS') {
      typeEdit += '<option value="PANTS">Pants</option>';
      typeEdit += '<option value="SKIRT">Skirt</option>';
      typeEdit += '<option value="DRESS">Dress</option>';
      typeEdit += '<option value="SHORTS" selected>Shorts</option>';
    }
  } else if (type == 'SHOES') {
    typeEdit += '<option value="OUTER">Outerwear</option>';
    typeEdit += '<option value="TOP">Top</option>';
    typeEdit += '<option value="BOTTOM">Bottom</option>';
    typeEdit += '<option value="SHOES" selected>Shoes</option></select>';
    typeEdit += '<select id="type-2" name="item-type-2">';
    if (subtype == 'SNEAKERS') {
      typeEdit += '<option value="SNEAKERS" selected>Sneakers</option>';
      typeEdit += '<option value="BOOTS">Boots</option>';
      typeEdit += '<option value="SANDALS">Sandals</option>';
      typeEdit += '<option value="DRESS_SHOES">Dress Shoes</option>';
    } else if (subtype == 'BOOTS') {
      typeEdit += '<option value="SNEAKERS">Sneakers</option>';
      typeEdit += '<option value="BOOTS" selected>Boots</option>';
      typeEdit += '<option value="SANDALS">Sandals</option>';
      typeEdit += '<option value="DRESS_SHOES">Dress Shoes</option>';
    } else if (subtype == 'SANDALS') {
      typeEdit += '<option value="SNEAKERS">Sneakers</option>';
      typeEdit += '<option value="BOOTS">Boots</option>';
      typeEdit += '<option value="SANDALS" selected>Sandals</option>';
      typeEdit += '<option value="DRESS_SHOES">Dress Shoes</option>';
    } else if (subtype == 'DRESS_SHOES') {
      typeEdit += '<option value="SNEAKERS">Sneakers</option>';
      typeEdit += '<option value="BOOTS">Boots</option>';
      typeEdit += '<option value="SANDALS">Sandals</option>';
      typeEdit += '<option value="DRESS_SHOES" selected>Dress Shoes</option>';
    }
  }

  return typeEdit;
}

function populatePatternEdit(pattern) {
  let patternEdit = '<h3>Pattern</h3><hr><select id="item-pattern" name="item-pattern">';

  if (pattern == 'SOLID') {
    patternEdit += '<option value="SOLID" selected>Solid</option>';
    patternEdit += '<option value="STRIPED">Striped</option>';
    patternEdit += '<option value="CHECKERED">Checkered</option>';
    patternEdit += '<option value="OTHER">Other</option></select>';
  } else if (pattern == 'STRIPED') {
    patternEdit += '<option value="SOLID">Solid</option>';
    patternEdit += '<option value="STRIPED" selected>Striped</option>';
    patternEdit += '<option value="CHECKERED">Checkered</option>';
    patternEdit += '<option value="OTHER">Other</option></select>';
  } else if (pattern == 'CHECKERED') {
    patternEdit += '<option value="SOLID">Solid</option>';
    patternEdit += '<option value="STRIPED">Striped</option>';
    patternEdit += '<option value="CHECKERED" selected>Checkered</option>';
    patternEdit += '<option value="OTHER">Other</option></select>';
  } else if (pattern == 'OTHER') {
    patternEdit += '<option value="SOLID">Solid</option>';
    patternEdit += '<option value="STRIPED">Striped</option>';
    patternEdit += '<option value="CHECKERED">Checkered</option>';
    patternEdit += '<option value="OTHER" selected>Other</option></select>';
  }

  return patternEdit;
}

function populateSeasonEdit(season) {
  let seasonEdit = '<h3>Season</h3><hr><select id="item-season" name="item-season">';

  if (season == 'SPRING') {
    seasonEdit += '<option value="SPRING" selected>Spring</option>';
    seasonEdit += '<option value="SUMMER">Summer</option>';
    seasonEdit += '<option value="FALL">Fall</option>';
    seasonEdit += '<option value="WINTER">Winter</option></select>';
  } else if (season == 'SUMMER') {
    seasonEdit += '<option value="SPRING">Spring</option>';
    seasonEdit += '<option value="SUMMER" selected>Summer</option>';
    seasonEdit += '<option value="FALL">Fall</option>';
    seasonEdit += '<option value="WINTER">Winter</option></select>';
  } else if (season == 'FALL') {
    seasonEdit += '<option value="SPRING">Spring</option>';
    seasonEdit += '<option value="SUMMER">Summer</option>';
    seasonEdit += '<option value="FALL" selected>Fall</option>';
    seasonEdit += '<option value="WINTER">Winter</option></select>';
  } else if (season == 'WINTER') {
    seasonEdit += '<option value="SPRING">Spring</option>';
    seasonEdit += '<option value="SUMMER">Summer</option>';
    seasonEdit += '<option value="FALL">Fall</option>';
    seasonEdit += '<option value="WINTER" selected>Winter</option></select>';
  }

  return seasonEdit;
}

function populateFormalityEdit(formality) {
  let formalityEdit = '<h3>Formality</h3><hr><select id="item-formality" name="item-formality">';

  if (formality == 'FORMAL') {
    formalityEdit += '<option value="FORMAL" selected>Formal</option>';
    formalityEdit += '<option value="BUSINESS_CASUAL">Business Casual</option>';
    formalityEdit += '<option value="CASUAL">Casual</option>';
    formalityEdit += '<option value="ULTRA_CASUAL">Ultra Casual</option></select>';
  } else if (formality == 'BUSINESS_CASUAL') {
    formalityEdit += '<option value="FORMAL">Formal</option>';
    formalityEdit += '<option value="BUSINESS_CASUAL" selected>Business Casual</option>';
    formalityEdit += '<option value="CASUAL">Casual</option>';
    formalityEdit += '<option value="ULTRA_CASUAL">Ultra Casual</option></select>';
  } else if (formality == 'CASUAL') {
    formalityEdit += '<option value="FORMAL">Formal</option>';
    formalityEdit += '<option value="BUSINESS_CASUAL">Business Casual</option>';
    formalityEdit += '<option value="CASUAL" selected>Casual</option>';
    formalityEdit += '<option value="ULTRA_CASUAL">Ultra Casual</option></select>';
  } else if (formality == 'ULTRA_CASUAL') {
    formalityEdit += '<option value="FORMAL">Formal</option>';
    formalityEdit += '<option value="BUSINESS_CASUAL">Business Casual</option>';
    formalityEdit += '<option value="CASUAL">Casual</option>';
    formalityEdit += '<option value="ULTRA_CASUAL" selected>Ultra Casual</option></select>';
  }

  return formalityEdit;
}


function editItem(itemId, item) {
  let colorEdit = populateColorEdit(parseHex(item[2]));
  let typeEdit = populateTypeEdit(item[3], item[4]);
  let patternEdit = populatePatternEdit(item[5]);
  let seasonEdit = populateSeasonEdit(item[6]);
  let formalityEdit = populateFormalityEdit(item[7]);

  // event handler for edit item button
  $('#edit-item-' + itemId).on('click', function (e) {

    // replace div contents with input fields
    $('.color-div').html(colorEdit);
    $('.type-div').html(typeEdit);
    $('.pattern-div').html(patternEdit);
    $('.season-div').html(seasonEdit);
    $('.formality-div').html(formalityEdit);

    // event listeners for change input
    let newColor = $('#item-color').val();
    let newType1 = $('#type-1').val();
    let newType2 = $('#type-2').val();
    console.log(newType2);
    let newPattern = $('#item-pattern').val();
    let newSeason = $('#item-season').val();
    let newFormality = $('#item-formality').val();

    $('.color-div #item-colors #item-color').on('change', function(e) {
      newColor = e.target.value;
    });

    $('.type-div #type-1').on('change', function(e) {
      newType1 = e.target.value;
    });

    $('.type-div #type-2').on('change', function(e) {
      newType2 = e.target.value;
    });

    $('.pattern-div #item-pattern').on('change', function(e) {
      newPattern = e.target.value;
    });

    $('.season-div #item-season').on('change', function(e) {
      newSeason = e.target.value;
    });

    $('.formality-div #item-formality').on('change', function(e) {
      newFormality = e.target.value;
    });

    dynamicTypeDropdown("type-div");

    // hide edit button
    $('#edit-item-' + itemId).css("display", "none");

    // add submit changes button
    let submitChangesButton = '<button class="submit-change-button" id="submit-edit-' + itemId + '">Submit Changes</button>';
    $('#modal-' + itemId + ' .modal-content .buttons').append(submitChangesButton);
    $('#submit-edit-' + itemId).on('click', function (e) {
      let postParams = {
        itemId: itemId,
        itemName: $('#name-item-' + itemId).text(),
        itemColor: newColor,
        itemType1: newType1,
        itemType2: newType2,
        itemPattern: newPattern,
        itemSeason: newSeason,
        itemFormality: newFormality
      };

      // post request to update item in database
      $.post("/editItem", postParams, responseJSON => {
          let item = JSON.parse(responseJSON);
          let newCardContent = '<span class="close" id="close-' + itemId + '">&times;</span>';
          newCardContent += generateCardContent(item);
          newCardContent += "<div class='buttons'>";
          newCardContent += '<button class="edit-button" id="edit-item-' + itemId + '">Edit Item</button></div>';
          newCardContent += '</div>';

          // repopulate modal with new card contents
          $('#modal-' + itemId + ' .modal-content').html(newCardContent);

          animateItemModal(itemId);
      });
    });


    // show delete button
    deleteUserItem(itemId);
  })
}

// // button that handles submtting changes
// function submitChanges(itemId, color, type1, type2, pattern, season, formality) {
//
// }

// add button functionality to remove an item
function deleteUserItem(itemId) {
  // add delete button
  let deleteButton = '<button class="delete-button" id="delete-item-' + itemId + '">Delete Item</button>';
  $('#modal-' + itemId + ' .modal-content .buttons').append(deleteButton);

  // event handler for removing item
  $('#delete-item-' + itemId).on('click', function(e) {

      // let postParams = {}
      let postParams = {
          id: itemId
      };

      $.post("/getOutfitWithItem", postParams, responseJSON => {

          let outfitIds = JSON.parse(responseJSON).outfitIds;

          if (outfitIds.length !== 0) {
              let confrimation = confirm("Do you have " + outfitIds.length + " outfits with this item. " +
                  "Are you sure you want to delete it?");
              if (confrimation === true) {
                  console.log("delete");


                  outfitIds.forEach(function(outfitId) {
                      let postParams = {
                          username : username,
                          outfitId : outfitId
                      };
                      console.log("deleting outfit#" + outfitId);
                      $.post("/deleteOutfit", postParams, responseJSON => {
                          console.log(JSON.parse(responseJSON));
                      })
                  });

                  let postParams = {
                      username: username,
                      itemId: itemId
                  };
                    console.log("delete item");
                  // post request to remove item
                  $.post("/deleteItem", postParams, responseJSON => {
                      $('#item-' + itemId).remove();
                      $('#modal-' + itemId).remove();
                      let imageKey = JSON.parse(responseJSON)[0];
                      if (imageKey !== "default") {
                          s3.deleteObject({Key: imageKey}, function(err, data) {
                              if (err) {
                                  alert('There was an error deleting your photo: ', err.message);
                              } else {
                                  console.log('Successfully deleted photo.');
                              }
                          });
                      }
                  });
                  window.location.reload();
              } else {
                  console.log("dont delete");
              }
          }
      });

  });
}

// function to retrieve and display user items
function displayUserItems(username) {
  // post request to /userItems to retrieve user's items
  let postParams = {
    username: username
  };

  $.post("/userItems", postParams, responseJSON => {
    //get the items of the user
    let userItems = JSON.parse(responseJSON).items;
    generateCards(userItems);
  });
}

// function that dynamically populates second type dropdown
function dynamicTypeDropdown(className) {
  let type1Selector;
  let type2Selector;
  if (className != null) {
    type1Selector = '.' + className + ' #type-1';
    type2Selector = '.' + className + ' #type-2';
  } else {
    type1Selector = '#type-1';
    type2Selector = '#type-2';
  }
  $(type1Selector).on('change', function(e){
    $(type2Selector).html('');
    if (e.target.value == "OUTER") {
        $(type2Selector).append('<option value="OUTER_COAT">Outer Coat</option>');
        $(type2Selector).append('<option value="SUIT">Suit</option>');
    } else if (e.target.value == "TOP") {
      $(type2Selector).append('<option value="SHIRT_BLOUSE">Shirt/Blouse</option>');
      $(type2Selector).append('<option value="T_SHIRT">T-Shirt</option>');
      $(type2Selector).append('<option value="SWEATER">Sweater</option>');
      $(type2Selector).append('<option value="JACKET">Jacket</option>');
    } else if (e.target.value == "BOTTOM") {
      $(type2Selector).append('<option value="PANTS">Pants</option>');
      $(type2Selector).append('<option value="SKIRT">Skirt</option>');
      $(type2Selector).append('<option value="DRESS">Dress</option>');
      $(type2Selector).append('<option value="SHORTS">Shorts</option>');
    } else {
      $(type2Selector).append('<option value="SNEAKERS">Sneakers</option>');
      $(type2Selector).append('<option value="BOOTS">Boots</option>');
      $(type2Selector).append('<option value="SANDALS">Sandals</option>');
      $(type2Selector).append('<option value="DRESS_SHOES">Dress Shoes</option>');
    }
  });
}

// function that allows for users to add more colors to outfit
function additionalColorForm() {
  $('#addColor').on('click', function(e) {
    $('#item-colors').append('<input type="color" name="item-color" value="#ff0000">');
    e.preventDefault();
  });
}

// function that animates the add item modal
function itemModalAnimation() {
  additionalColorForm();
  dynamicTypeDropdown();

  let modal = $('#addItemModal');
  let btn = $('#addItem');
  let span = $('#addSpan');

  // open modal when button clicked
  btn.click(function() {
      modal.css("display", "block");
  });

  // close modal when user clicks 'x'
  span.click(function() {
    modal.css("display", "none");
  });
}

let encodedImage = "";

//function fot previewing user uploaded images
function previewFile(){
    let preview = $('#image-preview').get(0); //selects the query named img
    let fileElement    = $('#image-input').get(0); //sames as here
    let file = fileElement.files[0];
    let reader = new FileReader();
    reader.onloadend = function (e) {
        preview.src = reader.result;
        encodedImage = reader.result.split(",")[1];
    };

    if (file) {
        reader.readAsDataURL(file); //reads the data as a URL
    } else {
        preview.src = "";
    }
}

function addItemFormSubmit() {
  $('#addItemForm').on("submit", function(e) {
    e.preventDefault();
    // get parameters for post request from form
    let postParams = {
      username: username,
      itemName: $('#item-name').val(),
      itemColor: $('#item-color').val(),
      itemType1: $('#type-1').val(),
      itemType2: $('#type-2').val(),
      itemPattern: $('#item-pattern').val(),
      itemSeason: $('#item-season').val(),
      itemFormality: $('#item-formality').val(),
      imageKey: "default"
    }
    //check if user uploaded an image
    if ($('#image-input').get(0).files.length !== 0) {
      let file = $('#image-input').get(0).files[0];
      uploadImageAndPost(username, file, postParams);
    } else {
      addItemPost(postParams);
    }
  });
}

// post request to addItems
function addItemPost(postParams) {
  $.post("/addItem", postParams, responseJSON => {
      let item = JSON.parse(responseJSON);
      let itemList = [item];
      generateCards(itemList);
      $('#addItemModal').css("display", "none");
      $('#image-input').val('');
      $('#image-preview').get(0).src = '';
  });
}

//attempts to upload image and calls addItemPost accordingly
function uploadImageAndPost(username, file, postParams) {
    let time = new Date().getTime();
    let filename = file.name;
    let photoKey = username.replace("@", ".") + "/" + time + "_" + filename;
    s3.upload({
        Key: photoKey,
        Body: file,
        ACL: 'public-read'
    }, function(err, data) {
        if (err) {
            alert("Error in file upload, using default images");
            addItemPost(postParams);
        } else {
            postParams.imageKey = photoKey;
            addItemPost(postParams)
        }
    });
    return photoKey;
}

$(document).ready(() => {
  username = localStorage.username;
  displayUserItems(username);
  itemModalAnimation();
  addItemFormSubmit();
  $(".name").html(localStorage.getItem('name'));
});
