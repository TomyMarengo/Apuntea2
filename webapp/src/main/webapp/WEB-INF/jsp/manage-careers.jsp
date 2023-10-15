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
    <title>Apuntea | <spring:message code="manageCareers.title"/></title>
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
    <link rel="stylesheet" href="<c:url value="/css/sections/manage/bar.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar loggedIn="${user != null}" isAdmin="${user.roles[1] ne null}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="manageCareers.title" var="title"/>
    <fragment:bottom-navbar title="/manage/careers:${title}">
    </fragment:bottom-navbar>
</header>

<main style="margin-left: 50px !important;">
    <!-- CAREER SELECT -->
    <div class="container my-5">
        <div class="row row-cols-1 row-cols-lg-2 justify-content-center">

            <div class="col col-lg-5">
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

            <div class="col col-lg-5">
                <select id="careerSelect" style="display: none;">
                    <option disabled selected value></option>
                </select>

                <input id="careerId" style="display: none;"/>

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

    <!-- SIDEBAR -->
    <div class="sidebar">
        <a class="btn nav-icon-button" href="${baseUrl}/manage/careers" data-bs-toggle="tooltip" data-bs-placement="right"
           data-bs-title="<spring:message code="manageCareers.title"/>" data-bs-trigger="hover">
            <img src="<c:url value="/svg/books.svg"/>"
                 alt="<spring:message code="manageUsers.title"/>"
                 class="icon-m fill-dark-primary"/>
        </a>

        <a class="btn nav-icon-button" href="${baseUrl}/manage/users" data-bs-toggle="tooltip" data-bs-placement="right"
           data-bs-title="<spring:message code="manageUsers.title"/>" data-bs-trigger="hover">
            <img src="<c:url value="/svg/user-slash.svg"/>"
                 alt="<spring:message code="manageUsers.title"/>"
                 class="icon-m fill-dark-primary"/>
        </a>

    </div>

    <c:if test="${career ne null}">
    <!-- TOP BUTTONS -->
    <div class="d-flex container mt-4 justify-content-between p-0">
        <div class="d-flex">
            <a href="#" data-bs-toggle="tooltip" data-bs-placement="bottom"
               data-bs-title="<spring:message code="linkSubject"/>"
               data-bs-trigger="hover">
                <button class="btn" data-bs-toggle="modal" data-bs-target="#linkSubjectModal"
                        id="newSubjectModalButton">
                    <img src="<c:url value="/svg/link-horizontal.svg"/>"
                         alt="<spring:message code="linkSubject"/>"
                         class="icon-m fill-dark-primary"/>
                </button>
            </a>
            <a href="#" data-bs-toggle="tooltip" data-bs-placement="bottom"
               data-bs-title="<spring:message code="createSubject"/>"
               data-bs-trigger="hover">
                <button class="btn" data-bs-toggle="modal" data-bs-target="#createSubjectModal"
                        id="createSubjectModalButton">
                    <img src="<c:url value="/svg/magic-wand.svg"/>"
                         alt="<spring:message code="createSubject"/>"
                         class="icon-m fill-dark-primary"/>
                </button>
            </a>
        </div>
    </div>
    <!-- HORIZONTAL LIST -->
    <section class="container mt-4 p-0" style="max-height: 500px; overflow-y: scroll">
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
                        data-name="<c:out value="${item.name}"/>"
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

                                <div data-bs-toggle="tooltip" data-bs-placement="bottom"
                                     data-bs-title="<spring:message code="unlink"/>" data-bs-trigger="hover">
                                    <button class="btn nav-icon-button delete-button"
                                            data-bs-toggle="modal" data-bs-target="#unlinkSubjectModal"
                                            id="<c:out value="unlink-${item.subjectId}"/>">
                                        <img src="<c:url value="/svg/trash.svg"/>"
                                             alt="<spring:message code="unlink"/>"
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
</main>

