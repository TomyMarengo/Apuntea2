<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en">

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
    <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/user/register-login.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>

<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar/>

    <c:url var="challengeUrl" value="/challenge"/>
    <spring:message var="logotype" code="logotype"/>

</header>

<main class="full">
    <section class="container d-flex align-self-center">
        <div class="row">
            <div class="card box">
                <div class="row">
                    <div class="col-lg-6">
                        <div class="card-body p-3 p-md-5 mx-4">
                            <form:form modelAttribute="challengeForm" action="${challengeUrl}" method="post">
                                <div class="mb-2">
                                    <h3 class="mb-2"><spring:message code="forgotPassword.title"/></h3>
                                    <p class="text-sm text-muted mb-0"><spring:message
                                            code="forgotPassword.challenge.title"/></p>
                                </div>

                                <div class="">
                                    <spring:message var="codePlaceholder" code="forgotPassword.code"/>
                                    <label for="code"></label>
                                    <form:input path="code" type="text" class="form-control bg-bg" id="code"
                                                placeholder="${codePlaceholder}" required="true" autofocus="autofocus"/>
                                    <form:errors path="code" cssClass="text-danger" element="p"/>
                                    <c:if test="${invalidCode}">
                                        <p class="text-danger"><spring:message code="Pattern.challengeForm.code"/></p>
                                    </c:if>
                                </div>

                                <label for="password"></label>
                                <div class="input-group">
                                    <spring:message var="passwordPlaceholder" code="forgotPassword.newPassword"/>
                                    <form:input path="newPassword" type="password" id="password"
                                                class="form-control bg-bg"
                                                placeholder="${passwordPlaceholder}" required="true"/>
                                    <button type="button" class="input-group-text input-group-icon"
                                            onclick="passwordShowHide();">
                                        <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye"
                                             class="icon-xs fill-dark-primary"/>
                                        <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye"
                                             class="d-none icon-xs fill-dark-primary"/>
                                    </button>
                                </div>
                                <form:errors path="newPassword" cssClass="text-danger" element="p"/>

                                <input type="hidden" value="${email}" name="email"/>

                                <div class="mt-3 d-flex justify-content-center">
                                    <button class="btn rounded-box button-primary">
                                        <spring:message code="forgotPassword.title"/>
                                    </button>
                                </div>

                                <%--                            <div class="mb-4">--%>
                                <%--                                <p class="text-sm text-muted mb-0"><spring:message code="forgotPassword.back"/> <a--%>
                                <%--                                            href="<c:url value="/login"/>"><spring:message code="forgotPassword.login"/></a></p>--%>
                                <%--                            </div>--%>
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
</main>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/password.js"/>"></script>

</body>

</html>

