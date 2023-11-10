<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea</title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/landing.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/scrolling.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <fragment:navbar user="${user}"/>
</header>


<main id='main-container'>

    <fragment:sidebar user="${user}"/>

    <div class="dot-nav-panel">
        <div class="navigation-container">
                <span class="navigator-wrapper">
                    <span class="navigator dot selected" data-key="1"></span>
                </span>
            <span class="navigator-wrapper">
                    <span class="navigator dot" data-key="2"></span>
                </span>
            <span class="navigator-wrapper">
                    <span class="navigator dot" data-key="3"></span>
                </span>
            <span class="navigator-wrapper">
                    <span class="navigator dot" data-key="4"></span>
                </span>
        </div>
    </div>

    <div class="slider-wrapper">
        <div id="slide1" class="slider-content current" data-key="1">
            <section class="content-wrapper">

                <div class="mb-5">
                    <h1><spring:message code="apuntea"/></h1>
                    <h2><spring:message code="index.subtitle"/></h2>
                </div>

                <div class="welcome-container">
                    <img src="image/index-welcome.png">
                    <div class="welcome-text">
                        <h3>¡Bienvenido!</h3>
                        <p>Apuntea es una plataforma de apuntes colaborativa, donde podrás compartir tus apuntes y
                            conocimientos con otros usuarios, y a su vez, aprender de ellos.</p>
                    </div>
                </div>
            </section>
        </div>
        <div id="slide2" class="slider-content next" data-key="2">
            <section class="content-wrapper">
                <h1 class="heading">Second slide</h1>
            </section>
        </div>
        <div id="slide3" class="slider-content next" data-key="3">
            <section class="content-wrapper">
                <h1 class="heading">Third slide</h1>
            </section>
        </div>
        <div id="slide4" class="slider-content next" data-key="4">
            <section class="content-wrapper">
                <h1 class="heading">Fourth slide</h1>
            </section>
        </div>
    </div>

</main>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/index-scrolling.js"/>"></script>
</body>

</html>