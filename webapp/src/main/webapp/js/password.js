function password_show_hide(num = '') {
    var x = document.getElementById(`password${num}`);
    var show_eye = document.getElementById(`show_eye${num}`);
    var hide_eye = document.getElementById(`hide_eye${num}`);
    hide_eye.classList.remove("d-none");
    if (x.type === "password") {
        x.type = "text";
        show_eye.style.display = "none";
        hide_eye.style.display = "block";
    } else {
        x.type = "password";
        show_eye.style.display = "block";
        hide_eye.style.display = "none";
    }
}