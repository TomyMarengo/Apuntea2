<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>


<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title> Apuntea | <c:out value="${note.name}"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

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
<fragment:navbar loggedin="${user != null}"/>

<fragment:bottom-navbar title="./${noteId}:${note.name}" hierarchy="${hierarchy}"/>

<div class="container-fluid">
    <div class="row">
        <section class="col-8 h-100-navs">

            <c:url var="noteFileUrl" value="${baseUrl}/notes/${noteId}/download"/>
            <c:choose>

                    <c:when test ="${note.fileType eq 'jpeg' or note.fileType eq 'jpg' or note.fileType eq 'png'}">
                        <img class="w-100" src="${noteFileUrl}" alt="<c:out value="${note.name}"/>"/>
                    </c:when>

                    <c:when test = "${note.fileType eq 'mp3'}">
                        <audio controls class="w-100">
                            <source src="${noteFileUrl}" type="audio/mp3">
                        </audio>
                    </c:when>


                <c:otherwise>
                    <iframe class="w-100" style="height: 90%" src="${noteFileUrl}"></iframe>
                </c:otherwise>
            </c:choose>

            <div class="d-flex justify-content-between">
                <h1 class="overflow-hidden">${note.name}</h1>
                <div class="d-flex">
                    <c:if test="${user ne null and note.user.userId eq user.userId}">
                        <span data-bs-toggle="tooltip" data-bs-placement="bottom"
                              data-bs-title="<spring:message code="edit"/>" data-bs-trigger="hover">
                            <button id="editNoteModalButton" class="btn nav-icon-button" data-bs-toggle="modal"
                                    data-bs-target="#editNoteModal">
                                <img src="<c:url value="/svg/pencil.svg"/>" alt="<spring:message code="edit"/>"
                                     class="icon-s fill-text">
                            </button>
                        </span>
                    </c:if>
                    <a href="./${note.id}/download" download="${note.name}">
                        <button type="button" class="btn button-expansion rounded-circle" data-bs-toggle="tooltip"
                                data-bs-placement="bottom" data-bs-title="<spring:message code="download"/>"
                                data-bs-trigger="hover">
                            <img src="<c:url value="/svg/download.svg"/>" alt="<spring:message code="download"/>"
                                 class="icon-s fill-text">
                        </button>
                    </a>

                    <c:if test="${user ne null and note.user.userId eq user.userId}">
                    <span data-bs-toggle="tooltip" data-bs-placement="bottom"
                          data-bs-title="<spring:message code="delete"/>" data-bs-trigger="hover">
                        <button id="openDeleteNoteModalButton" class="btn nav-icon-button" data-bs-toggle="modal"
                                data-bs-target="#deleteOneModal">
                            <img src="<c:url value="/svg/trash.svg"/>"
                                 alt="<spring:message code="delete"/>"
                                 class="icon-s fill-text">
                        </button>
                    </span>
                    </c:if>
                </div>
            </div>

            <div class="mt-2 mb-2">
                <img src="<c:url value="/image/teacher.png"/>" alt="<spring:message code="logotype"/>"
                     style="width: 40px; height: 40px; margin-right: 5px">
                <span><strong><c:out value="${note.user.email}"/></strong></span>
            </div>
            <span>
                <c:set var="date" value="${note.createdAt}"/>
                <spring:message code="date.format" arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/>
            </span>
        </section>

        <section class="col-4">
            <div class="h-100 d-flex flex-column">

                <div class="container-fluid pb-3">
                    <input type="submit" class="btn reviews-comments-button mb-3"
                           value="<spring:message code="notes.reviews.button"/>"/>


                    <c:if test="${not empty reviews}">
                        <span><spring:message code="score"/>: <fmt:formatNumber type="number" maxFractionDigits="1"
                                                                                value="${note.avgScore}"/> ⭐</span>

                        <div class="reviews-comments">
                            <c:forEach items="${reviews}" var="review">
                                <div class="card box review-card mb-3 p-3">

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
                        <form:form action="./${note.id}/review" method="post" modelAttribute="reviewForm">
                            <div>
                                <spring:message code="notes.review.text.placeholder" var="placeholderText"/>
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
                                <input type="submit" class="btn rounded-box button-primary "
                                       value="<spring:message code="notes.send.button"/>"/>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- DELETE MODAL -->
<c:url var="deleteUrl" value="./${noteId}/delete"/>

