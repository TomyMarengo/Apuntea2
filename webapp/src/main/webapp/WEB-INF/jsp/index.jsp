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
    <title>Apuntea</title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-4bw+/aepP/YC94hEpVNVgiZdgIC5+VKNBQNGCHeKRQN+PtmoHDEXuppvnDJzQIu9" crossorigin="anonymous">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/autocomplete.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/navbar.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/graph.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/carousel.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/landing/comments.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<!-- NAVBAR -->
<fragment:navbar/>

<!-- "¿SABÍAS QUÉ?" SECTION -->
<div class="container mt-5 mw-500">
    <div class="card bg-transparent border-0">
        <div class="card-body">
            <h5 class="card-title fw-bold"><spring:message code="index.dyk.title"/></h5>
            <div class="d-flex align-items-center mb-3">
                <img src="image/teacher.png" alt="Teacher" class="me-2" style="width: 40px; height: 40px;">
                <p class="mb-0"><spring:message code="index.dyk.subtitle"/></p>
            </div>
            <c:url var="searchUrl" value="./search"/>
            <form:form modelAttribute="searchNotesForm"
                       action="${searchUrl}"
                       method="get"
                       class="d-flex align-items-center mb-3"
                       id="searchWordForm">
                <spring:message code="index.search.placeholder" var="placeholderSearch" />
                <form:input path="word" type="text" class="form-control custom-input me-2 rounded-box bg-bg"
                            placeholder='${placeholderSearch}'/>
                <input type="submit" class="btn rounded-box button-white" value="<spring:message code="index.search.button"/>"/>
                <form:errors path="word" cssClass="text-danger" element="p"/>
            </form:form>
        </div>
    </div>
</div>

