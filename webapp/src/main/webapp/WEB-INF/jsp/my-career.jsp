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
    <title>Apuntea | <spring:message code="myCareer.title"/></title>
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
    <link rel="stylesheet" href="<c:url value="/css/sections/user/profile-notes.css"/>"/>

    <link rel="stylesheet" href=
            <c:url value="/css/sections/bars.css"/>/>
    <link rel="stylesheet" href=
            <c:url value="/css/sections/user/profile.css"/>/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar user="${user}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="myCareer.title" var="title"/>
    <fragment:bottom-navbar title="${baseUrl}/my-career,${title}"/>
</header>

<main>
    <fragment:sidebar user="${user}" active="my-career"/>

    <section class="container mt-5">
        <!-- ROOT DIRECTORIES -->
        <c:if test="${not empty root_directories}">
            <div class="d-flex flex-column mt-5">

                <ul class="mini-nav">
                    <c:forEach items="${root_directories}" var="subjects" varStatus="i">
                        <spring:message code='ordinal.${subjects.key}' var="ordinal"/>
                        <!--TAB-->
                        <li class="mini-nav-item">
                            <button class="btn mini-nav-button root-dir text-center ${param.tab eq null ? (i.index eq 0 ? 'active' : '') : (i.index eq param.tab ? 'active' : '')}"
                                    data-toggle="tab" role="tab" aria-selected="true">
                                <spring:message code="profileNotes.directories.year" arguments="${ordinal}"/>
                            </button>
                        </li>
                    </c:forEach>
                </ul>

                <div class="tab-content bg-bg">
                    <c:forEach items="${root_directories}" var="subjects" varStatus="i">
                        <div class="tab-pane root-dir-list fade ${param.tab eq null ? (i.index eq 0 ? 'active' : '') : (i.index eq param.tab ? 'active' : '')}" role="tabpanel">
                            <div class="file-list gap-5 justify-content-center align-items-center">
                                <c:forEach items="${subjects.value}" var="rd">
                                    <a class="align-self-start blob-container"
                                       href="<c:url value="./directory/${rd.rootDirectoryId}"/>">

                                        <div class="position-relative">

                                            <div class="heart-container">

                                                <c:set var="addFavorite"
                                                       value="./directory/${rd.rootDirectoryId}/addfavorite"/>
                                                <c:set var="removeFavorite"
                                                       value="./directory/${rd.rootDirectoryId}/removefavorite"/>

                                                <form:form
                                                        action="${rd.rootDirectory.favorite ? removeFavorite : addFavorite}"
                                                        method="post" cssClass="align-content-end">
                                                    <input name="redirectUrl"
                                                           value="/my-career?tab=${i.index}"
                                                           type="hidden"/>
                                                    <button type="submit"
                                                            class="btn nav-icon-button"
                                                            id="<c:out value="${rd.rootDirectoryId}"/>.f1">
                                                        <img src="<c:url value="${ rd.rootDirectory.favorite ? '/svg/filled-heart.svg' : '/svg/heart.svg'}"/>"
                                                             alt="<spring:message code="favorite"/>"
                                                             class="icon-xs fill-text">
                                                    </button>
                                                </form:form>
                                            </div>

                                            <div class="d-flex flex-column gap-2 align-items-center">
                                                <img src="<c:url value="/svg/folder.svg"/>"
                                                     alt="<spring:message code="folder"/>" class="icon-xxl fill-primary">
                                                <!-- max 2 lines-->
                                                <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                    <c:out value="${rd.rootDirectory.name}"/>
                                                </span>
                                            </div>

                                        </div>

                                    </a>

                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
    </section>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/color-picker.js"/>"></script>
<script src="<c:url value="/js/profile-notes.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

</body>

</html>