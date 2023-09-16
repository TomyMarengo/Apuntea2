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
    <title> Apuntea | ${note.name}</title>
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
    <link rel="stylesheet" href="<c:url value="/css/sections/notes/iframe.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/notes/reviews-comments.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar/>

<fragment:bottom-navbar title="./search:Búsqueda" extraLinks=""/>
<div class="container-fluid">
    <div class="row row-cols-1 row-cols-md-2">
        <section class="col-md-8">
            <iframe
                    class="iframe-note"
                    src="http://${pageContext.request.serverName}:${pageContext.request.serverPort}/notes/${noteId}/download"
            >
            </iframe>
        </section>
        <section class="col-md-4">
            <div class="container-fluid">
<%--                <div class="d-flex justify-content-around ">--%>
    <%--                    <!-- <c:if test="${reviews != null}"> -->--%>
                    <input type="submit" class="btn reviews-comments-button reviews section-active" value="<spring:message code="notes.reviews.button"/>"/>
<%--                    <input type="submit" class="btn reviews-comments-button comments" value="<spring:message code="notes.comments.button"/>"/>--%>

<%--                    <!-- <c:if test="${comments != null}">--%>
<%--                        <input type="submit" class="btn reviews-comments-button" value="<spring:message code="notes.reviews.button"/>"/>--%>
<%--                        <input type="submit" class="btn reviews-comments-button active" value="<spring:message code="notes.comments.button"/>"/>--%>
<%--                </div>--%>
                <div class="reviews-comments">
                    <div class="card mt-3" style="height: 100px; width: 100%">

                    </div>
                    <div class="card mt-3" style="height: 100px; width: 100%">

                    </div>
                    <div class="card mt-3" style="height: 100px; width: 100%">

                    </div>
                </div>
                <div class="card enter-review-comment mt-3 p-3">
                    <form:form action="/notes/${noteId}/review" method="post" modelAttribute="reviewForm">
                        <div class="input-group mb-3">
                            <span class="input-group-text input-group-icon" id="basic-addon1">@</span>
                            <spring:message code="notes.review.email.placeholder" var="placeholderEmail" />
                            <form:input path="email" type="text" id="email" class="form-control" placeholder='${placeholderEmail}'/>
                        </div>
                        <form:errors path="email" cssClass="text-danger" element="p"/>

                        <div class="my-3">
                            <spring:message code="notes.review.text.placeholder" var="placeholderText" />
                            <form:textarea path="text" class="form-control" placeholder='${placeholderText}'/>
                        </div>
                        <form:errors path="text" cssClass="text-danger" element="p"/>

                        <div class="d-flex justify-content-between my-3">
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

            <!-- BOTTOM-NAVBAR -->
<%--            <h1><c:out value="${note.name}"/></h1>--%>
<%--            <h2><spring:message code="notes.currentScore"/><c:out value="${note.avgScore}"/></h2>--%>

        </section>

    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="<c:url value="/js/darkmode.js"/>"></script>

</body>
</html>
