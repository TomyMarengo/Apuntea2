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
  <title>Apuntea | <spring:message code="404.title"/></title>
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

<div class="container">
  <h1 class="text-center">
    <spring:message code="404.title"/>
  </h1>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>
</body>

</html>