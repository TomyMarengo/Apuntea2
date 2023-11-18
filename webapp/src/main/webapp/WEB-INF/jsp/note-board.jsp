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
    <c:if test="${empty owner}">
        <title>Apuntea | <spring:message code="myNotes.title"/></title>
    </c:if>
    <c:if test="${not empty owner}">
        <title>Apuntea | <spring:message code="yourProfileNotes.title" arguments="${owner.displayName}"/></title>
    </c:if>

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
    <c:if test="${owner.userId eq user.userId}">
        <spring:message code="myProfileNotes.title" var="title"/>
    </c:if>
    <c:if test="${owner.userId ne user.userId}">
        <spring:message code="yourProfileNotes.title" arguments="${owner.displayName}" var="title"/>
    </c:if>
    <fragment:bottom-navbar title="${baseUrl}/user/${owner.userId}/note-board,${title}" user="${user}"
                            owner="${owner}"/>

</header>

<main>
    <c:if test="${owner eq user}">
        <fragment:sidebar user="${user}" active="note-board"/>
    </c:if>
    <c:if test="${user eq null or owner ne user}">
        <fragment:sidebar user="${user}"/>
    </c:if>

    <section class="container mt-5 d-flex flex-column align-items-center">
        <!-- USER PROFILE -->
        <c:if test="${owner ne user}">
            <c:url var="userProfilePicture" value="${baseUrl}/profile/${owner.userId}/picture"/>
            <div class="profile-card">
                <img src="${userProfilePicture}" alt="Profile Picture" class="rounded-circle" width="50px"
                     height="50px">
                <div class="d-flex flex-column">
                    <h4 class="mb-0">${owner.displayName}</h4>
                    <span>3⭐</span> <!-- TODO: Add user score -->
                </div>
                <c:if test="${user ne null}">
                    <c:set var="followUrl" value="${baseUrl}/user/${owner.userId}/follow"/>
                    <c:set var="unfollowUrl" value="${baseUrl}/user/${owner.userId}/unfollow"/>

                    <form:form action="${isFollowing ? unfollowUrl : followUrl}" method="post">
                        <button type="submit"
                                class="follow-button ${isFollowing ? 'following' : 'not-following'}"
                                data-following-text="<spring:message code='following'/>"
                                data-follow-text="<spring:message code='follow'/>"
                                data-unfollow-text="<spring:message code='unfollow'/>">
                        </button>
                    </form:form>

                </c:if>
            </div>
        </c:if>

        <c:if test="${not empty latestNotes}">

            <div class="d-flex flex-column mt-5 w-100">
                <ul class="mini-nav">
                    <li class="mini-nav-item">
                        <button class="btn mini-nav-button root-dir-no-js text-center active">
                            <spring:message code="latestNotes"/>
                        </button>
                    </li>
                </ul>

                <div class="tab-content bg-bg">
                    <div class="tab-pane active">
                        <div class="file-list gap-5 justify-content-center align-items-center">
                            <c:forEach items="${latestNotes}" var="note">
                                <a class="align-self-start w-100" href="<c:url value="${baseUrl}/notes/${note.id}"/>">
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
                                        <span class="fw-bold flex-wrap justify-content-center user-followed-name">
                                                <c:out value="${note.name}"/>
                                            </span>
                                    </div>
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- ROOT DIRECTORIES -->
        <c:if test="${not empty root_directories}">
            <div class="d-flex flex-column mt-5 w-100">
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
                        <div class="tab-pane root-dir-list fade ${param.tab eq null ? (i.index eq 0 ? 'active' : '') : (i.index eq param.tab ? 'active' : '')}"
                             role="tabpanel">
                            <div class="file-list gap-5 justify-content-center align-items-center">
                                <c:forEach items="${subjects.value}" var="rd">
                                    <a class="align-self-start blob-container"
                                       href="<c:url value="${baseUrl}/directory/${rd.rootDirectoryId}?userId=${ not empty owner ? owner.userId : user.userId}"/>">
                                        <div class="position-relative">
                                            <div class="note-count-container">
                                                <span><strong>${rd.rootDirectory.qtyFiles}</strong></span>
                                            </div>
                                            <div class="d-flex flex-column gap-2 align-items-center">
                                                <img src="<c:url value="/svg/folder.svg"/>"
                                                     alt="<spring:message code="folder"/>"
                                                     class="icon-xxl fill-primary">
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

        <c:if test="${empty root_directories}">
            <div class="d-flex flex-column mt-5">
                <ul class="mini-nav">
                    <li class="mini-nav-item">
                        <button class="btn mini-nav-button root-dir text-center active">
                            <spring:message code="profileNotes.notes"/>
                        </button>
                    </li>
                </ul>
                <div class="tab-content bg-bg">
                    <div class="tab-pane root-dir-list fade active"
                         role="tabpanel">
                        <c:set var="myCareerUrl" value="${baseUrl}/my-career"/>
                        <div class="d-flex flex-column gap-2 p-3 align-items-center">
                            <div class="d-flex">
                                <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                                <h3><spring:message code="directories.noContent"/></h3>
                            </div>
                            <c:if test="${empty owner}">
                                <div class="d-flex">
                                    <fmt:message key="profileNotes.notes.noContent.description">
                                        <fmt:param value="${myCareerUrl}"/>
                                    </fmt:message>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

        </c:if>
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