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
    <title> Apuntea | <c:out value="${directory.name}"/></title>
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

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<fragment:navbar/>

<fragment:bottom-navbar title="./${directoryId}:${directory.name}" extraLinks=""/>

<c:url var="createUrl" value="./${directoryId}"/>

<button class="btn rounded-box button-primary" data-bs-toggle="modal" data-bs-target="#createNoteModal"
        id="createNoteModalButton">
    <spring:message code="index.explore.upload"/></button>


<!-- CREATE NOTE MODAL -->
<div class="modal fade" id="createNoteModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="uploadLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">
            <div class="modal-header">
                <h1 class="modal-title fs-5" id="uploadLabel"><spring:message
                        code="form.upload.title"/></h1>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close">
                </button>
            </div>
            <div class="modal-body">

                <!-- CREATE NOTE FORM -->
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

                    <%--                                    <div class="d-flex flex-column gap-2">--%>
                    <%--                                        <select id="institutionSelect" style="display: none;">--%>
                    <%--                                            <option disabled selected value></option>--%>
                    <%--                                            <c:forEach items="${institutions}" var="inst">--%>
                    <%--                                                <option value="<c:out value="${inst.institutionId}"/>"><c:out value="${inst.name}"/></option>--%>
                    <%--                                            </c:forEach>--%>
                    <%--                                        </select>--%>

                    <%--                                        <form:input path="institutionId" id="institutionId" style="display: none;"/>--%>

                    <%--                                        <div class="input-group">--%>
                    <%--                                            <label class="input-group-text" for="institutionAutocomplete"><spring:message--%>
                    <%--                                                    code="form.upload.institution"/></label>--%>
                    <%--                                            <div class="autocomplete">--%>
                    <%--                                                <input type="text" id="institutionAutocomplete" class="form-control"/>--%>
                    <%--                                            </div>--%>
                    <%--                                        </div>--%>
                    <%--                                        <form:errors path="institutionId" cssClass="text-danger" element="p"/>--%>
                    <%--                                    </div>--%>

                    <%--                                    <div class="d-flex flex-column gap-2">--%>
                    <%--                                        <select id="careerSelect" style="display: none;">--%>
                    <%--                                            <option disabled selected value></option>--%>
                    <%--                                            <c:forEach items="${careers}" var="career">--%>
                    <%--                                                <option value="<c:out value="${career.careerId}"/>"><c:out value="${career.name}"/></option>--%>
                    <%--                                            </c:forEach>--%>
                    <%--                                        </select>--%>

                    <%--                                        <form:input path="careerId" id="careerId" style="display: none;"/>--%>

                    <%--                                        <div class="input-group">--%>
                    <%--                                            <label class="input-group-text" for="careerAutocomplete"><spring:message--%>
                    <%--                                                    code="form.upload.career"/></label>--%>
                    <%--                                            <div class="autocomplete">--%>
                    <%--                                                <input type="text" id="careerAutocomplete" class="form-control"/>--%>
                    <%--                                            </div>--%>
                    <%--                                        </div>--%>
                    <%--                                        <form:errors path="careerId" cssClass="text-danger" element="p"/>--%>
                    <%--                                    </div>--%>
                    <%--                                    --%>

                    <div class="d-flex flex-column gap-2">
                        <select id="subjectSelect" style="display: none;">
                            <option disabled selected value></option>
                            <c:forEach items="${subjects}" var="subject">
                                <option value="<c:out value="${subject.subjectId}"/>"><c:out
                                        value="${subject.name}"/></option>
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

                    <form:input type="hidden" path="parentId" name="parentId" value="${directoryId}" />

                    <input type="hidden" name="createNote" value="createNote" /> <!-- createNote -->

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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>
<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>

<c:if test="${errorsNoteForm != null}">
    <script>
        const createNoteModalButton = document.getElementById('createNoteModalButton');
        createNoteModalButton.click()
    </script>
</c:if>
</body>

</html>