<!-- "EXPLORA NUESTRAS OPCIONES" SECTION -->
<div class="container mw-500">
    <div class="card bg-transparent border-0">
        <div class="card-body d-flex flex-column align-items-center">
            <div class="d-flex align-items-center mb-3">
                <img src="<c:url value="/svg/rocket.svg"/>" alt="Rocket" class="me-2 icon-s fill-text">
                <h5 class="fw-bold"><spring:message code="index.explore.title"/></h5>
            </div>
            <div class="d-flex w-75 justify-content-around">
                <!-- TODO: Change to index.explore.register again -->
                <!-- UPLOAD BUTTON -->
                <button class="btn rounded-box button-primary" data-bs-toggle="modal" data-bs-target="#uploadModal"
                        id="uploadModalButton">
                    <spring:message code="index.explore.upload"/></button>

                <!-- DISCOVER BUTTON -->
                <a href="./search">
                    <button class="btn rounded-box button-secondary">
                        <spring:message code="index.explore.discover"/></button>
                </a>

                <div class="modal fade" id="uploadModal" data-bs-backdrop="static" data-bs-keyboard="false"
                     tabindex="-1" aria-labelledby="uploadLabel" aria-hidden="true">
                    <div class="modal-dialog modal-dialog-centered">
                        <div class="modal-content box bg-bg">
                            <div class="modal-header">
                                <h1 class="modal-title fs-5" id="uploadLabel"><spring:message
                                        code="form.upload.title"/></h1>
                                <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <!-- CREATE NOTE FORM -->
                                <c:url var="createUrl" value="./notes/create"/>
                                <form:form modelAttribute="createNoteForm"
                                           action="${createUrl}"
                                           method="post"
                                           enctype="multipart/form-data"
                                           autocomplete="off"
                                           class="d-flex flex-column gap-4"
                                           id="createNoteForm">

                                    <div class="d-flex flex-column gap-2">
                                        <div class="input-group">
                                            <label class="input-group-text" for="file"><spring:message
                                                    code="form.upload.file"/></label>
                                            <form:input path="file" type="file" class="form-control" id="file"/>
                                        </div>
                                        <form:errors path="file" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <div class="input-group">
                                            <label class="input-group-text" for="name"><spring:message
                                                    code="form.upload.name"/></label>
                                            <form:input path="name" type="text"
                                                        aria-label="<spring:message code=\"form.upload.name\"/>"
                                                        class="form-control" id="name"/>
                                        </div>
                                        <form:errors path="name" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <div class="input-group">
                                            <label class="input-group-text" for="email"><spring:message
                                                    code="form.upload.email"/></label>
                                            <form:input path="email" type="text"
                                                        aria-label="<spring:message code=\"form.upload.email\"/>"
                                                        class="form-control" id="email"/>
                                        </div>
                                        <form:errors path="email" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <select id="institutionSelect" style="display: none;">
                                            <option disabled selected value></option>
                                            <c:forEach items="${institutions}" var="inst">
                                                <option value="<c:out value="${inst.institutionId}"/>"><c:out value="${inst.name}"/></option>
                                            </c:forEach>
                                        </select>

                                        <form:input path="institutionId" id="institutionId" style="display: none;"/>

                                        <div class="input-group">
                                            <label class="input-group-text" for="institutionAutocomplete"><spring:message
                                                    code="form.upload.institution"/></label>
                                            <div class="autocomplete">
                                                <input type="text" id="institutionAutocomplete" class="form-control"/>
                                            </div>
                                        </div>
                                        <form:errors path="institutionId" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <select id="careerSelect" style="display: none;">
                                            <option disabled selected value></option>
                                            <c:forEach items="${careers}" var="career">
                                                <option value="<c:out value="${career.careerId}"/>"><c:out value="${career.name}"/></option>
                                            </c:forEach>
                                        </select>

                                        <form:input path="careerId" id="careerId" style="display: none;"/>

                                        <div class="input-group">
                                            <label class="input-group-text" for="careerAutocomplete"><spring:message
                                                    code="form.upload.career"/></label>
                                            <div class="autocomplete">
                                                <input type="text" id="careerAutocomplete" class="form-control"/>
                                            </div>
                                        </div>
                                        <form:errors path="careerId" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <select id="subjectSelect" style="display: none;">
                                            <option disabled selected value></option>
                                            <c:forEach items="${subjects}" var="subject">
                                                <option value="<c:out value="${subject.subjectId}"/>"><c:out value="${subject.name}"/></option>
                                            </c:forEach>
                                        </select>

                                        <form:input path="subjectId" id="subjectId" style="display: none;"/>

                                        <div class="input-group">
                                            <label class="input-group-text" for="subjectAutocomplete"><spring:message
                                                    code="form.upload.subject"/></label>
                                            <div class="autocomplete">
                                                <input type="text" id="subjectAutocomplete" class="form-control"/>
                                            </div>
                                        </div>
                                        <form:errors path="subjectId" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="d-flex flex-column gap-2">
                                        <div class="input-group">
                                            <label class="input-group-text" for="categorySelect"><spring:message
                                                    code="form.upload.category"/></label>
                                            <form:select path="category" class="form-select" id="categorySelect">
                                                <form:option
                                                        value="theory"><spring:message
                                                        code="form.upload.category.theory"/></form:option>
                                                <form:option
                                                        value="practice"><spring:message
                                                        code="form.upload.category.practice"/></form:option>
                                                <form:option
                                                        value="exam"><spring:message
                                                        code="form.upload.category.exam"/></form:option>
                                                <form:option
                                                        value="other"><spring:message
                                                        code="form.upload.category.other"/></form:option>
                                            </form:select>
                                        </div>
                                        <form:errors path="category" cssClass="text-danger" element="p"/>
                                    </div>

                                    <div class="modal-footer">
                                        <button type="button" class="btn rounded-box button-primary"
                                                data-bs-dismiss="modal">
                                            <spring:message code="form.upload.button.close"/></button>
                                        <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="form.upload.button.upload"/>"/>
                                    </div>
                                </form:form>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>

