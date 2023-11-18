<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <fragment:bottom-navbar title="${baseUrl}/my-favorites,${title}"/>
</header>

<main>
    <fragment:sidebar user="${user}" active="my-favorites"/>

    <section class="container mt-5">
        <!-- FAVORITES AND MY NOTES -->
        <div class="d-flex flex-column mt-5">
            <ul class="mini-nav">
                <!-- FAVORITES -->
                <li class="mini-nav-item">
                    <button class="btn mini-nav-button root-dir text-center ${param.tab eq null ? 'active' : (param.tab eq 0 ? 'active' : '')}"
                            data-toggle="tab"
                            role="tab" aria-selected="true">
                        <spring:message code="profileNotes.subjects"/>
                    </button>
                </li>
                <li class="mini-nav-item">
                    <button class="btn mini-nav-button root-dir text-center ${param.tab eq null ? '' : (param.tab eq 1 ? 'active' : '')}"
                            data-toggle="tab"
                            role="tab" aria-selected="true">
                        <spring:message code="profileNotes.directories"/>
                    </button>
                </li>
                <li class="mini-nav-item">
                    <button class="btn mini-nav-button root-dir text-center ${param.tab eq null ? '' : (param.tab eq 2 ? 'active' : '')}"
                            data-toggle="tab"
                            role="tab" aria-selected="true">
                        <spring:message code="profileNotes.notes"/>
                    </button>
                </li>
                <li class="mini-nav-item">
                    <button class="btn mini-nav-button root-dir text-center ${param.tab eq null ? '' : (param.tab eq 3 ? 'active' : '')}"
                            data-toggle="tab"
                            role="tab" aria-selected="true">
                        <spring:message code="users"/>
                    </button>
                </li>
            </ul>

            <!-- FAVORITES LIST -->
            <div class="tab-content bg-bg">
                <!-- SUBJECTS -->
                <div class="tab-pane root-dir-list fade ${param.tab eq null ? 'active' : (param.tab eq 0 ? 'active' : '')}"
                     role="tabpanel">
                    <c:if test="${empty subjectFavorites}">
                        <c:set var="myCareerUrl" value="./my-career"/>
                        <div class="d-flex flex-column gap-2 p-3 align-items-center">
                            <div class="d-flex">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                            <div class="d-flex">
                                <fmt:message key="myFavorites.subjects.noContent.description">
                                    <fmt:param value="${myCareerUrl}"/>
                                </fmt:message>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty subjectFavorites}">
                        <div class="file-list gap-5 justify-content-center align-items-center">
                            <c:forEach items="${subjectFavorites}" var="sub">
                                <a class="align-self-start" href="<c:url value="./directory/${sub.id}"/>">
                                    <div class="position-relative">
                                        <div class="heart-container">
                                            <c:set var="removeFavorite"
                                                   value="./directory/${sub.id}/removefavorite"/>
                                            <form:form
                                                    action="${removeFavorite}"
                                                    method="post" cssClass="align-content-end">
                                                <input name="redirectUrl"
                                                       value="/my-favorites?tab=0"
                                                       type="hidden"/>
                                                <button type="submit"
                                                        class="btn nav-icon-button"
                                                        id="<c:out value="${sub.id}"/>.f1">
                                                    <img src="<c:url value="/svg/filled-heart.svg"/>"
                                                         alt="<spring:message code="favorite"/>"
                                                         class="icon-xs fill-text">
                                                </button>
                                            </form:form>
                                        </div>

                                        <div class="d-flex flex-column gap-2 align-items-center blob-container">
                                            <img src="<c:url value="/svg/folder.svg"/>"
                                                 alt="<spring:message code="folder"/>" class="icon-xxl fill-primary">
                                            <!-- max 2 lines-->
                                            <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                    <c:out value="${sub.name}"/>
                                                </span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>

                        </div>
                    </c:if>
                </div>
                <c:url value="./search" var="searchUrl"/>

                <!-- FOLDERS -->
                <div class="tab-pane root-dir-list fade ${param.tab eq null ? '' : (param.tab eq 1 ? 'active' : '')}"
                     role="tabpanel">
                    <c:if test="${empty directoryFavorites}">
                        <div class="d-flex flex-column gap-2 p-3 align-items-center">
                            <div class="d-flex">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                            <div class="d-flex">
                                <fmt:message key="myFavorites.directories.noContent.description">
                                    <fmt:param value="${searchUrl}"/>
                                </fmt:message>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty directoryFavorites}">
                        <div class="file-list gap-5 justify-content-center align-items-center">

                            <c:forEach items="${directoryFavorites}" var="dir">
                                <a class="align-self-start" href="<c:url value="./directory/${dir.id}"/>">

                                    <div class="position-relative">

                                        <div class="heart-container">
                                            <c:set var="removeFavorite"
                                                   value="./directory/${dir.id}/removefavorite"/>
                                            <form:form
                                                    action="${removeFavorite}"
                                                    method="post" cssClass="align-content-end">
                                                <input name="redirectUrl"
                                                       value="/my-favorites?tab=1"
                                                       type="hidden"/>
                                                <button type="submit"
                                                        class="btn nav-icon-button"
                                                        id="<c:out value="${dir.id}"/>.f1">
                                                    <img src="<c:url value="/svg/filled-heart.svg"/>"
                                                         alt="<spring:message code="favorite"/>"
                                                         class="icon-xs fill-text">
                                                </button>
                                            </form:form>
                                        </div>

                                        <div class="d-flex flex-column gap-2 align-items-center blob-container">
                                            <img src="<c:url value="/svg/folder.svg"/>"
                                                 alt="<spring:message code="folder"/>"
                                                 class="icon-xxl fill-${dir.iconColor}">
                                            <!-- max 2 lines-->
                                            <span class="fw-bold flex-wrap justify-content-center folder-name">
                                                    <c:out value="${dir.name}"/>
                                                </span>
                                        </div>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <!-- NOTES -->
                <div class="tab-pane root-dir-list fade ${param.tab eq null ? '' : (param.tab eq 2 ? 'active' : '')}"
                     role="tabpanel">
                    <!-- TODO: CHANGE ACTIVE CLASS WHEN ADD MY NOTES -->
                    <c:if test="${empty noteFavorites}">
                        <div class="d-flex flex-column gap-2 p-3 align-items-center">
                            <div class="d-flex">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                            <div class="d-flex">
                                <fmt:message key="myFavorites.notes.noContent.description">
                                    <fmt:param value="${searchUrl}"/>
                                </fmt:message>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty noteFavorites}">
                        <div class="file-list gap-5 justify-content-center align-items-center">

                            <c:forEach items="${noteFavorites}" var="note">
                                <a class="align-self-start" href="<c:url value="./notes/${note.id}"/>">
                                    <div class="position-relative">
                                        <div class="heart-container">
                                            <c:set var="removeFavorite" value="./notes/${note.id}/removefavorite"/>
                                            <form:form
                                                    action="${removeFavorite}"
                                                    method="post" cssClass="align-content-end">
                                                <input name="redirectUrl"
                                                       value="/my-favorites?tab=2"
                                                       type="hidden"/>
                                                <button type="submit"
                                                        class="btn nav-icon-button"
                                                        id="<c:out value="${note.id}"/>.f1">
                                                    <img src="<c:url value="/svg/filled-heart.svg"/>"
                                                         alt="<spring:message code="favorite"/>"
                                                         class="icon-xs fill-text">
                                                </button>
                                            </form:form>
                                        </div>
                                        <div class="d-flex flex-column gap-2 align-items-center blob-container">
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
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>

                <!-- USERS -->
                <div class="tab-pane root-dir-list fade ${param.tab eq null ? '' : (param.tab eq 3 ? 'active' : '')}"
                     role="tabpanel">
                    <!-- TODO: CHANGE ACTIVE CLASS WHEN ADD MY NOTES -->
                    <c:if test="${empty userFavorites}">
                        <div class="d-flex flex-column gap-2 p-3 align-items-center">
                            <div class="d-flex">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="follows.noContent"/></h3>
                            </div>
                            <div class="d-flex">
                                <spring:message code="follows.noContent.description"/>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${not empty userFavorites}">
                        <div class="file-list gap-5 justify-content-center align-items-center">

                            <c:forEach items="${userFavorites}" var="user">
                                <c:url var="userProfilePicture" value="${baseUrl}/profile/${user.userId}/picture"/>

                                <a class="align-self-start w-100"
                                   href="<c:url value="${baseUrl}/user/${user.userId}/note-board"/>">
                                    <div class="d-flex flex-column gap-2 align-items-center blob-container">
                                        <img src="${userProfilePicture}"
                                             alt="<spring:message code="profile.picture"/>" class="icon-xxl">
                                        <span class="fw-bold flex-wrap justify-content-center user-followed-name">
                                                <c:out value="${user.displayName}"/>
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