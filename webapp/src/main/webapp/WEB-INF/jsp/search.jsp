<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Apuntea</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="./css/main.css" />
    <link rel="stylesheet" href="./css/general/elements.css" />
    <link rel="stylesheet" href="./css/general/sizes.css" />
    <link rel="stylesheet" href="./css/general/backgrounds.css" />
    <link rel="stylesheet" href="./css/general/texts.css" />
    <link rel="stylesheet" href="./css/general/buttons.css" />
    <link rel="stylesheet" href="./css/general/icons.css" />
    <link rel="stylesheet" href="./css/general/boxes.css" />
    <link rel="stylesheet" href="./css/sections/navbar.css" />

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid d-flex align-items-center">
        <a class="navbar-brand text-dark-primary" href="index.html">Apuntea</a>
        <div class="d-flex justify-content-center align-items-center" style="height: 58px">
            <button class="btn nav-icon-button" type="button">
                <img src="./svg/bell.svg" alt="Notifications" class="icon-s fill-dark-primary" />
            </button>
            <button id="darkModeToggle" class="btn nav-icon-button" type="button">
                <img id="darkModeIcon" src="./svg/sun.svg" alt="Dark Mode" class="icon-s fill-dark-primary" />
            </button>
            <a href="profile.html">
                <button class="button-primary nav-icon-button nav-user-circle d-flex align-items-center justify-content-center" type="button">
                    <img src="./svg/user.svg" alt="User" class="icon-s fill-bg" />
                </button>
            </a>
        </div>
    </div>
</nav>

<div class="bottom-navbar">
    <a href="profile.html">
        <div class="bottom-navbar-item active">Búsqueda</div>
    </a>
</div>

<!-- SEARCH -->
<div class="container d-flex flex-column w-100">
    <form class="">
        <div class="row row-cols-1 row-cols-md-3 row-cols-lg-6 ">
            <div class="col">
                <div class="input-group mb-3">
                    <span class="input-group-text"><img src="./svg/school.svg" alt="University" class="icon-s fill-text" /></span>
                    <input type="text" class="form-control bg-bg" placeholder="Universidad">
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <span class="input-group-text"><img src="./svg/books.svg" alt="Subject" class="icon-s fill-text" /></span>
                    <input type="text" class="form-control bg-bg" placeholder="Carrera">
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <span class="input-group-text"><img src="./svg/book-alt.svg" alt="Subject" class="icon-s fill-text" /></span>
                    <input type="text" class="form-control bg-bg" placeholder="Materia">
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <select class="form-select bg-bg" id="inputGroupSelectType">
                        <option selected>Tipo...</option>
                        <option value="1">Examen</option>
                        <option value="2">Guía Práctica</option>
                        <option value="3">Teórica</option>
                    </select>
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <select class="form-select bg-bg" id="inputGroupSelectRating">
                        <option selected>Calificación</option>
                        <option value="5">> ⭐⭐⭐⭐⭐</option>
                        <option value="4">> ⭐⭐⭐⭐</option>
                        <option value="3">> ⭐⭐⭐</option>
                        <option value="2">> ⭐⭐</option>
                        <option value="1">> ⭐</option>
                    </select>
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <select class="form-select bg-bg" id="inputGroupSelectOrder">
                        <option selected>Ordenar por...</option>
                        <option value="1">Nombre (ascendente)</option>
                        <option value="2">Nombre (descendente)</option>
                        <option value="3">Calificación (ascendente)</option>
                        <option value="4">Calificación (descendente)</option>
                    </select>
                </div>
            </div>

        </div>

        <button type="submit" class="btn button-primary w-100 ">Buscar</button>
    </form>
</div>

<!-- NOTES FOUND -->


<script src="./js/scripts.js"></script>
</body>

</html>