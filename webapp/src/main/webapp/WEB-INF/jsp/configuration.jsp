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
    <title>Apuntea | <spring:message code="configuration"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/apuntea-icon.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/color-picker.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/user/profile.css"/>"/>

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
    <fragment:navbar user="${user}" institutionId="${user.institutionId}" careerId="${user.career.careerId}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="configuration" var="title"/>
    <fragment:bottom-navbar title="${baseUrl}/configuration,${title}"/>
</header>

<main>
    <fragment:sidebar user="${user}"/>

    <section class="d-flex justify-content-center column-gap-5 flex-wrap" style="margin-top: 5rem;">
        <div class="card box p-3 w-inherit mw-600" style="block-size: fit-content">
            <c:url var="changePasswordUrl" value="/change-password"/>
            <form:form action="${changePasswordUrl}"
                       method="post"
                       modelAttribute="changePasswordForm"
                       id="changePasswordForm"
                       class="card-body p-3">
                    <h1><spring:message code="changePassword.title"/></h1>
                    <div class="d-flex flex-column mt-3 px-3 gap-3">
                        <div class="d-flex flex-column gap-2">
                            <label for="oldPassword"><strong><spring:message code="changePassword.oldPassword"/></strong></label>
                            <div class="input-group">
                                <spring:message var="oldPassword" code="changePassword.oldPassword"/>
                                <form:input type="password" name="oldPassword" id="password" class="form-control bg-bg"
                                            placeholder="${oldPassword}" path="oldPassword" value="" required="true"/>
                                <button type="button" class="input-group-text input-group-icon" onclick="passwordShowHide()">
                                    <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye"
                                         class="icon-xs fill-dark-primary"/>
                                    <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye"
                                         class="d-none icon-xs fill-dark-primary"/>
                                </button>
                            </div>
                            <form:errors path="oldPassword" cssClass="text-danger" element="p"/>
                        </div>

                        <div class="d-flex flex-column gap-1">
                            <label for="newPassword"><strong><spring:message code="changePassword.newPassword"/></strong></label>
                            <div class="input-group">
                                <spring:message var="newPassword" code="changePassword.newPassword"/>
                                <form:input type="password" name="newPassword" id="password2" class="form-control bg-bg"
                                            placeholder="${newPassword}" path="newPassword" value="" required="true"/>
                                <button type="button" class="input-group-text input-group-icon"
                                        onclick="passwordShowHide('2');">
                                    <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye2"
                                         class="icon-xs fill-dark-primary"/>
                                    <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye2"
                                         class="d-none icon-xs fill-dark-primary"/>
                                </button>
                            </div>
                            <form:errors path="newPassword" cssClass="text-danger" element="p"/>
                        </div>
                    </div>

                    <div class="mt-4 d-flex justify-content-center">
                        <button class="btn rounded-box button-primary"><spring:message code="update"/></button>
                    </div>
                </form:form>
        </div>
        <div class="card box p-3 w-inherit mw-500" style="block-size: fit-content">
                <c:set var="notificationsEnabledUrl" value="${baseUrl}/enable-notifications"/>
                <c:set var="notificationsDisabledUrl" value="${baseUrl}/disable-notifications"/>
                <form:form action="${notificationsEnabled ? notificationsDisabledUrl : notificationsEnabledUrl}"
                           method="post" id="changeNotificationsForm" cssClass="card-body p-3">
                    <h1><spring:message code="otherConfigurations.title"/></h1>
                    <div class="form-check form-switch px-3 mt-3">
                        <label class="form-check-label" for="receiveMailsSwitch"><spring:message code="otherConfigurations.receiveMails"/></label>
                        <input type="checkbox" id="receiveMailsSwitch" class="form-check-input" style="width: 3rem; height: 1.3rem;"
                               onclick="document.getElementById('changeNotificationsForm').submit()"/>
                    </div>
                </form:form>
        </div>
    </section>
</main>

<fragment:custom-toast message=""/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
</body>

<script src="<c:url value="/js/password.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

<script>
    <c:if test="${passwordChanged eq true}">
        displayToast('<spring:message code="toast.changePassword"/>')
    </c:if>
</script>

<script>
    <c:if test="${notificationsEnabled eq true}">
        document.getElementById('receiveMailsSwitch').checked = true;
    </c:if>
</script>
</body>
</html>