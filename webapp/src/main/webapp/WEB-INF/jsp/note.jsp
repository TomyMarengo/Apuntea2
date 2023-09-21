<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl" />


<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title> Apuntea | <c:out value="${note.name}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/navbar.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/notes/reviews-comments.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar/>

<fragment:bottom-navbar title="./${noteId}:${note.name}" extraLinks=""/>

<div class="container-fluid">
    <div class="row">
        <section class="col-8 h-100-navs">
            <iframe class=" w-100" style="height: 90%" src="${baseUrl}/notes/${noteId}/download"></iframe>

            <div class="d-flex justify-content-between">
                <h1 class="mb-0 !imp">${note.name}</h1>
                <div>
                    <button class="btn button-expansion rounded-circle edit-button" id="<c:out value="${note.noteId}"/>e1">
                        <img src="<c:url value="/svg/pencil.svg"/>" alt="${edit}" class="icon-xs fill-text">
                    </button>

                    <a href="./${note.noteId}/download" download="${note.name}">
                        <button class="btn button-expansion rounded-circle">
                            <img src="<c:url value="/svg/download.svg"/>" alt="${download}" class="icon-xs fill-text">
                        </button>
                    </a>

                </div>
            </div>
            <span>
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
            <div class="mt-2 mb-2">
                <img src="<c:url value="/image/teacher.png"/>" alt="${logotype}"
                     style="width: 40px; height: 40px; margin-right: 5px">
                <span><strong>Owner <!--${note.user}--></strong></span>
            </div>
            <span>
                <c:set var="date" value="${note.createdAt}"/>
                <spring:message code="date.format" arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/>
            </span>
        </section>

        <section class="col-4">
            <div class="h-100 d-flex flex-column">

                <div class="container-fluid pb-3">
                    <input type="submit" class="btn reviews-comments-button mb-3" value="<spring:message code="notes.reviews.button"/>"/>


                    <c:if test="${not empty reviews}">
                        <span><spring:message code="score"/>: <fmt:formatNumber type="number" maxFractionDigits="1" value="${note.avgScore}"/> ⭐</span>

                        <div class="reviews-comments">
                            <c:forEach items="${reviews}" var="review">
                                <div class="card box review-card mb-3 p-3" >

                                    <div class="d-flex flex-wrap justify-content-between">
                                        <h4 class="card-title overflow-hidden">
                                            <c:out value="${review.user.email}"/>
                                        </h4>
                                        <span class="card-header-pills">
                                    <c:forEach begin="1" end="${review.score}">⭐</c:forEach>
                                        </span>
                                    </div>

                                    <span class="card-text reviews-comment">
                                <c:out value="${review.content}"/>
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>

                    <c:if test="${empty reviews}">
                        <p class="mb-2"><spring:message code="notes.reviews.noReviews"/></p>
                    </c:if>

                    <div class="card box p-3">
                        <form:form action="./${note.noteId}/review" method="post" modelAttribute="reviewForm">
                            <div>
                                <spring:message code="notes.review.text.placeholder" var="placeholderText" />
                                <form:textarea path="content" class="form-control" placeholder='${placeholderText}'/>
                            </div>
                            <form:errors path="content" cssClass="text-danger" element="p"/>

                            <div class="d-flex justify-content-between mt-3">
                                <div class="input-group w-75">
                                    <form:select path="score" class="form-select bg-bg" id="scoreSelect">
                                        <form:option value="5">⭐⭐⭐⭐⭐</form:option>
                                        <form:option value="4">⭐⭐⭐⭐</form:option>
                                        <form:option value="3">⭐⭐⭐</form:option>
                                        <form:option value="2">⭐⭐</form:option>
                                        <form:option value="1">⭐</form:option>
                                    </form:select>
                                </div>
                                <input type="submit" class="btn rounded-box button-primary " value="<spring:message code="notes.send.button"/>"/>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>


        </section>
    </div>
</div>




<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/buttons.js"/>"></script>

</body>
</html>