<div class="modal fade" id="deleteOneModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="deleteLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="deleteLabel"><spring:message code="delete"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close">
                </button>
            </div>
            <form:form id="deleteForm" method="POST" action="${deleteUrl}">
                <div class="modal-body pb-0">
                    <spring:message code="DeleteForm.description"/>

                    <!-- DYNAMICALLY ADDED INPUTS -->
                </div>

                <div class="modal-footer mt-4">
                    <button type="button" class="btn rounded-box button-primary"
                            data-bs-dismiss="modal">
                        <spring:message code="close"/></button>
                    <input id="deleteOneButton" type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="delete"/>"/>
                </div>

            </form:form>
        </div>
    </div>
</div>

<!-- EDIT NOTE MODAL -->
<c:if test="${user eq null or note.user.userId eq user.userId}">
    <c:url var="editUrl" value="./${noteId}"/>

    <div class="modal fade" id="editNoteModal" data-bs-backdrop="static" data-bs-keyboard="false"
         tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content box bg-bg">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="editLabel"><spring:message
                            code="editNote"/></h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <!-- EDIT NOTE FORM -->
                <form:form modelAttribute="editNoteForm"
                           action="${editUrl}"
                           method="post"
                           enctype="multipart/form-data"
                           autocomplete="off"
                           class="d-flex flex-column"
                           id="editNoteForm">
                    <div class="modal-body pb-0">

                        <div class="d-flex flex-column gap-2">
                            <div class="input-group">
                                <label class="input-group-text" for="name"><spring:message
                                        code="name"/></label>
                                <form:input path="name" type="text"
                                            aria-label="<spring:message code=\"form.upload.name\"/>"
                                            class="form-control" id="name"/>
                            </div>
                            <form:errors path="name" cssClass="text-danger" element="p"/>
                            <form:errors cssClass="text-danger" element="p"/> <!-- Global errors -->
                        </div>

                        <div class="d-flex flex-column gap-2 mt-4">
                            <div class="input-group">
                                <label class="input-group-text" for="categorySelect"><spring:message
                                        code="category"/></label>
                                <form:select path="category" class="form-select" id="categorySelect">
                                    <form:option value="theory"><spring:message code="category.theory"/></form:option>
                                    <form:option value="practice"><spring:message code="category.practice"/></form:option>
                                    <form:option value="exam"><spring:message code="category.exam"/></form:option>
                                    <form:option value="other"><spring:message code="category.other"/></form:option>
                                </form:select>
                            </div>
                            <form:errors path="category" cssClass="text-danger" element="p"/>
                        </div>

                        <div class="d-flex flex-column gap-2 mt-4">
                            <div class="input-group">
                                <label class="input-group-text" for="visible"><spring:message
                                        code="privacy"/></label>
                                <form:select path="visible" class="form-select" id="visible">
                                    <form:option value="true"><spring:message
                                            code="public"/></form:option>
                                    <form:option value="false"><spring:message
                                            code="private"/></form:option>
                                </form:select>
                            </div>
                            <form:errors path="visible" cssClass="text-danger" element="p"/>
                        </div>

                    </div>
                    <div class="modal-footer mt-4">
                        <button type="button" class="btn rounded-box button-primary"
                                data-bs-dismiss="modal">
                            <spring:message code="close"/></button>
                        <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                                code="update"/>"/>
                    </div>
                    <input type="hidden" name="id" value="${note.id}"/>
                    <input type="hidden" name="parentId" value="${note.parentId}"/>
                    <input type="hidden" name="redirectUrl" value="/notes/${noteId}"/>
                </form:form>
            </div>
        </div>
    </div>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous">
</script>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/search-buttons.js"/>"></script>
<script src="<c:url value="/js/crud-buttons.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/global-search.js"/>"></script>

<c:if test="${user eq null or note.user.userId eq user.userId}">
    <c:if test="${errorsEditNoteForm != null}">
        <script>
            const editNoteModalButton = document.getElementById('editNoteModalButton');
            editNoteModalButton.click()
        </script>
    </c:if>
    <c:if test="${errorsEditNoteForm == null}">
        <script>
            editNoteForm.querySelectorAll('#name')[0].value = "<c:out value="${note.name}"/>";
            editNoteForm.querySelectorAll('#categorySelect')[0].value =  "<c:out value="${note.category}"/>";
            editNoteForm.querySelectorAll('#visible')[0].value =  "<c:out value="${note.visible}"/>";
        </script>
    </c:if>
</c:if>

</body>
</html>
