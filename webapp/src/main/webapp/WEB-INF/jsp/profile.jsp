<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="profile"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>" />
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href=<c:url value="/css/sections/navbar.css"/> />
    <link rel="stylesheet" href=<c:url value="/css/sections/user/profile.css"/> />

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar/>

<!-- BOTTOM-NAVBAR -->
<fragment:bottom-navbar title="profile:Perfil" extraLinks="settings:Ajustes">
</fragment:bottom-navbar>

<!-- USER INFO & BUTTONS "VER" -->
<div class="container">
    <div class="row justify-content-around">
        <!-- Profile picture column (hidden on small screens) -->
        <div class="col-lg-2 d-none d-lg-block">
            <div class="profile-picture">
                <div class="outer-circle">
                    <div class="inner-circle">
                        <!-- Profile picture content -->
                    </div>
                </div>
            </div>
        </div>

        <!-- User info column -->
        <div class="col-lg-4 col-12">
            <div class="card user-card box">
                <div class="card-body">
                    <!-- Profile picture (visible on small screens) -->
                    <div class="d-lg-none profile-picture-small">
                        <div class="outer-circle">
                            <div class="inner-circle">
                                <!-- Profile picture content -->
                            </div>
                        </div>
                    </div>

                    <!-- User info -->
                    <h4 class="card-title fw-bold">Tomas Marengo</h4>
                    <p class="card-text">Descripción</p>
                    <!-- Social media links, etc. -->
                </div>
            </div>
        </div>

        <div class="col-lg-auto d-flex justify-content-center gap-5 mt-4 mt-lg-0 ">
            <div class="d-flex flex-column text-center align-items-center">
                <span class="fw-bold">28</span>
                <span>Apuntes</span>
                <button class="btn button-primary rounded-box profile-button">Ver</button>
            </div>
            <div class="d-flex flex-column text-center align-items-center">
                <span class="fw-bold">28</span>
                <span>Comentarios</span>
                <button class="btn button-primary rounded-box profile-button">Ver</button>
            </div>
            <div class="d-flex flex-column text-center align-items-center">
                <span class="fw-bold">28</span>
                <span>Favoritos</span>
                <button class="btn button-primary rounded-box profile-button">Ver</button>
            </div>
        </div>
    </div>
</div>

<!--
<div class="d-flex">
    <img src="svg/bell.svg" alt="Level" class="icon-l fill-dark-primary" />
    <div class="d-flex flex-column">
        <span>Nivel 8</span>
        <span>Maestro de Sabiduría</span>
        <span>100/300 XP</span>
    </div>
</div> -->

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>

</body>

</html>