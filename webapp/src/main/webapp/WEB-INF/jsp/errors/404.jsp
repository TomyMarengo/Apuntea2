<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="utf-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title> Apuntea | <spring:message code="errors.404.title"/></title>
  <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

  <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
  <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>

<body>

<fragment:navbar user="${user}"/>

<main>
  <div class="container h-100">
    <div class="row h-100 justify-content-center align-items-center">
      <div class="col-md-6">
        <div class="text-center">
          <img src="${baseUrl}/image/page_not_found.png" alt="<spring:message code="errors.404.title"/>" class="img-fluid mt-5 mb-4">
          <h1 class="text-primary"><spring:message code="errors.404.title"/></h1>
          <p class="text-primary"><spring:message code="errors.404.message"/></p>
        </div>
      </div>
    </div>
  </div>
</main>

<script src="<c:url value="/js/popups.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

</body>

</html>
