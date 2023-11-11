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
            <span id="dot1" class="dot-content navigator-wrapper">
                <span class="navigator dot selected" data-key="1"></span>
            </span>
            <span id="dot2" class="dot-content navigator-wrapper">
                <span class="navigator dot" data-key="2"></span>
            </span>
            <span id="dot3" class="dot-content navigator-wrapper">
                <span class="navigator dot" data-key="3"></span>
            </span>
            <span id="dot4" class="dot-content navigator-wrapper">
                <span class="navigator dot" data-key="4"></span>
            </span>
        </div>
    </div>

    <div class="slider-wrapper">
        <div id="slide1" class="slider-content current" data-key="1">
            <section class="content-wrapper">

                <div class="mb-lg-5">
                    <h1><spring:message code="apuntea"/></h1>
                    <h2><spring:message code="index.subtitle"/></h2>
                </div>

                <div class="welcome-container">
                    <img id="index-welcome-img" src="image/index-welcome.png"
                         alt="<spring:message code="index.welcome.alt"/>">
                    <div class="welcome-side">

                        <div class="welcome-title">
                            <h3><spring:message code="index.welcome.title"/></h3>
                            <p><spring:message code="index.welcome.subtitle"/></p>
                        </div>

                        <div class="welcome-search">
                            <div class="search-pill">
                                <button type="button" id="selectOnlyFoldersButton">
                                    <img src="<c:url value="/svg/folder.svg"/>" alt="<spring:message code="folder"/>"
                                         class="icon-s fill-bg"/>
                                    <spring:message code="folders"/>
                                </button>

                                <button class="active" type="button" id="selectAllCategoriesButton">
                                    <spring:message code="category.all"/>
                                </button>

                                <button type="button" id="selectOnlyFilesButton">
                                    <spring:message code="files"/>
                                    <img src="<c:url value="/svg/file.svg"/>" alt="<spring:message code="folder"/>"
                                         class="icon-s fill-bg"/>
                                </button>
                            </div>
                            <div class="welcome-search-input-group">
                                <spring:message var="placeholderWord" code="search.word.placeholder"/>
                                <input type="text" name="word" id="wordInput" class="welcome-search-input"
                                       placeholder="${placeholderWord}" required/>
                                <button class="welcome-search-button" onclick="goToSearch()">
                                    <img src="<c:url value="/svg/search.svg"/>"
                                         alt="<spring:message code="search.title"/>"
                                         class="icon-s fill-dark-text"/>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>

        <c:if test="${user ne null}">
            <c:url var="urlParams"
                   value="${baseUrl}/search?institutionId=${user.institution.institutionId}&careerId=${user.career.careerId}"/>
        </c:if>
        <c:if test="${user eq null}">
            <c:url var="urlParams" value="${baseUrl}/search"/>
        </c:if>

        <div id="slide2" class="slider-content next" data-key="2">
            <section class="content-wrapper">
                <h2>Con Apuntea puedes hacer todo esto y más</h2>
                <div class="index-cards">

                    <div class="d-flex gap-5">


                        <a href="${baseUrl}/user/note-board" class="call-to-action-card">
                            <img src="<c:url value="/svg/add-document.svg"/>"
                                 alt="<spring:message code="search.title"/>" class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card2.title"/></h3>
                                </div>
                                <p><spring:message code="index.card2.body"/></p>
                            </div>
                        </a>

                        <a href="${urlParams}" class="call-to-action-card">
                            <img src="<c:url value="/svg/search-alt.svg"/>" alt="<spring:message code="search.title"/>"
                                 class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card3.title"/></h3>
                                </div>
                                <p><spring:message code="index.card3.body"/></p>
                            </div>
                        </a>
                    </div>
                    <c:if test="${user != null}">
                        <a href="${baseUrl}/profile" class="call-to-action-card">
                            <img src="<c:url value="/svg/user.svg"/>" alt="<spring:message code="search.title"/>"
                                 class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card1.title.editProfile"/></h3>
                                </div>
                                <p><spring:message code="index.card1.body.editProfile"/></p>
                            </div>
                        </a>
                    </c:if>
                    <c:if test="${user == null}">
                        <a href="${baseUrl}/register" class="call-to-action-card">
                            <img src="<c:url value="/svg/users.svg"/>" alt="<spring:message code="search.title"/>"
                                 class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card1.title.register"/></h3>
                                </div>
                                <p><spring:message code="index.card1.body.register"/></p>
                            </div>
                        </a>
                    </c:if>
                </div>
            </section>
        </div>

        <div id="slide2-sm" class="slider-content next" data-key="2">
            <section class="content-wrapper">
                <h2>Con Apuntea puedes hacer todo esto y más</h2>
                <div class="index-cards">
                    <a href="${baseUrl}/user/note-board" class="call-to-action-card">
                        <img src="<c:url value="/svg/add-document.svg"/>"
                             alt="<spring:message code="search.title"/>" class="icon-m fill-dark-text"/>
                        <div class="call-to-action-card-body">
                            <div class="call-to-action-card-title">
                                <h3><spring:message code="index.card2.title"/></h3>
                            </div>
                            <p><spring:message code="index.card2.body"/></p>
                        </div>
                    </a>
                </div>
            </section>
        </div>

        <div id="slide3" class="slider-content next" data-key="3">
            <section class="content-wrapper">
                <h2>Con Apuntea puedes hacer todo esto y más</h2>
                <div class="index-cards">
                    <a href="${urlParams}" class="call-to-action-card">
                        <img src="<c:url value="/svg/search-alt.svg"/>" alt="<spring:message code="search.title"/>"
                             class="icon-m fill-dark-text"/>
                        <div class="call-to-action-card-body">
                            <div class="call-to-action-card-title">
                                <h3><spring:message code="index.card3.title"/></h3>
                            </div>
                            <p><spring:message code="index.card3.body"/></p>
                        </div>
                    </a>
                </div>
            </section>
        </div>

        <div id="slide4" class="slider-content next" data-key="4">
            <section class="content-wrapper">
                <h2>Con Apuntea puedes hacer todo esto y más</h2>
                <div class="index-cards">

                    <c:if test="${user != null}">
                        <a href="${baseUrl}/profile" class="call-to-action-card">
                            <img src="<c:url value="/svg/user.svg"/>" alt="<spring:message code="search.title"/>"
                                 class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card1.title.editProfile"/></h3>
                                </div>
                                <p><spring:message code="index.card1.body.editProfile"/></p>
                            </div>
                        </a>
                    </c:if>
                    <c:if test="${user == null}">
                        <a href="${baseUrl}/register" class="call-to-action-card">
                            <img src="<c:url value="/svg/users.svg"/>" alt="<spring:message code="search.title"/>"
                                 class="icon-m fill-dark-text"/>
                            <div class="call-to-action-card-body">
                                <div class="call-to-action-card-title">
                                    <h3><spring:message code="index.card1.title.register"/></h3>
                                </div>
                                <p><spring:message code="index.card1.body.register"/></p>
                            </div>
                        </a>
                    </c:if>

                </div>
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