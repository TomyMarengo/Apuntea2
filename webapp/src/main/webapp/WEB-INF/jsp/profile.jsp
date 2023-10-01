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
    <title>Apuntea | <spring:message code="profile.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/color-picker.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href=
            <c:url value="/css/sections/navbar.css"/>/>
    <link rel="stylesheet" href=
            <c:url value="/css/sections/user/profile.css"/>/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar loggedin="${user != null}"/>

<!-- BOTTOM-NAVBAR -->
<spring:message code="profile.title" var="title"/>
<fragment:bottom-navbar title="./profile:${title}"/>


<!-- USER INFO & BUTTONS "VER" -->
<main class="container-fluid px-5 mt-5">
    <div class="row justify-content-around">
        <!-- User info column -->
        <div class="col-12 col-lg-4 col-xl-3 ">
            <div class="card user-card box">
                <div class="card-body">
                    <!-- Profile picture (visible on small screens) -->
                    <div class="profile-picture-small">
                        <div class="rounded-circle overflow-hidden d-flex justify-content-center align-items-center">
                            <img src="<c:url value="/image/profile-picture.jpeg"/>" alt="Profile picture" class="profile-picture border border-2 border-dark-primary">
                        </div>
                   </div>

                    <!-- User info -->
                    <div class="d-flex flex-column gap-2">
                        <h4 class="card-title fw-bold">${user.email}</h4>
                        <c:forEach items="${user.roles}" var="r">
                            <span class="card-text">
                            <c:choose>
                                <c:when test = "${r eq 'ROLE_STUDENT'}">
                                        <spring:message code="profile.role.student"/>
                                </c:when>
                                <c:when test = "${r eq 'ROLE_MODERATOR'}">
                                    <spring:message code="profile.role.moderator"/>
                                </c:when>
                                <c:when test = "${r eq 'ROLE_ADMIN'}">
                                    <spring:message code="profile.role.admin"/>
                                </c:when>
                            </c:choose>
                            </span>
                        </c:forEach>
                        <span class="card-text">${user.institution.name}</span>
                        <span class="card-text">${user.career.name}</span>
                    </div>
                    <!-- Social media links, etc. -->
                </div>
            </div>
        </div>


        <div class="col-12 col-lg-7 col-xl-8">
            <h2 class="text-center mb-3"><spring:message code="profile.directories.title"/></h2>
            <div class="flex-row flex-wrap card rounded-box gap-2 p-4 justify-content-around">
                <c:forEach items="${root_directories}" var="rd">
                        <a href="<c:url value="./directory/${rd.id}"/>">
                            <div class="d-flex flex-column gap-2 align-items-center px-4 py-3">
                                <!-- TODO: REVISAR COLOR RD -->
                                <img src="<c:url value="/svg/folder.svg"/>" alt="<spring:message code="folder"/>" class="icon-xxl fill-${rd.iconColor}">
                                <span class="fw-bold">
                                    <c:out value="${rd.name}"/>
                                </span>
                            </div>
                        </a>
                </c:forEach>
            </div>
        </div>
    </div>
</main>


<%--<div class="col-lg-auto d-flex justify-content-center gap-5 mt-4 mt-lg-0 ">
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
        </div>--%>
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
<script src="<c:url value="/js/color-picker.js"/>"></script>
<script src="<c:url value="/js/global-search.js"/>"></script>

</body>

</html>