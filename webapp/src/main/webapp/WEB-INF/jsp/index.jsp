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
    <title>Apuntea</title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/landing.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <fragment:navbar user="${user}"/>
</header>

<main>
        <fragment:sidebar user="${user}"/>

    <section class="d-flex flex-column justify-content-center align-items-center gap-3 text-center">
        <h2><spring:message code="index.title" arguments='<span class="apuntea-title">Apuntea</span>'/></h2>
        <h4><spring:message code="index.subtitle"/></h4>

        <div class="index-cards">

            <a href="${baseUrl}/my-notes" class="call-to-action-card">

                <img src="<c:url value="/svg/add-document.svg"/>" alt="<spring:message code="search.title"/>"
                     class="fill-dark-text"/>

                <div class="call-to-action-card-body">
                    <div class="call-to-action-card-title">
                        <h3><spring:message code="index.card2.title"/></h3>
                    </div>
                    <p><spring:message code="index.card2.body"/></p>
                </div>

            </a>

            <c:if test="${user ne null}">
                <c:url var="urlParams" value="${baseUrl}/search?institutionId=${user.institution.institutionId}&careerId=${user.career.careerId}"/>
            </c:if>

            <c:if test="${user eq null}">
                <c:url var="urlParams" value="${baseUrl}/search"/>
            </c:if>

            <a href="${urlParams}" class="call-to-action-card">

                <img src="<c:url value="/svg/search-alt.svg"/>" alt="<spring:message code="search.title"/>"
                     class="fill-dark-text"/>

                <div class="call-to-action-card-body">
                    <div class="call-to-action-card-title">
                        <h3><spring:message code="index.card3.title"/></h3>
                    </div>
                    <p><spring:message code="index.card3.body"/></p>
                </div>

            </a>

            <c:if test="${user != null}">
                <a href="${baseUrl}/profile" class="call-to-action-card">

                    <img src="<c:url value="/svg/user.svg"/>" alt="<spring:message code="search.title"/>"
                         class="fill-dark-text"/>

                    <div class="call-to-action-card-body">

                        <div class="call-to-action-card-title">
                            <h3><spring:message code="index.card1.title.editProfile"/></h3>
                        </div>
                        <p><spring:message code="index.card1.body.editProfile"/></p>
                    </div>

                </a>
            </c:if>

            <c:if test="${user == null}">
                <a href="${baseUrl}/register" class="call-to-action-card">

                    <img src="<c:url value="/svg/users.svg"/>" alt="<spring:message code="search.title"/>"
                         class="fill-dark-text"/>

                    <div class="call-to-action-card-body">

                        <div class="call-to-action-card-title">
                            <h3><spring:message code="index.card1.title.register"/></h3>
                        </div>
                        <p><spring:message code="index.card1.body.register"/></p>
                    </div>

                </a>
            </c:if>

        </div>
    </section>
</main>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/popups.js"/>"></script>

</body>

</html>