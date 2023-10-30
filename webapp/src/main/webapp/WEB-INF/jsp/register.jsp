<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="halloween">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="register.title"/></title>
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

    <c:url var="registerUrl" value="/register"/>
    <spring:message var="logotype" code="logotype"/>
</header>


<section class="login-register-container container d-flex flex-column justify-content-center align-items-center">

    <div class="row">
        <div class="card box">
            <div class="row">
                <div class="col-lg-6">
                    <div class="card-body p-3 p-md-5 mx-4">

                        <form:form modelAttribute="userForm" action="${registerUrl}" method="post">
                            <h1><spring:message code="register.title"/></h1>

                            <div class="">
                                <spring:message var="registerEmail" code="email"/>
                                <label for="email"></label>
                                <form:input path="email" type="email" id="email" class="form-control bg-bg"
                                            placeholder="${registerEmail}" required="true"/>
                                <form:errors path="email" cssClass="text-danger" element="p"/>
                            </div>

                            <label for="password"></label>
                            <div class="input-group">
                                <spring:message var="registerPassword" code="password"/>
                                <form:input path="password" type="password" id="password" class="form-control bg-bg"
                                            placeholder="${registerPassword}" required="true"/>
                                <span class="input-group-text input-group-icon clickable"
                                      onclick="password_show_hide();">
                                    <img src="<c:url value="/svg/eye.svg"/>" alt="" id="show_eye"
                                         class="icon-xs fill-dark-primary"/>
                                    <img src="<c:url value="/svg/eye-crossed.svg"/>" alt="" id="hide_eye"
                                         class="d-none icon-xs fill-dark-primary"/>
                                </span>
                            </div>
                            <form:errors path="password" cssClass="text-danger" element="p"/>


                            <%--                            <div class="">--%>
                            <%--                                <spring:message var="registerRepeatPassword" code="repeatPassword"/>--%>
                            <%--                                <label for="password"></label>--%>
                            <%--                                <form:input path="repeatPassword" type="password" id="password"--%>
                            <%--                                            class="form-control bg-bg"--%>
                            <%--                                            placeholder="${registerRepeatPassword}"/>--%>
                            <%--                                <form:errors path="repeatPassword" cssClass="text-danger" element="p"/>--%>
                            <%--                            </div>--%>

                            <div class="">
                                <label for="institutionSelect"></label>
                                <select id="institutionSelect" style="display: none;">
                                    <option disabled selected value></option>
                                </select>

                                <form:input path="institutionId" id="institutionId" style="display: none;"/>

                                <label for="institutionAutocomplete"></label>
                                <div class="input-group">
                                    <div class="autocomplete ">
                                        <spring:message code="search.institution.placeholder"
                                                        var="placeholderInstitution"/>
                                        <input type="text" id="institutionAutocomplete"
                                               class="form-control bg-bg special-radius"
                                               placeholder="${placeholderInstitution}" autocomplete="off" required/>

                                    </div>
                                    <button type="button" class="input-group-text input-group-icon"
                                          id="eraseInstitutionButton">
                                        <img src="<c:url value="/svg/cross.svg"/>"
                                             alt="<spring:message code="search.sort.image"/>"
                                             class="icon-xs fill-dark-primary"/>
                                    </button>
                                </div>

                                <form:errors path="institutionId" cssClass="text-danger" element="p"/>
                            </div>

                            <div class="">
                                <label for="careerSelect"></label>
                                <select id="careerSelect" style="display: none;">
                                    <option disabled selected value></option>
                                </select>

                                <form:input path="careerId" id="careerId" style="display: none;"/>

                                <label for="careerAutocomplete"></label>
                                <div class="input-group">
                                    <div class="autocomplete">
                                        <spring:message code="search.career.placeholder" var="placeholderCareer"/>
                                        <input type="text" id="careerAutocomplete"
                                               class="form-control bg-bg special-radius"
                                               placeholder="${placeholderCareer}" autocomplete="off" required/>
                                    </div>
                                    <button type="button" class="input-group-text input-group-icon" id="eraseCareerButton">
                                        <img src="<c:url value="/svg/cross.svg"/>"
                                             alt="<spring:message code="search.sort.image"/>"
                                             class="icon-xs fill-dark-primary"/>
                                    </button>
                                </div>

                                <form:errors path="careerId" cssClass="text-danger" element="p"/>
                            </div>


                            <div class="mt-3 d-flex justify-content-center">
                                <spring:message var="register" code="register.title"/>
                                <input class="btn rounded-box button-primary" type="submit" value="${register}">
                            </div>

                        </form:form>

                        <div class="d-flex align-items-center justify-content-center mt-3">
                            <p class="mb-0 me-2"><spring:message code="register.have"/></p>
                            <a href="./login">
                                <button type="button" class="btn login-register-button box"><spring:message
                                        code="login.title"/></button>
                            </a>
                        </div>

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
    const {institutions, careerMap, subjectMap} = JSON.parse('${institutionData}');
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ics-autocomplete.js"/>"></script>
<script src="<c:url value="/js/password.js"/>"></script>


</body>

</html>

