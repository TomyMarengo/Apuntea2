<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

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
    <link rel="stylesheet" href="<c:url value="/css/sections/navbar.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/landing.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <fragment:navbar loggedIn="${user != null}" isAdmin="${user.roles[1] ne null}"/>
</header>

<main class="h-100-nav container-fluid mt-5">
    <div class="h-100 d-flex flex-column align-items-center gap-3 text-center">
        <h2><spring:message code="index.title"/></h2>
        <h4><spring:message code="index.subtitle"/></h4>

        <div class="index-cards">
            <a href="${baseUrl}/settings" class="call-to-action-card section1">

                <img src="<c:url value="/svg/gears.svg"/>" alt="<spring:message code="search.title"/>"
                     class="fill-dark-text"/>

                <div class="call-to-action-card-body">

                    <div class="call-to-action-card-title">
                        <h3><spring:message code="index.card1.title"/></h3>
                    </div>
                    <div class="call-to-action-card-text">
                        <p><spring:message code="index.card1.body"/></p>
                    </div>
                </div>

            </a>

            <a href="${baseUrl}/profile" class="call-to-action-card section2">

                <img src="<c:url value="/svg/add-document.svg"/>" alt="<spring:message code="search.title"/>"
                     class="fill-dark-text"/>

                <div class="call-to-action-card-body">
                    <div class="call-to-action-card-title">
                        <h3><spring:message code="index.card2.title"/></h3>
                    </div>
                    <div class="call-to-action-card-text">
                        <p><spring:message code="index.card2.body"/></p>
                    </div>
                </div>

            </a>

            <a href="${baseUrl}/search" class="call-to-action-card section3">

                <img src="<c:url value="/svg/search-alt.svg"/>" alt="<spring:message code="search.title"/>"
                     class="fill-dark-text"/>

                <div class="call-to-action-card-body">
                    <div class="call-to-action-card-title">
                        <h3><spring:message code="index.card3.title"/></h3>
                    </div>
                    <div class="call-to-action-card-text">
                        <p><spring:message code="index.card3.body"/></p>
                    </div>
                </div>

            </a>

        </div>
    </div>
</main>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

</body>

</html>