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
  itemContent += '<h1>' +  item[1]  + '</h1><hr>';
  itemContent += '<div class="item-content-1">';
  itemContent += '<div class="content-elem">';
  itemContent += '<img src="' + item[8] + '"></div>';
  itemContent += '<div class="content-elem">';
  itemContent += '<div class="color-type-div">';
  itemContent += '<h3>Color</h3><hr>';
  itemContent += '<div class="rect-color" style="background-color:#' + parseHex(item[2]) + ';"></div>'
  itemContent += '<span>#' + parseHex(item[2]) + '</span>';
  itemContent += '<h3>Type</h3><hr>';
  itemContent += '<p>' + parseItemText(item[3]) + ' - ' + parseItemText(item[4]) + '</p>';
  itemContent += '</div></div></div>';
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
        + id + '"></button><div style="text-align: center">' + item[1] + '</div></div>';
    let modalHTML = '<div class="modal" id="modal-' + id + '">';
    modalHTML += '<div class="modal-content">';
    modalHTML += '<span class="close" id="close-' + id + '">&times;</span>';
    modalHTML += generateCardContent(item);
    modalHTML += '<button id="edit-item-' + id + '">Edit Item</button>';
    modalHTML += '<button id="delete-item-' + id + '">Delete Item</button>';
    modalHTML += '</div></div>';

    // add modal to div 'items'
    $('#items').append(buttonHTML);
    $('#items').append(modalHTML);

    // Set image
    let imageSource = item[8];
    $('#item-' + id).css("background", "url(" + imageSource + ") no-repeat");
    $('#item-' + id).css("background-size", "contain");

    // add popup functionality to given modal
    animateItemModal(id);

    // add delete button functionality
    deleteUserItem(id);
  }
}

// add button functionality to remove an item
function deleteUserItem(itemId) {
  // event handler for removing item
  $('#delete-item-' + itemId).on('click', function(e) {

      // let postParams = {}
      let postParams = {
          id: itemId
      };

      $.post("/getOutfitWithItem", postParams, responseJSON => {

          let outfitIds = JSON.parse(responseJSON).outfitIds;
          console.log(outfitIds);

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
function dynamicTypeDropdown() {
  $('#type-1').on('change', function(){
    $('#type-2').html('');
    if ($('#type-1').val() == "OUTER") {
        $('#type-2').append('<option value="OUTER_COAT">Outer Coat</option>');
        $('#type-2').append('<option value="SUIT">Suit</option>');
    } else if ($('#type-1').val() == "TOP") {
      $('#type-2').append('<option value="SHIRT_BLOUSE">Shirt/Blouse</option>');
      $('#type-2').append('<option value="T_SHIRT">T-Shirt</option>');
      $('#type-2').append('<option value="SWEATER">Sweater</option>');
      $('#type-2').append('<option value="JACKET">Jacket</option>');
    } else if ($('#type-1').val() == "BOTTOM") {
      $('#type-2').append('<option value="PANTS">Pants</option>');
      $('#type-2').append('<option value="SKIRT">Skirt</option>');
      $('#type-2').append('<option value="DRESS">Dress</option>');
      $('#type-2').append('<option value="SHORTS">Shorts</option>');
    } else {
      $('#type-2').append('<option value="SNEAKERS">Sneakers</option>');
      $('#type-2').append('<option value="BOOTS">Boots</option>');
      $('#type-2').append('<option value="SANDALS">Sandals</option>');
      $('#type-2').append('<option value="DRESS_SHOES">Dress Shoes</option>');
    }
  });
}

// function that allows for users to add more colors to outfit
function additionalColorForm() {
  $('#addColor').on('click', function(e) {
    $('#item-colors').append('<input type="color" name="item-color" value="#ff0000"><br>');
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
