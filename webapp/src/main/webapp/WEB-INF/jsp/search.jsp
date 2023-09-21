<%--suppress HtmlFormInputWithoutLabel --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl" />

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark" data-search-view="horizontal">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="search.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

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
    <link rel="stylesheet" href="<c:url value="/css/sections/search/table-list.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar/>

<!-- BOTTOM-NAVBAR -->
<fragment:bottom-navbar title="./search:Búsqueda" extraLinks="">
</fragment:bottom-navbar>



<!-- SEARCH -->
<div class="container">
    <c:url var="searchUrl" value="./search"/>
    <form:form modelAttribute="searchNotesForm"
               action="${searchUrl}"
               method="get"
               id="searchForm"
               cssClass="d-flex flex-column w-100 align-items-center">
        <div class="row row-cols-2 row-cols-md-3">
            <div class="col">
                <select id="institutionSelect" style="display: none;">
                    <option disabled selected value></option>
                    <c:forEach items="${institutions}" var="inst">
                        <option value="<c:out value="${inst.institutionId}"/>"><c:out value="${inst.name}"/></option>
                    </c:forEach>
                </select>

                <form:input path="institutionId" id="institutionId" style="display: none;"/>

                <div class="input-group mb-3">
                    <div class="autocomplete">
                        <spring:message code="search.institution.placeholder" var="placeholderInstitution"/>
                        <input type="text" id="institutionAutocomplete" class="form-control bg-bg"
                               placeholder="${placeholderInstitution}"/>
                    </div>
                </div>
            </div>

            <div class="col">
                <select id="careerSelect" style="display: none;">
                    <option disabled selected value></option>
                    <c:forEach items="${careers}" var="career">
                        <option value="<c:out value="${career.careerId}"/>"><c:out value="${career.name}"/></option>
                    </c:forEach>
                </select>

                <form:input path="careerId" id="careerId" style="display: none;"/>

                <div class="input-group mb-3">
                    <div class="autocomplete">
                        <spring:message code="search.career.placeholder" var="placeholderCareer"/>
                        <input type="text" id="careerAutocomplete" class="form-control bg-bg"
                               placeholder="${placeholderCareer}"/>
                    </div>
                </div>
            </div>

            <div class="col">
                <select id="subjectSelect" style="display: none;">
                    <option disabled selected value></option>
                    <c:forEach items="${subjects}" var="subject">
                        <option value="<c:out value="${subject.subjectId}"/>"><c:out value="${subject.name}"/></option>
                    </c:forEach>
                </select>

                <form:input path="subjectId" id="subjectId" style="display: none;"/>

                <div class="input-group mb-3">
                    <div class="autocomplete">
                        <spring:message code="search.subject.placeholder" var="placeholderSubject"/>
                        <input type="text" id="subjectAutocomplete" class="form-control bg-bg"
                               placeholder="${placeholderSubject}"/>
                    </div>
                </div>
            </div>

            <div class="col" >
                <div class="input-group mb-3">
                    <form:select path="category" class="form-select bg-bg" id="categorySelect">
                        <form:option
                                value=""><spring:message
                                code="search.category.all"/></form:option>
                        <form:option
                                value="theory"><spring:message
                                code="search.category.theory"/></form:option>
                        <form:option
                                value="practice"><spring:message
                                code="search.category.practice"/></form:option>
                        <form:option
                                value="exam"><spring:message
                                code="search.category.exam"/></form:option>
                        <form:option
                                value="other"><spring:message
                                code="search.category.other"/></form:option>
                    </form:select>
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <span class="input-group-text input-group-icon clickable">
                        <form:checkbox path="ascending" id="ascCheckbox" cssClass="d-none"/>
                            <c:if test="${searchNotesForm.ascending}">
                                <img src="<c:url value="/svg/arrow-up.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-dark-primary"
                                     id="arrowImage" title="ascending"/>
                            </c:if>
                            <c:if test="${!searchNotesForm.ascending}">
                                <img src="<c:url value="/svg/arrow-down.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-dark-primary"
                                     id="arrowImage" title="descending"/>
                            </c:if>
                    </span>

                    <form:select path="sortBy" class="form-select bg-bg" id="sortBySelect">
                        <form:option value="score"><spring:message code="search.sort.score"/></form:option>
                        <%--                        <form:option value=""><spring:message code="search.sort.placeholder"/></form:option>--%>
                        <form:option value="name"><spring:message code="search.sort.name"/></form:option>
                        <form:option value="date"><spring:message code="search.sort.date"/></form:option>
                    </form:select>
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                    <spring:message code="search.word.placeholder" var="placeholderSearch" />
                    <form:input path="word" type="text" class="form-control bg-bg" placeholder='${placeholderSearch}'/>
                </div>
            </div>

        </div>

        <div class="search-input w-25">
            <button type="submit" class="btn button-primary w-100"><spring:message code="search.button"/></button>
        </div>
    </form:form>

</div>

<!-- LIST OF NOTES MATCHING -->
<spring:message code="download" var="download"/>
<spring:message code="copy" var="copy"/>
<spring:message code="search.toggleView" var="searchViewImage"/>
<c:url value="/svg/box-list.svg" var="boxViewUrl"/>
<c:url value="/svg/horizontal-list.svg" var="horizontalViewUrl"/>