<!-- LINK SUBJECT MODAL -->
<div class="modal fade" id="linkSubjectModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="linkLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">

            <!-- LINK SUBJECT FORM -->
            <c:url var="linkSubjectUrl" value="./${careerId}/linkSubject"/>
            <form:form modelAttribute="linkSubjectForm"
                       action="${linkSubjectUrl}"
                       method="post"
                       id="linkSubjectForm"
                       class="d-flex flex-column">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="linkLabel"><spring:message
                            code="linkSubject"/></h1>
                    <button type="button" class="btn-close close-modal" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <div class="modal-body pb-0">
                    <div class="d-flex flex-column gap-2">
                        <select id="linkSubjectSelect" style="display: none;">
                            <option disabled selected value></option>
                        </select>
                        <form:input path="subjectId" id="linkSubjectId" style="display: none;"/>
                        <div class="input-group">
                            <div class="autocomplete">
                                <spring:message code="linkForm.subject.placeholder" var="placeholderSubject"/>
                                <input type="text" id="linkSubjectAutocomplete" class="form-control special-radius"
                                       placeholder="${placeholderSubject}" autocomplete="off" required/>
                            </div>
                            <span class="input-group-text input-group-icon clickable" id="eraseLinkSubjectButton">
                                <img src="<c:url value="/svg/cross.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-xs fill-dark-primary"/>
                                </span>
                        </div>
                        <form:errors path="subjectId" cssClass="text-danger" element="p"/>
                    </div>
                    <div class="d-flex flex-column gap-2 mt-4">
                        <div class="input-group">
                            <label class="input-group-text" for="year"><spring:message
                                    code="year"/></label>
                            <spring:message code="search.year" var="placeholderYear"/>
                            <form:input type="number" id="yearInput" class="form-control" min="1" max="10"
                                        required="true"
                                        placeholder="${placeholderYear}" autocomplete="off" path="year"/>
                        </div>
                        <form:errors path="year" cssClass="text-danger" element="p"/>
                    </div>
                </div>
                <div class="modal-footer mt-4">
                    <button type="button" class="btn rounded-box button-primary close-modal"
                            data-bs-dismiss="modal">
                        <spring:message code="close"/></button>
                    <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="link"/>"/>
                </div>
            </form:form>
        </div>
    </div>
</div>

<!-- CREATE SUBJECT MODAL -->
<div class="modal fade" id="createSubjectModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="createLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">
            <!-- CREATE SUBJECT FORM -->
            <c:url var="createSubjectUrl" value="./${careerId}/createSubject"/>
            <form:form modelAttribute="createSubjectForm"
                       action="${createSubjectUrl}"
                       method="post"
                       autocomplete="off"
                       class="d-flex flex-column"
                       id="createSubjectForm">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="createLabel"><spring:message
                            code="createSubject"/></h1>
                    <button type="button" class="btn-close close-modal" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <div class="modal-body pb-0">
                    <div class="d-flex flex-column gap-2">
                        <div class="input-group">
                            <spring:message code="name" var="placeholderName"/>
                            <label class="input-group-text" for="name"><spring:message code="name"/></label>
                            <form:input path="name" type="text" class="form-control" id="createName"
                                        placeholder="${placeholderName}" required="true"/>
                        </div>

                        <form:errors path="name" cssClass="text-danger" element="p"/>
                    </div>
                    <div class="d-flex flex-column gap-2 mt-4">
                        <div class="input-group">
                            <label class="input-group-text" for="year"><spring:message
                                    code="year"/></label>
                            <form:input path="year" type="number" min="1" max="10" required="true"
                                        class="form-control" id="createYear" placeholder="${placeholderYear}"
                                        autocomplete="off"/>
                        </div>
                        <form:errors path="year" cssClass="text-danger" element="p"/>
                    </div>
                </div>
                <div class="modal-footer mt-4">
                    <button type="button" class="btn rounded-box button-primary close-modal"
                            data-bs-dismiss="modal">
                        <spring:message code="close"/></button>
                    <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="create"/>"/>
                </div>
            </form:form>
        </div>
    </div>
</div>

