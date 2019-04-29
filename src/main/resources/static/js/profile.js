document.getElementById("form-wrapper").style.display = "none";
$("#name").html(localStorage.getItem('name'));
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