<!-- TODO: UNCOMMENT
<div class="row mt-5 m-0 ">
    # CARROUSEL LAST NOTES
    <div class="d-flex flex-column col-xl-6">
        <div class="d-flex justify-content-center" style="padding-left: 40px;">
            <h5 class="fw-bold" style="margin-left: 120px; margin-right: 75px"><spring:message
                    code="index.notes.title"/></h5>
            <div>
                <button class="carousel-button" type="button" data-bs-target="#carouselUniversity" data-bs-slide="prev">
                    <img src="<c:url value="/svg/arrow-left.svg"/>" alt="Previous" class="icon-l fill-dark-primary">
                </button>
                <button class="carousel-button" type="button" data-bs-target="#carouselUniversity" data-bs-slide="next">
                    <img src="<c:url value="/svg/arrow-right.svg"/>" alt="Next" class="icon-l fill-dark-primary">
                </button>
            </div>

        </div>

        <div id="carouselUniversity" class="carousel slide mt-3">
            <div class="carousel-inner">
                # First "page" of carrousel
                <div class="carousel-item mb-5 active">
                    # University name and graph
                    <div class="container mw-600">
                        <div class="card box h-100 bg-bg">
                            <div class="card-body d-flex align-items-center h-100 justify-content-around flex-wrap">
                                # University Icon and Name (in the right)
                                <div class="d-flex align-items-center university-title ">
                                    <img src="<c:url value="/svg/graduation-cap.svg"/>" alt="University Icon"
                                         class="icon-s fill-text">
                                    <h5 class=" mx-2 w-100"><strong>PLACEHOLDER</strong></h5>
                                </div>
                                # Graph with circles and lines
                                <div class="graph h-100">
                                    <div class="circle circle1"></div>
                                    <div class="line line1"></div>
                                    <div class="circle circle2"></div>
                                    <div class="line line2"></div>
                                    <div class="circle circle3"></div>
                                    <div class="line line3"></div>
                                    <div class="circle circle4"></div>
                                    <div class="line line4"></div>
                                    <div class="circle circle5"></div>
                                    <div class="info1 d-flex flex-column fw-bold text-center w-25">
                                        <p>NUMBER</p>
                                        <p><spring:message code="index.notes.students"/></p>
                                    </div>
                                    <div class="info2 d-flex flex-column fw-bold text-center w-25">
                                        <p>NUMBER</p>
                                        <p><spring:message code="index.notes.notes"/></p>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>
                    # Example notes
                    <div class="container mw-600">
                        <div class="card box h-100 py-1 bg-dark-bg">
                            <div class="card-body d-flex flex-column gap-2">
                                <a href="https://www.google.com" class="bar-link">
                                    <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex align-items-center text-s">
                                        <span class="col-4 text-start">PLACEHOLDER</span>
                                        <span class="col-6">PLACEHOLDER</span>
                                        <span class="col-2 text-end">PLACEHOLDER</span>
                                    </button>
                                </a>
                                <a href="https://www.google.com" class="bar-link">
                                    <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                        <span class="col-4 text-start">PLACEHOLDER</span>
                                        <span class="col-6">PLACEHOLDER</span>
                                        <span class="col-2 text-end">PLACEHOLDER</span>
                                    </button>
                                </a>
                                <a href="https://www.google.com" class="bar-link">
                                    <button class="btn rounded-pill button-white button-expansion
                          w-100 d-flex justify-content-between align-items-center text-s">
                                        <span class="col-4 text-start">PLACEHOLDER</span>
                                        <span class="col-6">PLACEHOLDER</span>
                                        <span class="col-2 text-end">PLACEHOLDER</span>
                                    </button>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    # LIST LAST COMMENTS
    <div id="last-comments" class="d-flex flex-column col-xl-6">
        <h5 class="text-center fw-bold"><spring:message code="index.comments.title"/></h5>
        <div class="container mt-3">
            <div class="row justify-content-center">
                # A COMMENT
                <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                    <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                        <div class="card-body">
                            <h5 class="card-title fb-">
                                <img src="<c:url value="/svg/arrow-trend-up.svg"/>" alt="University Icon"
                                     class="icon-s fill-dark-primary mx-2">
                                <strong>Matemática Discreta</strong>
                            </h5>
                            <p class="card-text"><strong>Jonathan ha comentado</strong></p>
                            <p class="card-text mt-2">Buen apunte, pero el de Apuntes Britu es mejor.</p>
                        </div>
                    </a>
                </div>
                # A COMMENT
                <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                    <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                        <div class="card-body">
                            <h5 class="card-title fb-">
                                <img src="<c:url value="/svg/arrow-trend-up.svg"/>" alt="University Icon"
                                     class="icon-s fill-dark-primary mx-2">
                                <strong>Teoría de Lenguajes y Autómatas</strong>
                            </h5>
                            <p class="card-text"><strong>David ha comentado</strong></p>
                            <p class="card-text mt-2">El de Apuntes Abru le pasa el trapo, aunque pesa 100MB más...</p>
                        </div>
                    </a>
                </div>
                # A COMMENT
                <div class="col-lg-12 col-xl-6 mb-4 mw-500">
                    <a href="https://www.google.com" class="card box h-100 text-decoration-none button-shadow">
                        <div class="card-body">
                            <h5 class="card-title fb-">
                                <img src="<c:url value="/svg/arrow-trend-up.svg"/>" alt="University Icon"
                                     class="icon-s fill-dark-primary mx-2">
                                <strong>Matemática III</strong>
                            </h5>
                            <p class="card-text"><strong>Tomás ha comentado</strong></p>
                            <p class="card-text mt-2">Muy bueno!</p>
                        </div>
                    </a>
                </div>

            </div>
        </div>
    </div>
</div> -->


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>

<c:if test="${errors != null}">
    <script>
      const uploadModalButton = document.getElementById('uploadModalButton');
      uploadModalButton.click()
    </script>
</c:if>
</body>

</html>