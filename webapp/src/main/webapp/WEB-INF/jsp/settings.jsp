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

<section class="container d-flex flex-row justify-content-center align-items-center mt-3">
    <div class="card box w-50">
        <div class="card-body p-3 p-md-5 mx-4">
            <c:url var="editUserUrl" value="/settings"/>
            <c:url var="userProfilePicture" value="${baseUrl}/profile/${user.userId}/picture"/>

            <form:form modelAttribute="editUserForm"
                       action="${editUserUrl}"
                       method="post"
                       enctype="multipart/form-data"
                       class="d-flex flex-column"
                       id="editUserForm">
                <h1><spring:message code="profile.title"/></h1>

                <div class="align-items-center d-flex" id="image-input">
                    <label for="profilePicture" id="selected-image">
                        <img src="${userProfilePicture}" alt="Profile Picture" class="picture" id="preview-image">
                        <span><img src="<c:url value="/svg/pencil.svg"/>" class="d-none" id="hidden-pencil"
                                   alt="${edit}"></span>
                    </label>
                    <form:errors path="profilePicture" cssClass="text-danger mt-3 align-self-center" element="p"/>
                </div>


                <div>
                    <spring:message var="settingsFirstName" code="name"/>
                    <label for="firstName"></label>
                    <p><strong><spring:message code="name"/></strong></p>
                    <form:input disabled="true" type="text" name="firstName" id="firstName" class="form-control bg-bg dynamic-info"
                                placeholder="${settingsFirstName}" path="firstName" value="${user.firstName}"/>
                    <form:errors path="firstName" cssClass="text-danger" element="p"/>
                </div>


                <div>
                    <spring:message var="settingsLastName" code="lastName"/>
                    <label for="lastName"></label>
                    <p><strong><spring:message code="lastName"/></strong></p>
                    <form:input disabled="true"  type="text" name="lastName" id="lastName" class="form-control bg-bg dynamic-info"
                                placeholder="${settingsLastName}" path="lastName" value="${user.lastName}"/>
                    <form:errors path="lastName" cssClass="text-danger" element="p"/>
                </div>


                <div>
                    <spring:message var="settingsUsername" code="username"/>
                    <label for="username"></label>
                    <p><strong><spring:message code="username"/></strong></p>
                    <form:input disabled="true" type="text" name="username" id="username" class="form-control bg-bg dynamic-info"
                                placeholder="${settingsUsername}" path="username" value="${user.username}"
                                required="true"/>
                    <form:errors path="username" cssClass="text-danger" element="p"/>
                </div>

                <div class="d-flex flex-column mt-4">
                    <p><strong><spring:message code="email"/></strong></p>
                    <span class="card-text">${user.email}</span>
                </div>
                <div class="d-flex flex-column my-4">
                    <p><strong><spring:message code="roles"/></strong></p>
                    <c:forEach items="${user.roles}" var="r">
                            <span class="card-text">
                            <c:choose>
                                <c:when test="${r eq 'ROLE_STUDENT'}">
                                    <spring:message code="role.student"/>
                                </c:when>
                                <c:when test="${r eq 'ROLE_MODERATOR'}">
                                    <spring:message code="role.moderator"/>
                                </c:when>
                                <c:when test="${r eq 'ROLE_ADMIN'}">
                                    <spring:message code="role.admin"/>
                                </c:when>
                            </c:choose>
                            </span>
                    </c:forEach>
                </div>
                <div class="d-flex flex-column mb-4">
                    <p><strong><spring:message code="institution"/></strong></p>
                    <span class="card-text">${user.institution.name}</span>
                </div>
                <div class="d-flex flex-column">
                    <p><strong><spring:message code="career"/></strong></p>
                    <span class="card-text">${user.career.name}</span>
                </div>

                <div class="mt-5 d-flex justify-content-center d-none" id="update-info">
                    <spring:message var="update" code="update"/>
                    <input class="btn rounded-box button-primary mx-3" id="update-button" type="submit" value="${update}">
                    <input type="button" class="btn rounded-box button-secondary" id="cancel-edit-button" value="<spring:message code="close"/>">
                </div>

                <div class="mt-5 d-flex justify-content-center" id="edit-info-button">
                    <input type="button" class="btn rounded-box button-primary" value="<spring:message code="editInformation"/>">
                </div>
            </form:form>
        </div>
    </div>
</section>


<fragment:custom-toast message=""/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>

<script src="<c:url value="/js/settings.js"/>"></script>
<script src="<c:url value="/js/password.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

<script>
    <c:if test="${userEdited ne null and userEdited eq true}">
    displayToast('<spring:message code="toast.changeInfo"/>')
    </c:if>
</script>
</body>
</html>