<c:if test="${not empty notes}">
    <div class="d-flex container mt-4 justify-content-between p-0">
        <button id="searchViewToggle" class="btn nav-icon-button" type="button">
            <img id="searchViewIcon" src="${horizontalViewUrl}" alt="${searchViewImage}" class="icon-s fill-dark-primary" />
        </button>
        <div class="d-flex">
            <div id="selectedButtons" class="align-items-center" style="display: none;">
                <button id="deselectAllButton" class="btn nav-icon-button" type="button">
                    <img src="<c:url value="/svg/cross.svg"/>" alt="deselect" class="icon-s fill-dark-primary" />
                </button>
                <span class="text-dark-primary mx-2">
                    <strong id="selectedCount" class="text-dark-primary"> 0 </strong>
                    <spring:message code="search.selected"/>
                </span>
                <button id="downloadSelectedButton" class="btn nav-icon-button" type="button">
                    <img src="<c:url value="/svg/download.svg"/>" alt="download" class="icon-s fill-dark-primary" />
                </button>
                <button id="copySelectedButton" class="btn nav-icon-button" type="button">
                    <img src="<c:url value="/svg/link.svg"/>" alt="copy" class="icon-s fill-dark-primary" />
                </button>
            </div>
            <button id="selectAllButton" class="btn nav-icon-button" type="button">
                <img src="<c:url value="/svg/list-check.svg"/>" alt="select all" class="icon-s fill-dark-primary" />
            </button>
        </div>

    </div>

    <!-- HORIZONTAL LIST -->
    <section class="container mt-4 p-0" id="horizontalList">
        <div class="table-responsive">
            <table class="table table-hover table-search">
                <thead>
                <tr>
                    <th><spring:message code="name"/></th>
    <%--                <th><spring:message code="owner"/></th>--%>
                    <th><spring:message code="createdAt"/></th>
                    <th><spring:message code="score"/></th>
                    <th></th>
                    <!-- TODO: ADD SIZE OF FILE -->
                </tr>
                </thead>
                <tbody>
                <c:forEach var="note" items="${notes}">
                    <c:set var="date" value="${note.createdAt}"/>
                    <tr class="note-found no-select" id="<c:out value="${note.noteId}"/>1">
                        <td>
                            <img src="image/pdf.png" alt="pdf" class="icon-m"> <!--TODO: c:if note.type -->
                            <c:out value="${note.name}"/>
                        </td>
    <%--                    <td>owner</td>--%>
                        <td><spring:message code="date.format"
                                            arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/></td>
                        <td>
                            <c:if test="${note.avgScore eq 0}">
                                <spring:message code="score.none"/>
                            </c:if>
                            <c:if test="${note.avgScore ne 0}">
                                <fmt:formatNumber type="number" maxFractionDigits="1" value="${note.avgScore}"/>
                            </c:if>
                        </td>

                        <td class="search-actions">
                            <a href="./notes/${note.noteId}/download" download="${note.name}">
                                <button class="btn button-expansion rounded-circle">
                                    <img src="<c:url value="/svg/download.svg"/>" alt="${download}" class="icon-xs fill-text">
                                </button>
                            </a>
                            <button class="btn button-expansion rounded-circle copy-button" id="<c:out value="${note.noteId}"/>c1">
                                <img src="<c:url value="/svg/link.svg"/>" alt="${copy}" class="icon-xs fill-text">
                            </button>
                            <input type="checkbox" class="select-checkbox d-none"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <!-- BOX LIST -->
    <section class="container mt-4 p-0" id="boxList">
        <div class="row">
            <c:forEach items="${notes}" var="note">
                <div class="col-md-4 mb-4">
                    <div class="note-found card box search-note-box" id="<c:out value="${note.noteId}"/>2">
                        <div class="card-body no-select">
                            <h4 class="card-title">
                                    <img src="image/pdf.png" alt="pdf" class="icon-m"> <!--TODO: c:if note.type -->
                                    <c:out value="${note.name}"/>
                            </h4>

    <%--                        <span class="card-text">--%>
    <%--                            <strong><spring:message code="owner"/></strong>:--%>
    <%--                            owner--%>
    <%--                        </span>--%>
    <%--                        <br>--%>

                            <span class="card-text"><strong><spring:message code="category"/></strong>:
                                <c:if test="${note.category.formattedName eq 'Theory'}">
                                    <spring:message code="search.category.theory"/>
                                </c:if>
                                <c:if test="${note.category.formattedName eq 'Practice'}">
                                    <spring:message code="search.category.practice"/>
                                </c:if>
                                <c:if test="${note.category.formattedName eq 'Exam'}">
                                    <spring:message code="search.category.exam"/>
                                </c:if>
                                <c:if test="${note.category.formattedName eq 'Other'}">
                                    <spring:message code="search.category.other"/>
                                </c:if>
                            </span>

                            <br>

                            <span class="card-text">
                                <strong><spring:message code="createdAt"/></strong>:
                                <spring:message code="date.format" arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/>
                            </span>

                            <br>

                            <span class="card-text">
                                <strong><spring:message code="score"/></strong>:
                                <fmt:formatNumber type="number" maxFractionDigits="1" value="${note.avgScore}"/>
                            </span>

                            <input type="checkbox" class="select-checkbox d-none"/>

                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </section>
</c:if>

<c:if test="${empty notes}">
    <section class="container mt-4">
        <p><spring:message code="notes.noNotes"/></p>
    </section>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ascdesc.js"/>"></script>
<script src="<c:url value="/js/note-list.js"/>"></script>
<script src="<c:url value="/js/buttons.js"/>"></script>

</body>

</html>