<!-- EDIT SUBJECT MODAL -->
<div class="modal fade" id="editSubjectModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="editLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">
            <!-- EDIT NOTE FORM -->
            <c:url var="editSubjectUrl" value="./${careerId}/editSubject"/>
            <form:form modelAttribute="editSubjectForm"
                       action="${editSubjectUrl}"
                       method="post"
                       autocomplete="off"
                       class="d-flex flex-column"
                       id="editSubjectForm">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="editLabel"><spring:message
                            code="editSubject"/></h1>
                    <button type="button" class="btn-close close-modal" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <div class="modal-body pb-0">
                    <div class="d-flex flex-column gap-2">
                        <div class="input-group">
                            <label class="input-group-text" for="name"><spring:message
                                    code="name"/></label>
                            <form:input path="name" type="text"
                                        class="form-control" id="editSubjectName"/>
                        </div>
                        <form:errors path="name" cssClass="text-danger" element="p"/>
                    </div>
                    <div class="d-flex flex-column gap-2 mt-4">
                        <div class="input-group">
                            <label class="input-group-text" for="year"><spring:message
                                    code="year"/></label>
                            <form:input path="year" type="number" min="1" max="10" required="true"
                                        class="form-control" id="editSubjectYear"/>
                        </div>
                        <form:errors path="year" cssClass="text-danger" element="p"/>
                    </div>
                </div>
                <div class="modal-footer mt-4">
                    <button type="button" class="btn rounded-box button-primary close-modal"
                            data-bs-dismiss="modal">
                        <spring:message code="close"/></button>
                    <input type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                            code="update"/>"/>
                </div>
                <form:hidden path="subjectId" id="editSubjectId"/>
            </form:form>
        </div>
    </div>
</div>

<!-- UNLINK MODAL -->
<c:url var="unlinkSubjectUrl" value="./${careerId}/unlinkSubject"/>
<div class="modal fade" id="unlinkSubjectModal" data-bs-backdrop="static" data-bs-keyboard="false"
     tabindex="-1" aria-labelledby="unlinkLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content box bg-bg">
            <form:form modelAttribute="unlinkSubjectForm"
                       action="${unlinkSubjectUrl}"
                        method="POST"
                        id="unlinkSubjectForm">
                <div class="modal-header">
                    <h3 class="modal-title fs-5" id="unlinkLabel"><spring:message code="unlinkForm.description"/> :
                        <span id="unlinkSubjectName"></span></h3>
                    <button type="button" class="btn-close close-modal" data-bs-dismiss="modal"
                            aria-label="Close">
                    </button>
                </div>
                <div class="modal-body pb-0">
                    <spring:message code="unlinkForm.confirm" arguments="${career.name}"/>
                    <form:errors cssClass="text-danger" element="p"/>
                </div>

                <div class="modal-footer mt-4">
                    <button type="button" class="btn rounded-box button-primary close-modal"
                            data-bs-dismiss="modal">
                        <spring:message code="close"/></button>
                    <input id="unlinkSelectedButton" type="submit" class="btn rounded-box button-secondary" value="<spring:message
                                                code="unlink"/>"/>
                </div>

                <form:hidden path="subjectId" id="unlinkSubjectId" value=""/>

            </form:form>
        </div>
    </div>
</div>
</c:if>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script>
    const {institutions, careerMap, subjectMap} = JSON.parse('${institutionData}');
    <c:if test="${career ne null}">
    const institutionId = '${career.institutionId}';
    const careerId = '${career.careerId}';
    let unownedSubjects = JSON.parse('${unownedSubjects}');
    </c:if>
    <c:if test="${errorsLinkSubjectForm ne null}">
    let linkSubjectModal = new bootstrap.Modal(document.getElementById('linkSubjectModal'), {})
    linkSubjectModal.show();
    </c:if>
    <c:if test="${errorsUnlinkSubjectForm ne null}">
    let unlinkSubjectIdNode = document.getElementById('unlinkSubjectId');
    if (unlinkSubjectIdNode.value != null) {
        let unlinkSubjectNameNode = document.getElementById('unlinkSubjectName');
        unlinkSubjectNameNode.innerText = subjects.find(subject => subject.subjectId === unlinkSubjectIdNode.value).name;
    }
    let unlinkSubjectModal = new bootstrap.Modal(document.getElementById('unlinkSubjectModal'), {})
    unlinkSubjectModal.show();
    </c:if>
    <c:if test="${errorsCreateSubjectForm ne null}">
    let createSubjectModal = new bootstrap.Modal(document.getElementById('createSubjectModal'), {})
    createSubjectModal.show();
    </c:if>
    <c:if test="${errorsEditSubjectForm ne null}">
    let editSubjectModal = new bootstrap.Modal(document.getElementById('editSubjectModal'), {})
    editSubjectModal.show();
    </c:if>
</script>

<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ics-autocomplete.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/manage-career.js"/>"></script>
<script src="<c:url value="/js/sidebar.js"/>"></script>

</body>

</html>