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
    <title>Apuntea | <spring:message code="settings.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href=<c:url value="/css/sections/navbar.css"/>/>
    <link rel="stylesheet" href=<c:url value="/css/general/settings.css"/>/>

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet"/>

</head>

<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar loggedIn="${user != null}" isAdmin="${user.roles[1] ne null}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="settings.title" var="title"/>
    <fragment:bottom-navbar title="./settings:${title}"/>
</header>


<!-- CONFIGURATION -->
<div class="container">
    <%--    <div class="row row-cols-md-2 row-cols-1 gx-md-4 gy-4 gy-md-0">--%>
    <%--        <!-- NOTIFICATIONS -->--%>
    <%--        <div class="col col-lg-3">--%>
    <%--            <div class="card box">--%>
    <%--                <div class="card-body d-flex flex-column gap-3">--%>
    <%--                    <h5 class="card-title fw-bold"><spring:message code="settings.notifications.title"/></h5>--%>
    <%--                    <div class="form-check form-switch p-0">--%>
    <%--                        <label class="form-check-label" for="newNotes">--%>
    <%--                            <spring:message code="settings.notifications.newNotes"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="newNotes">--%>
    <%--                    </div>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="newComments">--%>
    <%--                            <spring:message code="settings.notifications.newComments"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="newComments">--%>
    <%--                    </div>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="newReviews">--%>
    <%--                            <spring:message code="settings.notifications.newReviews"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="newReviews">--%>
    <%--                    </div>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="aboutUs">--%>
    <%--                            <spring:message code="settings.notifications.aboutUs"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="aboutUs">--%>
    <%--                    </div>--%>
    <%--                    <h6 class="card-title fw-bold mt-3"><spring:message code="settings.lapse.title"/></h6>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="immediately">--%>
    <%--                            <spring:message code="settings.lapse.immediately"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="immediately">--%>
    <%--                    </div>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="dailySummary">--%>
    <%--                            <spring:message code="settings.lapse.dailySummary"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="dailySummary">--%>
    <%--                    </div>--%>
    <%--                    <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                        <label class="form-check-label" for="weeklySummary">--%>
    <%--                            <spring:message code="settings.lapse.weeklySummary"/>--%>
    <%--                        </label>--%>
    <%--                        <input class="form-check-input" type="checkbox" id="weeklySummary">--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <%--            </div>--%>
    <%--        </div>--%>

    <%--        <div class="col col-lg-9">--%>
    <%--            <div class="row row-cols-lg-3 row-cols-1 gy-4 gx-lg-4">--%>
    <%--                <!-- PRIVACY -->--%>
    <%--                <div class="col">--%>
    <%--                    <div class="card box">--%>
    <%--                        <div class="card-body d-flex flex-column gap-3">--%>
    <%--                            <h5 class="card-title fw-bold"><spring:message code="settings.privacy.title"/></h5>--%>
    <%--                            <div class="form-check form-switch p-0">--%>
    <%--                                <label class="form-check-label" for="privateProfile">--%>
    <%--                                    <spring:message code="settings.privacy.privateProfile"/>--%>
    <%--                                </label>--%>
    <%--                                <input class="form-check-input" type="checkbox" id="privateProfile">--%>
    <%--                            </div>--%>
    <%--                            <div class="form-check form-switch d-flex justify-content-between p-0">--%>
    <%--                                <label class="form-check-label" for="privateNotes">--%>
    <%--                                    <spring:message code="settings.privacy.privateNotes"/>--%>
    <%--                                </label>--%>
    <%--                                <input class="form-check-input" type="checkbox" id="privateNotes">--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>

    <%--                <!-- APPEARANCE AND INTERFACE -->--%>
    <%--                <div class="col">--%>
    <%--                    <div class="card box">--%>
    <%--                        <div class="card-body d-flex flex-column gap-3">--%>
    <%--                            <h5 class="card-title fw-bold"><spring:message code="settings.ui.title"/></h5>--%>
    <%--                            <div class="form-check form-switch p-0">--%>
    <%--                                <label class="form-check-label" for="darkMode">--%>
    <%--                                    <spring:message code="settings.ui.darkMode"/>--%>
    <%--                                </label>--%>
    <%--                                <input class="form-check-input" type="checkbox" id="darkMode">--%>
    <%--                            </div>--%>
    <%--                        </div>--%>
    <%--                    </div>--%>
    <%--                </div>--%>
    <div class="container mt-5">
        <ul class="nav nav-tabs">
            <li class="nav-item flex-fill">
                <a class="nav-link ${errorsChangePasswordForm == null ? 'active' : '' } text-center" data-toggle="tab"
                   href="#" role="tab" aria-selected="true" id="personalDataTab"><spring:message
                        code="settings.account.personalData"/></a>
            </li>
            <li class="nav-item flex-fill">
                <a class="nav-link ${errorsChangePasswordForm != null ? 'active' : '' }  text-center" data-toggle="tab"
                   href="#" role="tab" aria-selected="false" id="changePasswordTab">
                    <spring:message code="settings.account.changePassword"/></a>
            </li>

        </ul>

        <c:url var="editUserUrl" value="/settings"/>
        <c:url var="userProfilePicture" value="${baseUrl}/${user.userId}/profile/picture"/>
        <c:url var="changePasswordUrl" value="/change-password"/>
        <div class="tab-content">
            <div class="tab-pane fade ${errorsChangePasswordForm == null ? 'show active' : '' }" id="dataTab"
                 role="tab-panel">
                <div class="container my-3">
                    <form:form modelAttribute="editUserForm"
                               action="${editUserUrl}"
                               method="post"
                               enctype="multipart/form-data"
                               class="d-flex flex-column"
                               id="editUserForm">

                        <label for="profile-picture" class="selected-image">
                            <img src="${userProfilePicture}" alt="Profile Picture" id="preview-image">
                            <span><img src="<c:url value="/svg/pencil.svg"/>" class="hidden-pencil"
                                       alt="${edit}"></span>

                        </label>
                        <input type="file" name="profilePicture" style="display: none" id="profile-picture"
                               accept="image/*" onchange="previewImage(event)">

                        <div>
                            <spring:message var="settingsFirstName" code="name"/>
                            <label for="firstName"></label>
                            <p><strong><spring:message code="name"/></strong></p>
                            <form:input type="text" name="firstName" id="firstName" class="form-control bg-bg"
                                        placeholder="${settingsFirstName}" path="firstName" value="${user.firstName}"/>
                            <form:errors path="firstName" cssClass="text-danger" element="p"/>
                        </div>

                        <div>
                            <spring:message var="settingsLastName" code="lastName"/>
                            <label for="lastName"></label>
                            <p><strong><spring:message code="lastName"/></strong></p>
                            <form:input type="text" name="lastName" id="lastName" class="form-control bg-bg"
                                        placeholder="${settingsLastName}" path="lastName" value="${user.lastName}"/>
                            <form:errors path="lastName" cssClass="text-danger" element="p"/>
                        </div>

                        <div>
                            <spring:message var="settingsUsername" code="username"/>
                            <label for="username"></label>
                            <p><strong><spring:message code="username"/></strong></p>
                            <form:input type="text" name="username" id="username" class="form-control bg-bg"
                                        placeholder="${settingsUsername}" path="username" value="${user.username}"/>
                            <form:errors path="username" cssClass="text-danger" element="p"/>
                        </div>

                        <div class="mt-5 d-flex justify-content-center">
                            <spring:message var="update" code="update"/>
                            <input class="btn rounded-box button-primary" type="submit" value="${update}">
                        </div>
                    </form:form>
                </div>
            </div>
            <div class="tab-pane fade ${errorsChangePasswordForm != null ? 'show active' : '' }" id="passwordTab"
                 role="tab-panel">
                <div class="container my-3">
                    <form:form action="${changePasswordUrl}"
                               method="post"
                               modelAttribute="changePasswordForm"
                               id="changePasswordForm"

                    >
                        <label for="oldPassword"></label>
                        <p><strong><spring:message code="settings.account.oldPassword"/></strong></p>
                        <div class="input-group">
                            <spring:message var="settingsOldPassword" code="settings.account.oldPassword"/>
                            <form:input type="password" name="oldPassword" id="password" class="form-control bg-bg"
                                        placeholder="${settingsOldPassword}" path="oldPassword" value=""/>
                            <span class="input-group-text input-group-icon clickable" onclick="password_show_hide();">
                                    <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye"
                                         class="icon-xs fill-dark-primary"/>
                                    <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye"
                                         class="d-none icon-xs fill-dark-primary"/>
                                </span>
                        </div>
                        <form:errors path="oldPassword" cssClass="text-danger" element="p"/>

                        <label for="newPassword"></label>
                        <p><strong><spring:message code="settings.account.newPassword"/></strong></p>
                        <div class="input-group">
                            <spring:message var="settingsNewPassword" code="settings.account.newPassword"/>
                            <form:input type="password" name="newPassword" id="password2" class="form-control bg-bg"
                                        placeholder="${settingsNewPassword}" path="newPassword" value=""/>
                            <span class="input-group-text input-group-icon clickable"
                                  onclick="password_show_hide('2');">
                                    <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye2"
                                         class="icon-xs fill-dark-primary"/>
                                    <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye2"
                                         class="d-none icon-xs fill-dark-primary"/>
                                </span>
                        </div>
                        <form:errors path="newPassword" cssClass="text-danger" element="p"/>

                        <div class="mt-5 d-flex justify-content-center">
                            <spring:message var="update" code="update"/>
                            <input class="btn rounded-box button-primary" type="submit" value="${update}">
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
    </div>


    <!-- PROFILE AND ACCOUNT -->
    <%--    <div class="col col-3">--%>
    <%--        <div class="card box">--%>
    <%--            <div class="card-body d-flex flex-column gap-3">--%>
    <%--                <h5 class="card-title fw-bold"><spring:message code="settings.account.title"/></h5>--%>
    <%--                <a href="#" class="card-link"><spring:message code="settings.account.personalData"/></a>--%>
    <%--                <a href="#" class="card-link"><spring:message code="settings.account.changePassword"/></a>--%>
    <%--            </div>--%>
    <%--        </div>--%>
    <%--    </div>--%>
</div>

<fragment:custom-toast message=""/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
</body>

<script src="<c:url value="/js/settings.js"/>"></script>
<script src="<c:url value="/js/password.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

<script>
    <c:if test="${passwordChanged eq true}">
        displayToast('<spring:message code="toast.changePassword"/>')
    </c:if>

    <c:if test="${userEdited ne null and userEdited eq true}">
        displayToast('<spring:message code="toast.changeInfo"/>')
    </c:if>
</script>
</body>
</html>