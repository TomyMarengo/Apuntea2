<%--suppress HtmlFormInputWithoutLabel --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark" data-search-view="horizontal">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="manageCareer.title"/></title>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/teacher.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/autocomplete.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/color-picker.css"/>"/>
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
<fragment:navbar loggedin="${user != null}"/>

<!-- BOTTOM-NAVBAR -->
<spring:message code="manageCareer.title" var="title"/>
<fragment:bottom-navbar title="/careers:${title}">
</fragment:bottom-navbar>


<!-- CAREER SELECT -->
<div class="container my-5">
    <div class="row row-cols-2 row-cols-md-3">
        <div class="col">
            <select id="institutionSelect" style="display: none;">
                <option disabled selected value></option>
            </select>

            <input id="institutionId" style="display: none;"/>

            <div class="input-group mb-3">
                <div class="autocomplete">
                    <spring:message code="search.institution.placeholder" var="placeholderInstitution"/>
                    <input type="text" id="institutionAutocomplete" class="form-control bg-bg special-radius"
                           placeholder="${placeholderInstitution}" autocomplete="off"/>
                </div>
                <span class="input-group-text input-group-icon clickable" id="eraseInstitutionButton">
                    <img src="<c:url value="/svg/cross.svg"/>"
                         alt="<spring:message code="search.sort.image"/>"
                         class="icon-xs fill-dark-primary"/>
                </span>
            </div>
        </div>

        <div class="col">
            <select id="careerSelect" style="display: none;">
                <option disabled selected value></option>
            </select>

            <input id="careerId" style="display: none;" />

            <div class="input-group mb-3">
                <div class="autocomplete">
                    <spring:message code="search.career.placeholder" var="placeholderCareer"/>
                    <input type="text" id="careerAutocomplete" class="form-control bg-bg special-radius"
                           placeholder="${placeholderCareer}" autocomplete="off"/>
                </div>
                <span class="input-group-text input-group-icon clickable" id="eraseCareerButton">
                    <img src="<c:url value="/svg/cross.svg"/>"
                         alt="<spring:message code="search.sort.image"/>"
                         class="icon-xs fill-dark-primary"/>
                </span>
            </div>
        </div>
    </div>
</div>

