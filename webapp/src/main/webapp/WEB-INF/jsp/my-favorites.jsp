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
    <title>Apuntea | <spring:message code="myFavorites.title"/></title>
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
    <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>

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
    <spring:message code="profileNotes.directories.favorites" var="title"/>
    <fragment:bottom-navbar title="./my-favorites:${title}"/>
</header>

<main>
    <fragment:sidebar user="${user}" active="my-favorites"/>

    <section class="container mt-5">
        <!-- FAVORITES AND MY NOTES -->
            <div class="d-flex flex-column mt-5">
                <ul class="mini-nav">
                    <!-- FAVORITES -->
                    <li class="mini-nav-item">
                        <button class="btn mini-nav-button root-dir text-center active" data-toggle="tab"
                                role="tab" aria-selected="true">
                            <spring:message code="profileNotes.subjects"/>
                        </button>
                    </li>
                    <li class="mini-nav-item">
                        <button class="btn mini-nav-button root-dir text-center" data-toggle="tab"
                                role="tab" aria-selected="true">
                            <spring:message code="profileNotes.directories"/>
                        </button>
                    </li>
                    <li class="mini-nav-item">
                        <button class="btn mini-nav-button root-dir text-center" data-toggle="tab"
                                role="tab" aria-selected="true">
                            <spring:message code="profileNotes.notes"/>
                        </button>
                    </li>
                </ul>

                <div class="tab-content bg-bg">
                    <!-- FAVORITES LIST -->
                    <div class="tab-pane root-dir-list fade active" role="tabpanel">
                        <c:if test="${empty subjectFavorites}">
                            <div class="d-flex align-middle gap-2 p-3 justify-content-center">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                        </c:if>
                        <c:if test="${not empty subjectFavorites}">
                            <div class="file-list gap-5 justify-content-center align-items-center">
                                <c:forEach items="${subjectFavorites}" var="sub">
                                    <a class="align-self-start" href="<c:url value="./directory/${sub.id}"/>">
                                        <div class="d-flex flex-column gap-2 align-items-center">
                                            <img src="<c:url value="/svg/folder.svg"/>"
                                                 alt="<spring:message code="folder"/>"
                                                 class="icon-xxl fill-4986E7">
                                            <!-- max 2 lines-->
                                            <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                <c:out value="${sub.name}"/>
                                            </span>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                    <div class="tab-pane root-dir-list fade" role="tabpanel">
                        <c:if test="${empty directoryFavorites}">
                            <div class="d-flex align-middle gap-2 justify-content-center">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                        </c:if>
                        <c:if test="${not empty directoryFavorites}">
                            <div class="file-list gap-5 justify-content-center align-items-center">
                                <c:forEach items="${directoryFavorites}" var="dir">
                                    <a class="align-self-start" href="<c:url value="./directory/${dir.id}"/>">
                                        <div class="d-flex flex-column gap-2 align-items-center">
                                            <img src="<c:url value="/svg/folder.svg"/>"
                                                 alt="<spring:message code="folder"/>"
                                                 class="icon-xxl fill-${dir.iconColor}">
                                            <!-- max 2 lines-->
                                            <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                <c:out value="${dir.name}"/>
                                            </span>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                    <div class="tab-pane root-dir-list fade" role="tabpanel">
                        <!-- TODO: CHANGE ACTIVE CLASS WHEN ADD MY NOTES -->
                        <c:if test="${empty noteFavorites}">
                            <div class="d-flex align-middle gap-2 justify-content-center">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                        </c:if>
                        <c:if test="${not empty noteFavorites}">
                            <div class="file-list gap-5 justify-content-center align-items-center">
                                <c:forEach items="${noteFavorites}" var="note">
                                    <a class="align-self-start" href="<c:url value="./notes/${note.id}"/>">
                                        <div class="d-flex flex-column gap-2 align-items-center">
                                            <c:if test="${note.fileType eq 'pdf'}">
                                                <img src="<c:url value="/image/pdf.png"/>" alt="pdf" class="icon-xxl">
                                            </c:if>
                                            <c:if test="${note.fileType eq 'jpeg'}">
                                                <img src="<c:url value="/image/jpeg.png"/>" alt="jpeg" class="icon-xxl">
                                            </c:if>
                                            <c:if test="${note.fileType eq 'jpg'}">
                                                <img src="<c:url value="/image/jpg.png"/>" alt="jpg" class="icon-xxl">
                                            </c:if>
                                            <c:if test="${note.fileType eq 'png'}">
                                                <img src="<c:url value="/image/png.png"/>" alt="png" class="icon-xxl">
                                            </c:if>
                                            <c:if test="${note.fileType eq 'mp3'}">
                                                <img src="<c:url value="/image/mp3.png"/>" alt="mp3" class="icon-xxl">
                                            </c:if>
                                            <c:if test="${note.fileType eq 'mp4'}">
                                                <img src="<c:url value="/image/mp4.png"/>" alt="mp4" class="icon-xxl">
                                            </c:if>
                                            <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                <c:out value="${note.name}"/>
                                            </span>
                                        </div>
                                    </a>
                                </c:forEach>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
    </section>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/color-picker.js"/>"></script>
<script src="<c:url value="/js/profile-notes.js"/>"></script>
</body>

</html>