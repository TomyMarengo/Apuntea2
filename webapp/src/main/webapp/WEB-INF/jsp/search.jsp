<%--suppress HtmlFormInputWithoutLabel --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

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
<div class="container d-flex flex-column w-100">
    <c:url var="searchUrl" value="./search"/>
    <form:form modelAttribute="searchNotesForm"
               action="${searchUrl}"
               method="get"
               id="searchForm">
        <div class="row row-cols-1 row-cols-md-3 row-cols-xl-6">
            <div class="col">
                <select id="institutionSelect" style="display: none;">
                    <option disabled selected value></option>
                    <c:forEach items="${institutions}" var="inst">
                        <option value="${inst.institutionId}">${inst.name}</option>
                    </c:forEach>
                </select>

                <form:input path="institutionId" id="institutionId" style="display: none;"/>

                <div class="input-group mb-3">
                        <span class="input-group-text input-group-icon">
                            <img src="<c:url value="/svg/school.svg"/>"
                                 alt="<spring:message code="search.institution.placeholder"/>"
                                 class="icon-s fill-text"/>
                        </span>
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
                        <option value="${career.careerId}">${career.name}</option>
                    </c:forEach>
                </select>

                <form:input path="careerId" id="careerId" style="display: none;"/>

                <div class="input-group mb-3">
                        <span class="input-group-text input-group-icon">
                            <img src="<c:url value="/svg/books.svg"/>"
                                 alt="<spring:message code="search.career.placeholder"/>" class="icon-s fill-text"/>
                        </span>
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
                        <option value="${subject.subjectId}">${subject.name}</option>
                    </c:forEach>
                </select>

                <form:input path="subjectId" id="subjectId" style="display: none;"/>

                <div class="input-group mb-3">
                        <span class="input-group-text input-group-icon">
                            <img src="<c:url value="/svg/book-alt.svg"/>"
                                 alt="<spring:message code="search.subject.placeholder"/>" class="icon-s fill-text"/>
                        </span>
                    <div class="autocomplete">
                        <spring:message code="search.subject.placeholder" var="placeholderSubject"/>
                        <input type="text" id="subjectAutocomplete" class="form-control bg-bg"
                               placeholder="${placeholderSubject}"/>
                    </div>
                </div>
            </div>

            <div class="col">
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
                    <form:select path="score" class="form-select bg-bg" id="scoreSelect">
                        <form:option value=""><spring:message code="search.score.all"/></form:option>
                        <form:option value="5">⭐⭐⭐⭐⭐</form:option>
                        <form:option value="4">⭐⭐⭐⭐</form:option>
                        <form:option value="3">⭐⭐⭐</form:option>
                        <form:option value="2">⭐⭐</form:option>
                        <form:option value="1">⭐</form:option>
                    </form:select>
                </div>
            </div>

            <div class="col">
                <div class="input-group mb-3">
                        <span class="input-group-text input-group-icon">

                            <form:checkbox path="ascending" id="ascCheckbox" name="ascending" cssClass="d-none"/>

                            <c:if test="${searchNotesForm.ascending}">
                                <img src="<c:url value="/svg/arrow-up.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-text"
                                     id="arrowImage" title="ascending"/>
                            </c:if>
                            <c:if test="${!searchNotesForm.ascending}">
                                <img src="<c:url value="/svg/arrow-down.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-text"
                                     id="arrowImage" title="descending"/>
                            </c:if>
                        </span>
                    <form:select path="sortBy" class="form-select bg-bg" id="sortBySelect">
                        <form:option value=""><spring:message code="search.sort.placeholder"/></form:option>
                        <form:option value="name"><spring:message code="search.sort.name"/></form:option>
                        <form:option value="score"><spring:message code="search.sort.score"/></form:option>
                    </form:select>
                </div>
            </div>

        </div>

        <button type="submit" class="btn button-primary w-100 "><spring:message code="search.button"/></button>
    </form:form>
</div>

<!-- LIST OF NOTES MATCHING -->

<div class="container">
    <div class="table-responsive">
        <table class="table table-hover ">
            <thead>
            <tr>
                <th>Name of Note</th>
                <th>Owner</th>
                <th>Created At</th>
                <th>Score</th>
                <!-- TODO: ADD SIZE OF FILE -->
            </tr>
            </thead>
            <tbody>
            <c:forEach var="note" items="${notes}">
                <c:set var="date" value="${note.createdAt}"/>
                <tr>
                    <td>${note.name}</td>
                    <td>owner</td>
                    <td><spring:message code="date.format"
                                        arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/></td>
                    <td>${note.avgScore}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div class="container mt-4">
    <div class="row">
        <c:forEach items="${notes}" var="note">
            <div class="col-md-4 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${note.name}</h5>
                        <p class="card-text">${note.category.formattedName}</p>

                        <p class="card-text"><spring:message code="date.format" arguments="${date.year},${date.monthValue
                            },${date.dayOfMonth}"/></p>
                        <p class="card-text">${note.avgScore}</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ascdesc.js"/>"></script>

</body>

</html>