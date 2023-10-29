<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="forgotPassword.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/autocomplete.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/navbar.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/user/register-login.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>

<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar user="${user}"/>

    <c:url var="forgotUrl" value="/forgot-password"/>
    <spring:message var="logotype" code="logotype"/>

</header>


<section class="login-register-container container d-flex flex-column justify-content-center align-items-center">

    <div class="row">
        <div class="card box">
            <div class="row">
                <div class="col-lg-6">
                    <div class="card-body p-3 p-md-5 mx-4">
                        <form:form modelAttribute="forgotPasswordForm" action="${forgotUrl}" method="post">
                            <h3><spring:message code="forgotPassword.title"/></h3>

                            <div>
                                <spring:message var="emailPlaceholder" code="forgotPassword.email"/>
                                <label for="email"></label>
                                <p class="text-sm text-muted mb-2"><spring:message code="forgotPassword.subtitle"/></p>
                                <form:input path="email" type="email" class="form-control bg-bg mb-2" id="email"
                                            placeholder="${emailPlaceholder}" required="true"
                                            autofocus="autofocus"/>
                                <form:errors path="email" cssClass="text-danger" element="p"/>
                            </div>
                            <div class="d-flex flex-row mt-4 justify-content-end">
                                <div class="d-flex align-items-center justify-content-center"
                                     style="margin-right: 1em;">
                                    <a href="./login">
                                        <button type="button" class="btn login-register-button box"><spring:message
                                                code="cancel"/></button>
                                    </a>
                                </div>
                                <div class="d-flex justify-content-center">
                                    <spring:message var="forgotPassword" code="forgotPassword.title"/>
                                    <input class="btn rounded-box button-primary" type="submit"
                                           value="${forgotPassword}">
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>

                <div class="col-lg-6 box d-flex align-items-center we-are-more-container">
                    <div class="text-center px-5 py-5 ">
                        <div class="text-center mb-5">
                            <img src="<c:url value="/image/teacher.png"/>" alt="${logotype}"
                                 style="width: 40px; height: 40px;">
                            <h3 class="mt-1 text-bg"><spring:message code="login.weAre"/></h3>
                        </div>
                        <h4 class="mb-4"><spring:message code="login.weAreMore.title"/></h4>
                        <p><spring:message code="login.weAreMore.subtitle"/></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

</section>

<script>
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>

</body>

</html>
