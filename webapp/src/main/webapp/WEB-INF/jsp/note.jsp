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


<h1><c:out value="${note.name}"/></h1>
<p><c:out value="${note.avgScore}"/></p>
<form:form action="/notes/${noteId}/review" method="post" modelAttribute="reviewForm">
    <form:input path="email"/>
    <form:select path="score">
        <form:option value="1">1</form:option>
        <form:option value="2">2</form:option>
        <form:option value="3">3</form:option>
        <form:option value="4">4</form:option>
        <form:option value="5">5</form:option>
    </form:select>
    <input type="submit"/>
</form:form>

<%--<object--%>
<%--        type="application/pdf"--%>
<%--        data="http://localhost:8080/notes/${noteId}/download"--%>
<%--        width="100%"--%>
<%--        height="80%">--%>
<%--</object>--%>


<div class="container">
    <ul class="nav nav-tabs d-flex justify-content-between align-items-center text-dark p-3">
        <li class="nav-item">
            <a
                    href="#"
                    class="p-1 border rounded-circle"
                    id="prev_page"
                    title="previous page"
                    data-bs-toggle="tooltip"
                    data-bs-placement="bottom"
            >
                <img src="<c:url value="/svg/angle-small-left.svg"/>">
            </a>

            <a
                    href="#"
                    class="p-1 border rounded-circle"
                    id="next_page"
                    data-bs-toggle="tooltip"
                    data-bs-placement="bottom"
                    title="next page"
            >
                <img src="<c:url value="/svg/angle-small-right.svg"/>">
            </a>

            <!-- page 1 of 5 -->
            Page
            <span id="page_num"></span>
            of
            <span id="page_count"></span>
        </li>

        <li class="nav-item">
            <button
                    class="rounded-circle p-2 border-0 btn btn-primary"
                    id="zoom_in"
                    data-bs-toggle="tooltip"
                    data-bs-placement="bottom"
                    title="zoom in"
            >
                <i class="bi bi-zoom-in"></i>
            </button>

            <button
                    class="rounded-circle p-2 border-0 btn btn-primary"
                    id="zoom_out"
                    data-bs-toggle="tooltip"
                    data-bs-placement="bottom"
                    title="zoom out"
            >
                <i class="bi bi-zoom-out"></i>
            </button>
        </li>
    </ul>
    <canvas
            id="canvas"
            class="d-flex flex-column justify-content-center align-items-center mx-auto"
    ></canvas>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script src="https://cdn.jsdelivr.net/npm/pdfjs-dist@2.10.377/build/pdf.min.js"></script>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/pdfviewer.js"/>"></script>

</body>
</html>
