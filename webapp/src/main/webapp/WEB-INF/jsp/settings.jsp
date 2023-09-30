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

    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet"/>

</head>

<body>

<!-- NAVBAR -->
<fragment:navbar/>

<!-- BOTTOM-NAVBAR -->
<spring:message code="settings.title" var="title"/>
<fragment:bottom-navbar title="./settings:${title}"/>


<!-- CONFIGURATION -->
<div class="container">
    <div class="row row-cols-md-2 row-cols-1 gx-md-4 gy-4 gy-md-0">
        <!-- NOTIFICATIONS -->
        <div class="col col-lg-3">
            <div class="card box">
                <div class="card-body d-flex flex-column gap-3">
                    <h5 class="card-title fw-bold"><spring:message code="settings.notifications.title"/></h5>
                    <div class="form-check form-switch p-0">
                        <label class="form-check-label" for="newNotes">
                            <spring:message code="settings.notifications.newNotes"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="newNotes">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="newComments">
                            <spring:message code="settings.notifications.newComments"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="newComments">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="newReviews">
                            <spring:message code="settings.notifications.newReviews"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="newReviews">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="aboutUs">
                            <spring:message code="settings.notifications.aboutUs"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="aboutUs">
                    </div>
                    <h6 class="card-title fw-bold mt-3"><spring:message code="settings.lapse.title"/></h6>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="immediately">
                            <spring:message code="settings.lapse.immediately"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="immediately">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="dailySummary">
                            <spring:message code="settings.lapse.dailySummary"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="dailySummary">
                    </div>
                    <div class="form-check form-switch d-flex justify-content-between p-0">
                        <label class="form-check-label" for="weeklySummary">
                            <spring:message code="settings.lapse.weeklySummary"/>
                        </label>
                        <input class="form-check-input" type="checkbox" id="weeklySummary">
                    </div>
                </div>
            </div>
        </div>

        <div class="col col-lg-9">
            <div class="row row-cols-lg-3 row-cols-1 gy-4 gx-lg-4">
                <!-- PRIVACY -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold"><spring:message code="settings.privacy.title"/></h5>
                            <div class="form-check form-switch p-0">
                                <label class="form-check-label" for="privateProfile">
                                    <spring:message code="settings.privacy.privateProfile"/>
                                </label>
                                <input class="form-check-input" type="checkbox" id="privateProfile">
                            </div>
                            <div class="form-check form-switch d-flex justify-content-between p-0">
                                <label class="form-check-label" for="privateNotes">
                                    <spring:message code="settings.privacy.privateNotes"/>
                                </label>
                                <input class="form-check-input" type="checkbox" id="privateNotes">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- APPEARANCE AND INTERFACE -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold"><spring:message code="settings.ui.title"/></h5>
                            <div class="form-check form-switch p-0">
                                <label class="form-check-label" for="darkMode">
                                    <spring:message code="settings.ui.darkMode"/>
                                </label>
                                <input class="form-check-input" type="checkbox" id="darkMode">
                            </div>
                        </div>
                    </div>
                </div>

                <!-- PROFILE AND ACCOUNT -->
                <div class="col">
                    <div class="card box">
                        <div class="card-body d-flex flex-column gap-3">
                            <h5 class="card-title fw-bold"><spring:message code="settings.account.title"/></h5>
                            <a href="#" class="card-link"><spring:message code="settings.account.changePassword"/></a>
                            <a href="#" class="card-link"><spring:message code="settings.account.deleteAccount"/></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/global-search.js"/>"></script>

</body>
</html>