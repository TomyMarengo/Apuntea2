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
    <title>Apuntea | <spring:message code="profile.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href=<c:url value="/css/sections/bars.css"/>/>
    <link rel="stylesheet" href="<c:url value="/css/sections/user/profile.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/autocomplete.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet"/>

</head>

<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar user="${user}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="profile.title" var="title"/>
    <fragment:bottom-navbar title="${baseUrl}/profile,${title}"/>
</header>

<main>
    <fragment:sidebar user="${user}"/>

    <section class="my-5 d-flex justify-content-center">
        <div class="mt-5 card box p-3 w-inherit mw-600 w-100">
            <div class="card-body p-3">
                <c:url var="editUserUrl" value="/profile"/>
                <c:url var="userProfilePicture" value="${baseUrl}/profile/${user.userId}/picture"/>

                <form:form modelAttribute="editUserForm"
                           action="${editUserUrl}"
                           method="post"
                           enctype="multipart/form-data"
                           class="d-flex flex-column"
                           id="editUserForm">
                    <h1><spring:message code="profile.title"/></h1>
                    <div class="align-items-center d-flex flex-column" id="image-input">
                        <label for="profilePicture" id="selected-image">
                            <img src="${userProfilePicture}" alt="Profile Picture" class="picture" id="preview-image">
                            <span><img src="<c:url value="/svg/pencil.svg"/>" class="d-none" id="hidden-pencil"
                                       alt="${edit}"></span>
                        </label>
                        <form:errors path="profilePicture" cssClass="text-danger mt-3 align-self-center" element="p"/>
                    </div>

                    <div class="profile-data-container">
                        <div>
                            <div>
                                <spring:message var="profileFirstName" code="name"/>
                                <p><strong><spring:message code="name"/></strong></p>
                                <form:input disabled="true" type="text" id="firstName"
                                            class="form-control bg-bg dynamic-info"
                                            placeholder="${profileFirstName}" path="firstName"
                                            value="${user.firstName}"/>
                                <form:errors path="firstName" cssClass="text-danger" element="p"/>
                            </div>

                            <div>
                                <spring:message var="profileLastName" code="lastName"/>
                                <p><strong><spring:message code="lastName"/></strong></p>
                                <form:input disabled="true" type="text" id="lastName"
                                            class="form-control bg-bg dynamic-info"
                                            placeholder="${profileLastName}" path="lastName" value="${user.lastName}"/>
                                <form:errors path="lastName" cssClass="text-danger" element="p"/>
                            </div>

                            <div>
                                <spring:message var="profileUsername" code="username"/>
                                <p><strong><spring:message code="username"/></strong></p>
                                <form:input disabled="true" type="text" id="username"
                                            class="form-control bg-bg dynamic-info"
                                            placeholder="${profileUsername}" path="username" value="${user.username}"
                                            required="true"/>
                                <form:errors path="username" cssClass="text-danger" element="p"/>
                            </div>
                        </div>

                        <div>
                            <div>
                                <p><strong><spring:message code="career"/></strong></p>
                                <div>
                                    <label for="careerSelect" class="visually-hidden"></label>
                                    <select id="careerSelect" style="display: none;">
                                        <option disabled selected value></option>
                                    </select>

                                    <form:input path="careerId" id="careerId" style="display: none;"
                                                value="${user.career.careerId}"/>

                                    <label for="careerAutocomplete" class="visually-hidden"></label>
                                    <div class="input-group">
                                        <div class="autocomplete">
                                            <spring:message code="search.career.placeholder" var="placeholderCareer"/>
                                            <input type="text" id="careerAutocomplete"
                                                   class="form-control bg-bg special-radius dynamic-info"
                                                   placeholder="${placeholderCareer}" autocomplete="off" required
                                                   disabled
                                                   value="${user.career.name}"/>
                                        </div>
                                        <button type="button" class="input-group-text input-group-icon dynamic-info"
                                                id="eraseCareerButton" disabled>
                                            <img src="<c:url value="/svg/cross.svg"/>"
                                                 alt="<spring:message code="search.sort.image"/>"
                                                 class="icon-xs fill-dark-primary"/>
                                        </button>
                                    </div>

                                    <form:errors path="careerId" cssClass="text-danger" element="p"/>
                                </div>
                            </div>

                            <div>
                                <p><strong><spring:message code="email"/></strong></p>
                                <span class="card-text">${user.email}</span>
                            </div>

                            <div>
                                <p><strong><spring:message code="institution"/></strong></p>
                                <span class="card-text">${user.institution.name}</span>
                            </div>

                        </div>
                    </div>

                    <div class="mt-5 d-flex justify-content-center" id="edit-info-button">
                        <button type="button" class="btn rounded-box button-primary"><spring:message
                                code="editInformation"/></button>
                    </div>

                    <div class="mt-5 d-flex justify-content-center d-none" id="update-info">
                        <button type="button" class="btn rounded-box button-secondary" id="cancel-edit-button">
                            <spring:message code="close"/></button>
                        <button class="btn rounded-box button-primary mx-3"><spring:message code="update"/></button>
                    </div>

                </form:form>
            </div>
        </div>
    </section>
</main>

<fragment:custom-toast message=""/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script>
    const careers = JSON.parse('${careers}');
</script>


<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/profile.js"/>"></script>
<script src="<c:url value="/js/password.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

<script>
    <c:if test="${userEdited ne null and userEdited eq true}">
    displayToast('<spring:message code="toast.changeInfo"/>')
    </c:if>
</script>
</body>
</html>