<c:if test="${career ne null}">
    <!-- DEFINES -->

    <!-- HORIZONTAL LIST -->
    <section class="container mt-4 p-0">
        <div class="table-responsive">
            <table class="table table-hover table-search">
                <thead>
                <tr>
                    <th class="col-md-5"><spring:message code="name"/></th>
                    <th class="col-md-2"><spring:message code="year"/></th>
                    <th class="col-md-2"></th> <!-- ACTIONS -->
                </tr>
                </thead>
                <tbody id="subjectTable">
                <c:forEach var="item" items="${ownedSubjects}">
                    <tr class="note-found no-select"
                        id="<c:out value="subject-${item.subjectId}"/>"
                        data-subject-id="<c:out value="${item.subjectId}"/>"
                        data-year="<c:out value="${item.year}"/>"
                    >
                        <td class="note-found-title">
                            <span class="card-title align-middle mx-2">
                                <c:out value="${item.name}"/>
                            </span>
                        </td>
                        <td class="note-found-title">
                            <span class="card-title align-middle mx-2">
                                <c:out value="${item.year}"/>
                            </span>
                        </td>
                        <td class="search-actions">
                            <div class="d-flex justify-content-end">
                                <!-- EDIT -->
                                <div data-bs-toggle="tooltip" data-bs-placement="bottom"
                                     data-bs-title="<spring:message code="edit"/>" data-bs-trigger="hover">
                                    <button class="btn nav-icon-button edit-button"
                                            data-bs-toggle="modal" data-bs-target="#editSubjectModal"
                                            id="<c:out value="edit-${item.subjectId}"/>"
                                            data-
                                    >
                                        <img src="<c:url value="/svg/pencil.svg"/>"
                                             alt="<spring:message code="edit"/>"
                                             class="icon-xs fill-text">
                                    </button>
                                </div>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
    <div class="container my-5">
        <c:url var="addSubjectUrl" value="./${careerId}/addSubject"/>
        <form:form modelAttribute="addSubjectForm"
                   action="${addSubjectUrl}"
                   method="post"
                   id="newSubjectForm"
                   cssClass="d-flex flex-column w-100 align-items-center">
            <div class="row row-cols-2 row-cols-md-3">
                <div class="col">
                    <select id="newSubjectSelect" style="display: none;">
                        <option disabled selected value></option>
                    </select>
                    <form:input path="subjectId" id="newSubjectId" style="display: none;"/>
                    <div class="input-group mb-3">
                        <div class="autocomplete">
                            <spring:message code="search.subject.placeholder" var="placeholderSubject"/>
                            <input type="text" id="newSubjectAutocomplete" class="form-control bg-bg special-radius"
                                   placeholder="${placeholderSubject}" autocomplete="off"/>
                        </div>
                        <span class="input-group-text input-group-icon clickable" id="eraseNewSubjectButton">
                                <img src="<c:url value="/svg/cross.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-xs fill-dark-primary"/>
                        </span>
                    </div>
                    <form:errors path="subjectId" cssClass="text-danger" element="p"/>
                </div>
                <div class="col">
                    <div class="input-group mb-3">
                        <div class="autocomplete">
                            <spring:message code="search.year" var="placeholderYear"/>
                            <form:input type="number" id="yearInput" class="form-control bg-bg special-radius" min="1" max="10"
                                   placeholder="${placeholderYear}" autocomplete="off" path="year"/>
                        </div>
                    </div>
                    <form:errors path="year" cssClass="text-danger" element="p"/>
                </div>
                <div class="w-25">
                    <button type="submit" id="addSubjectButton" class="btn button-primary w-100"><spring:message code="add"/></button>
                </div>
            </div>
        </form:form>
    </div>


    <!-- EDIT SUBJECT MODAL -->
    <div class="modal fade" id="editSubjectModal" data-bs-backdrop="static" data-bs-keyboard="false"
         tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content box bg-bg">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="editLabel"><spring:message
                            code="editSubject"/></h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <!-- EDIT NOTE FORM -->
                <c:url var="editSubjectUrl" value="./${careerId}/editSubject"/>
                <form:form modelAttribute="editSubjectForm"
                           action="${editSubjectUrl}"
                           method="post"
                           autocomplete="off"
                           class="d-flex flex-column"
                           id="editSubjectForm">
                    <div class="modal-body pb-0">

                        <div class="d-flex flex-column gap-2">
                            <div class="input-group">
                                <label class="input-group-text" for="year"><spring:message
                                        code="year"/></label>
                                <form:input path="year" type="number" min="1" max="10"
                                            class="form-control" id="editYear"/>
                            </div>
                            <form:errors path="year" cssClass="text-danger" element="p"/>
                        </div>
                    </div>
                    <div class="modal-footer mt-4">
                        <button type="button" class="btn rounded-box button-primary"
                                data-bs-dismiss="modal">
                            <spring:message code="close"/></button>
                        <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="update"/>"/>
                    </div>
                    <input type="hidden" name="subjectId" id="editSubjectId"/>
                </form:form>
            </div>
        </div>
    </div>

</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script>
    //TODO c:out institution data
    var { institutions, careers, subjects, careerMap, subjectMap } = JSON.parse('${institutionData}');
    <c:if test="${career ne null}">
        var institutionId = '${career.institutionId}';
        var careerId = '${career.careerId}';
        var unownedSubjects = JSON.parse('${unownedSubjects}');
    </c:if>
    <c:if test="${errorsEditSubjectForm ne null}">
        let editSubjectModal = new bootstrap.Modal(document.getElementById('editSubjectModal'), {})
        editSubjectModal.show();

    </c:if>
</script>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ics-autocomplete.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/manage-career.js"/>"></script>

</body>

</html>