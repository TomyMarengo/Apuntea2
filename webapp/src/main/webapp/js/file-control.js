const maxFileMb = 524288000 / 1024 ** 2;

const fileInput = document.getElementById("file")
if (fileInput) {
    fileInput.addEventListener("change", function () {
        if (fileInput.files.length > 0) {
            const fileSize = fileInput.files.item(0).size;
            const fileMb = fileSize / 1024 ** 2;
            if (fileMb >= maxFileMb) {
                document.getElementById("file-size-error").classList.remove("d-none");
                fileInput.value = "";
            } else {
                document.getElementById("file-size-error").classList.add("d-none");
            }
        }
    });
}