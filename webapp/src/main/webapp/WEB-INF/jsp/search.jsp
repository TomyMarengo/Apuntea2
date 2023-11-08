<%--suppress HtmlFormInputWithoutLabel --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en" data-search-view="horizontal">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Apuntea | <spring:message code="search.title"/></title>
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
    <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/search/table-list.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/halloween.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar user="${user}"/>

    <!-- BOTTOM-NAVBAR -->
    <spring:message code="search.title" var="title"/>
    <fragment:bottom-navbar title="${baseUrl}/search,${title}">
    </fragment:bottom-navbar>
</header>

<main>
    <fragment:sidebar user="${user}"/>

    <section class="container-fluid">
        <!-- SEARCH -->
        <c:url var="searchUrl" value="./search"/>
        <form:form modelAttribute="searchForm"
                   action="${searchUrl}"
                   method="get"
                   id="searchForm"
                   cssClass="d-flex flex-column gap-2 mt-5">

            <div class="container my-5 d-flex flex-column align-items-center">


                <div class="w-100 row row-cols-2 row-cols-lg-4">
                    <div class="col">
                        <select id="institutionSelect" style="display: none;">
                            <option disabled selected value></option>
                        </select>

                        <form:input path="institutionId" id="institutionId" style="display: none;"/>

                        <div class="input-group mb-3">
                            <div class="autocomplete">
                                <spring:message code="search.institution.placeholder" var="placeholderInstitution"/>
                                <input type="text" id="institutionAutocomplete"
                                       class="form-control bg-bg special-radius"
                                       placeholder="${placeholderInstitution}" autocomplete="off"/>
                            </div>
                            <button type="button" class="input-group-text input-group-icon" id="eraseInstitutionButton">
                                <img src="<c:url value="/svg/cross.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-xs fill-dark-primary"/>
                            </button>
                        </div>
                    </div>

                    <div class="col">
                        <select id="careerSelect" style="display: none;">
                            <option disabled selected value></option>
                        </select>

                        <form:input path="careerId" id="careerId" style="display: none;"/>

                        <div class="input-group mb-3">
                            <div class="autocomplete">
                                <spring:message code="search.career.placeholder" var="placeholderCareer"/>
                                <input type="text" id="careerAutocomplete" class="form-control bg-bg special-radius"
                                       placeholder="${placeholderCareer}" autocomplete="off"/>
                            </div>
                            <button type="button" class="input-group-text input-group-icon" id="eraseCareerButton">
                                <img src="<c:url value="/svg/cross.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-xs fill-dark-primary"/>
                            </button>
                        </div>
                    </div>

                    <div class="col">
                        <select id="subjectSelect" style="display: none;">
                            <option disabled selected value></option>
                        </select>

                        <form:input path="subjectId" id="subjectId" style="display: none;"/>

                        <div class="input-group mb-3">
                            <div class="autocomplete">
                                <spring:message code="search.subject.placeholder" var="placeholderSubject"/>
                                <input type="text" id="subjectAutocomplete" class="form-control bg-bg special-radius"
                                       placeholder="${placeholderSubject}" autocomplete="off"/>
                            </div>
                            <button type="button" class="input-group-text input-group-icon" id="eraseSubjectButton">
                                <img src="<c:url value="/svg/cross.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-xs fill-dark-primary"/>
                            </button>
                        </div>
                    </div>

                    <div class="col">
                        <div class="input-group mb-3">
                            <spring:message code="search.word.placeholder" var="placeholderSearch"/>
                            <form:input path="word" id="wordInput" type="text" class="form-control bg-bg"
                                        placeholder='${placeholderSearch}'/>
                        </div>
                    </div>

                </div>

                <form:hidden path="pageNumber" id="pageNumber" value="1"/>

                <!-- TODO: Maybe move and add profile info? -->
                <c:if test="${filterUser ne null}">
                    <form:hidden path="userId" id="userId" value="${filterUser.userId}"/>
                </c:if>

                <div class="w-50 d-flex flex-column justify-content-center align-items-center">
                    <button type="submit" class="btn button-primary w-50"><spring:message
                            code="search.button"/></button>
                    <c:if test="${filterUser ne null}">
                        <a class="btn text-dark-primary" href="./search?userId=${filterUser.userId}"><spring:message
                                code="search.button.clearAllFilters"/></a>
                    </c:if>
                    <c:if test="${filterUser eq null}">
                        <a class="btn text-dark-primary" href="./search"><spring:message
                                code="search.button.clearAllFilters"/></a>
                    </c:if>
                </div>


            </div>


            <!-- TOP BUTTONS -->
            <!-- DEFINES -->
            <spring:message code="download" var="download"/>
            <spring:message code="folder" var="folder"/>
            <spring:message code="search.toggleView" var="searchViewImage"/>
            <c:url value="/svg/box-list.svg" var="boxViewUrl"/>
            <c:url value="/svg/horizontal-list.svg" var="horizontalViewUrl"/>

            <div class="container mt-5 d-flex justify-content-between p-0">

                <!-- SEARCH PILL -->
                <div class="d-flex align-items-center gap-3">
                    <div class="search-pill">
                        <button type="button" id="selectOnlyFoldersButton">
                            <img src="<c:url value="/svg/folder.svg"/>" alt="<spring:message code="folder"/>"
                                 class="icon-s fill-bg"/>
                            <spring:message code="folders"/>
                        </button>

                        <button type="button" id="selectAllCategoriesButton">
                            <spring:message code="category.all"/>
                        </button>

                        <button type="button" id="selectOnlyFilesButton">
                            <spring:message code="files"/>
                            <img src="<c:url value="/svg/file.svg"/>" alt="<spring:message code="folder"/>"
                                 class="icon-s fill-bg"/>
                        </button>
                    </div>

                    <div class="input-group">
                        <button class="input-group-text input-group-icon" id="ascDescButton">
                            <form:checkbox path="ascending" id="ascCheckbox" cssClass="d-none"/>
                            <c:if test="${searchForm.ascending}">
                                <img src="<c:url value="/svg/arrow-up.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-dark-primary"
                                     id="arrowImage" title="ascending"/>
                            </c:if>
                            <c:if test="${!searchForm.ascending}">
                                <img src="<c:url value="/svg/arrow-down.svg"/>"
                                     alt="<spring:message code="search.sort.image"/>"
                                     class="icon-s fill-dark-primary"
                                     id="arrowImage" title="descending"/>
                            </c:if>
                        </button>

                        <form:select path="sortBy" class="form-select bg-bg" id="sortBySelect" cssStyle="width:270px;"
                                     onchange="submitSearchForm()">
                            <form:option value="date"><spring:message code="search.sort.date"/></form:option>
                            <c:if test="${searchForm.category ne 'all' and searchForm.category ne 'directory'}">
                                <form:option value="score"><spring:message code="search.sort.score"/></form:option>
                            </c:if>
                            <form:option value="name"><spring:message code="search.sort.name"/></form:option>
                        </form:select>
                    </div>

                    <div class="input-group" id="categorySelectContainer">
                        <c:if test="${searchForm.category ne 'all' and searchForm.category ne 'directory'}">
                            <div class="input-group">
                                <form:select path="category" class="form-select bg-bg" id="categorySelect"
                                             onchange="submitSearchForm()">
                                    <form:option
                                            value="note"><spring:message
                                            code="search.category.all"/></form:option>
                                    <form:option
                                            value="theory"><spring:message
                                            code="search.category.theory"/></form:option>
                                    <form:option
                                            value="practice"><spring:message
                                            code="search.category.practice"/></form:option>
                                    <form:option
                                            value="exam"><spring:message
                                            code="search.category.exam"/></form:option>
                                    <form:option
                                            value="other"><spring:message
                                            code="search.category.other"/></form:option>
                                    <form:option cssClass="d-none"
                                                 value="directory"/>
                                    <form:option cssClass="d-none"
                                                 value="all"/>
                                </form:select>
                            </div>
                        </c:if>
                        <c:if test="${searchForm.category eq 'all' or searchForm.category eq 'directory'}">
                            <form:hidden path="category" cssClass="form-select bg-bg d-none" id="categorySelect"/>
                        </c:if>
                    </div>


                </div>

                <!-- BUTTONS -->
                <c:if test="${not empty results}">
                    <div class="d-flex">
                        <div id="selectedButtons" class="align-items-center" style="display: none;">
                            <button type="button" id="deselectAllButton" class="btn nav-icon-button"
                                    data-bs-toggle="tooltip"
                                    data-bs-placement="bottom"
                                    data-bs-title="<spring:message code="search.button.deselectAll"/>"
                                    data-bs-trigger="hover">
                                <img src="<c:url value="/svg/cross.svg"/>" alt="deselect"
                                     class="icon-s fill-dark-primary"/>
                            </button>
                            <span class="text-dark-primary d-flex flex-row">
                        <strong id="selectedCount" class="text-dark-primary mx-1"> 0 </strong>
                        <spring:message code="search.selected"/>
                    </span>
                            <button type="button" id="downloadSelectedButton" class="btn nav-icon-button"
                                    data-bs-toggle="tooltip"
                                    data-bs-placement="bottom" data-bs-title="<spring:message code="download"/>"
                                    data-bs-trigger="hover">
                                <img src="<c:url value="/svg/download.svg"/>" alt="download"
                                     class="icon-s fill-dark-primary"/>
                            </button>
                        </div>


                        <button type="button" id="selectAllButton" class="btn nav-icon-button" data-bs-toggle="tooltip"
                                data-bs-placement="bottom"
                                data-bs-title="<spring:message code="search.button.selectAll"/>"
                                data-bs-trigger="hover">
                            <img src="<c:url value="/svg/list-check.svg"/>" alt="select all"
                                 class="icon-s fill-dark-primary"/>
                        </button>

                        <button type="button" id="searchViewToggle" class="btn nav-icon-button" data-bs-toggle="tooltip"
                                data-bs-placement="bottom"
                                data-bs-title="<spring:message code="search.button.listView"/>"
                                data-horizontal="<spring:message code="search.button.listView"/>"
                                data-box="<spring:message code="search.button.boxView"/>" data-bs-trigger="hover">
                            <img id="searchViewIcon" src="${horizontalViewUrl}" alt="${searchViewImage}"
                                 class="icon-s fill-dark-primary"/>
                        </button>

                        <div data-bs-toggle="tooltip" data-bs-placement="right"
                             data-bs-title="<spring:message code="search.button.pageSize"/>"
                             data-bs-trigger="hover">
                            <form:select path="pageSize" class="form-select bg-bg" onchange="submitSearchForm()">
                                <form:option value="12">12</form:option>
                                <form:option value="18">18</form:option>
                                <form:option value="24">24</form:option>
                                <c:if test="${searchForm.pageSize ne 12 and searchForm.pageSize ne 18 and searchForm.pageSize ne 24}">
                                    <form:option value="${searchForm.pageSize}"><c:out
                                            value="${searchForm.pageSize}"/></form:option>
                                </c:if>
                            </form:select>
                        </div>
                    </div>
                </c:if>
            </div>

        </form:form>

        <c:if test="${empty results}">
            <section class="container mt-4">
                <p><spring:message code="notes.noNotes"/></p>
            </section>
        </c:if>

        <c:if test="${not empty results}">
            <c:set var="folders" value=""/>
            <c:set var="files" value=""/>
            <!-- HORIZONTAL LIST -->
            <section class="container mt-4 p-0" id="horizontalList">
                <div class="table-responsive">
                    <table class="table table-hover table-search">
                        <thead>
                        <tr>
                            <th class="col-md-3"><spring:message code="name"/></th>
                            <th class="col-md-3"><spring:message code="subject"/></th>
                            <th class="col-md-2"><spring:message code="owner"/></th>
                            <th class="col-md-1"><spring:message code="createdAt"/></th>
                            <c:if test="${searchForm.category ne 'all' and searchForm.category ne 'directory'}">
                                <th class="col-md-1"><spring:message code="score"/></th>
                            </c:if>
                            <th class="col-md-2"></th> <!-- ACTIONS -->
                            <!-- TODO: ADD SIZE OF FILE -->
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="item" items="${results}">

                            <!-- SAVE FOR BOX LIST -->
                            <c:if test="${item.category.formattedName eq 'directory'}">
                                <c:set var="folders" value="${folders}${item.id},"/>
                            </c:if>
                            <c:if test="${item.category.formattedName ne 'directory'}">
                                <c:set var="files" value="${files}${item.id},"/>
                            </c:if>

                            <c:set var="date" value="${item.createdAt}"/>
                            <tr class="note-found no-select"
                                data-category="<c:out value="${item.category.formattedName}"/>"
                                data-visible="${item.visible}"
                                id="<c:out value="${item.id}"/>.1">
                                <td class="note-found-title">
                                    <c:if test="${item.category.formattedName ne 'directory'}">
                                        <c:if test="${item.fileType eq 'pdf'}">
                                            <img src="<c:url value="/image/pdf.png"/>" alt="pdf" class="icon-m">
                                        </c:if>
                                        <c:if test="${item.fileType eq 'jpeg'}">
                                            <img src="<c:url value="/image/jpeg.png"/>" alt="jpeg" class="icon-m">
                                        </c:if>
                                        <c:if test="${item.fileType eq 'jpg'}">
                                            <img src="<c:url value="/image/jpg.png"/>" alt="jpg" class="icon-m">
                                        </c:if>
                                        <c:if test="${item.fileType eq 'png'}">
                                            <img src="<c:url value="/image/png.png"/>" alt="png" class="icon-m">
                                        </c:if>
                                        <c:if test="${item.fileType eq 'mp3'}">
                                            <img src="<c:url value="/image/mp3.png"/>" alt="mp3" class="icon-m">
                                        </c:if>
                                        <c:if test="${item.fileType eq 'mp4'}">
                                            <img src="<c:url value="/image/mp4.png"/>" alt="mp4" class="icon-m">
                                        </c:if>
                                    </c:if>
                                    <c:if test="${item.category.formattedName eq 'directory'}">
                                        <img src="<c:url value="/svg/folder.svg"/>" alt="${folder}"
                                             data-color="${item.iconColor}"
                                             class="icon-m fill-${item.iconColor} folder-icon">
                                    </c:if>
                                    <span class="card-title align-middle mx-2 note-name">
                                    <c:out value="${item.name}"/>
                                </span>
                                </td>
                                <td>
                                    <p class="subject-name">
                                        <c:out value="${item.subject.name}"/>
                                    </p>
                                </td>
                                <td><c:out value="${item.user.email}"/></td>
                                <td><spring:message code="date.format"
                                                    arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/></td>
                                <c:if test="${searchForm.category ne 'all' and searchForm.category ne 'directory'}">
                                    <td>
                                        <c:if test="${item.avgScore eq 0}">
                                            <spring:message code="notes.noScore"/>
                                        </c:if>
                                        <c:if test="${item.avgScore ne 0}">
                                            <fmt:formatNumber type="number" maxFractionDigits="1"
                                                              value="${item.avgScore}"/>
                                        </c:if>
                                    </td>
                                </c:if>

                                <td class="search-actions">
                                    <div class="d-flex justify-content-end">

                                        <!-- Favorite -->
                                        <c:if test="${user ne null}">
                                            <!-- TODO: Change all occurrences of notes for note -->
                                            <c:if test="${item.category.type eq 'directory'}">
                                                <c:set var="addFavorite" value="./directory/${item.id}/addfavorite"/>
                                                <c:set var="removeFavorite"
                                                       value="./directory/${item.id}/removefavorite"/>
                                            </c:if>
                                            <c:if test="${item.category.type ne 'directory'}">
                                                <c:set var="addFavorite" value="./notes/${item.id}/addfavorite"/>
                                                <c:set var="removeFavorite" value="./notes/${item.id}/removefavorite"/>
                                            </c:if>

                                            <div data-bs-toggle="tooltip" data-bs-placement="bottom"
                                                 data-bs-title="<spring:message code="favorite"/>"
                                                 data-bs-trigger="hover">
                                                <form:form action="${item.favorite ? removeFavorite : addFavorite}"
                                                           method="post">
                                                    <input name="redirectUrl"
                                                           value="/search?${requestScope['javax.servlet.forward.query_string']}"
                                                           type="hidden"/>
                                                    <button type="submit"
                                                            class="btn nav-icon-button"
                                                            data-bs-toggle="tooltip"
                                                            data-bs-placement="bottom"
                                                            data-bs-title="<spring:message code="favorite"/>"
                                                            id="<c:out value="${item.id}"/>.f1">
                                                        <img src="<c:url value="${ item.favorite ? '/svg/filled-heart.svg' : '/svg/heart.svg'}"/>"
                                                             alt="<spring:message code="favorite"/>"
                                                             class="icon-xs fill-text">
                                                    </button>
                                                </form:form>
                                            </div>
                                        </c:if>

                                        <c:if test="${item.category.formattedName ne 'directory'}"> <!-- FOLDERS CANNOT BE DOWNLOADED -->
                                            <a href="./notes/${item.id}/download" download="${item.name}">
                                                <button type="button" class="btn nav-icon-button"
                                                        data-bs-toggle="tooltip" data-bs-placement="bottom"
                                                        data-bs-title="<spring:message code="download"/>"
                                                        data-bs-trigger="hover">
                                                    <img src="<c:url value="/svg/download.svg"/>" alt="${download}"
                                                         class="icon-xs fill-text">
                                                </button>
                                            </a>
                                        </c:if>

                                        <!-- ALL CAN BE COPIED -->
                                        <button class="btn nav-icon-button copy-button"
                                                id="<c:out value="${item.id}"/>.c1"
                                                data-bs-toggle="tooltip"
                                                data-bs-placement="bottom"
                                                data-bs-title="<spring:message code="copyLink"/>"
                                                data-bs-trigger="hover">
                                            <img src="<c:url value="/svg/link.svg"/>"
                                                 alt="<spring:message code="copyLink"/>"
                                                 class="icon-xs fill-text">
                                        </button>
                                    </div>

                                    <input type="checkbox" class="select-checkbox d-none"/>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>

            <!-- BOX LIST -->
            <section class="container mt-4 p-0" id="boxList">

                <!-- FOLDERS -->
                <c:if test="${folders ne ''}">
                    <h5 class="mx-1 mb-3"><strong><spring:message code="folders"/></strong></h5>
                    <div class="row">
                        <c:forEach items="${results}" var="item">
                            <c:if test="${item.category.formattedName eq 'directory'}">
                                <div class="col-md-4 mb-4">
                                    <div class="note-found card box search-note-box h-100"
                                         data-category="<c:out value="${item.category.formattedName}"/>"
                                         id="<c:out value="${item.id}"/>.2">
                                        <div class="card-body no-select">

                                            <!-- TITLE AND BUTTONS -->
                                            <div class="d-flex justify-content-between">
                                                <div class="d-flex gap-2 overflow-hidden align-items-center mb-2">
                                                    <img src="<c:url value="/svg/folder.svg"/>" alt="${folder}"
                                                         data-color="${item.iconColor}"
                                                         class="icon-m fill-${item.iconColor} folder-icon">
                                                    <h4 class="card-title text-truncate mb-0 note-name">
                                                        <c:out value="${item.name}"/>
                                                    </h4>
                                                </div>

                                                <div class="d-flex">
                                                    <!-- FAVORITE -->
                                                    <c:set var="addFavorite"
                                                           value="./directory/${item.id}/addfavorite"/>
                                                    <c:set var="removeFavorite"
                                                           value="./directory/${item.id}/removefavorite"/>

                                                    <div data-bs-toggle="tooltip" data-bs-placement="bottom"
                                                         data-bs-title="<spring:message code="favorite"/>"
                                                         data-bs-trigger="hover">
                                                        <form:form
                                                                action="${item.favorite ? removeFavorite : addFavorite}"
                                                                method="post">
                                                            <input name="redirectUrl"
                                                                   value="/search?${requestScope['javax.servlet.forward.query_string']}"
                                                                   type="hidden"/>
                                                            <button type="submit"
                                                                    class="btn nav-icon-button"
                                                                    data-bs-toggle="tooltip"
                                                                    data-bs-placement="bottom"
                                                                    data-bs-title="<spring:message code="favorite"/>"
                                                                    id="<c:out value="${item.id}"/>.f1">
                                                                <img src="<c:url value="${ item.favorite ?  '/svg/filled-heart.svg' : '/svg/heart.svg'}"/>"
                                                                     alt="<spring:message code="favorite"/>"
                                                                     class="icon-xs fill-text">
                                                            </button>
                                                        </form:form>
                                                    </div>
                                                </div>

                                            </div>

                                            <span class="card-text">
                                            <strong><spring:message code="owner"/></strong>:
                                            <c:out value="${item.user.email}"/>
                                        </span>

                                            <br>

                                            <span class="card-text">
                                            <strong><spring:message code="createdAt"/></strong>:
                                            <spring:message code="date.format"
                                                            arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/>
                                        </span>

                                            <input type="checkbox" class="select-checkbox d-none"/>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- FILES -->
                <c:if test="${files ne ''}">
                    <h5 class="mx-1 mb-3 mt-1"><strong><spring:message code="files"/></strong></h5>
                    <div class="row">
                        <c:forEach items="${results}" var="item">
                            <c:if test="${item.category.formattedName ne 'directory'}">
                                <div class="col-md-4 mb-4">
                                    <div class="note-found card box search-note-box h-100"
                                         data-category="<c:out value="${item.category.formattedName}"/>"
                                         id="<c:out value="${item.id}"/>.2">
                                        <div class="card-body no-select">

                                            <!-- TITLE AND BUTTONS -->
                                            <div class="d-flex justify-content-between">
                                                <div class="d-flex gap-2 overflow-hidden align-items-center mb-2">
                                                    <c:if test="${item.fileType eq 'pdf'}">
                                                        <img src="<c:url value="/image/pdf.png"/>" alt="pdf"
                                                             class="icon-m">
                                                    </c:if>
                                                    <c:if test="${item.fileType eq 'jpeg'}">
                                                        <img src="<c:url value="/image/jpeg.png"/>" alt="jpeg"
                                                             class="icon-m">
                                                    </c:if>
                                                    <c:if test="${item.fileType eq 'jpg'}">
                                                        <img src="<c:url value="/image/jpg.png"/>" alt="jpg"
                                                             class="icon-m">
                                                    </c:if>
                                                    <c:if test="${item.fileType eq 'png'}">
                                                        <img src="<c:url value="/image/png.png"/>" alt="png"
                                                             class="icon-m">
                                                    </c:if>
                                                    <c:if test="${item.fileType eq 'mp3'}">
                                                        <img src="<c:url value="/image/mp3.png"/>" alt="mp3"
                                                             class="icon-m">
                                                    </c:if>
                                                    <c:if test="${item.fileType eq 'mp4'}">
                                                        <img src="<c:url value="/image/mp4.png"/>" alt="mp4"
                                                             class="icon-m">
                                                    </c:if>
                                                    <h4 class="card-title text-truncate mb-0 note-name">
                                                        <c:out value="${item.name}"/>
                                                    </h4>
                                                </div>

                                                <div class="d-flex">
                                                    <!-- FAVORITE -->
                                                    <c:set var="addFavorite" value="./notes/${item.id}/addfavorite"/>
                                                    <c:set var="removeFavorite"
                                                           value="./notes/${item.id}/removefavorite"/>

                                                    <div data-bs-toggle="tooltip" data-bs-placement="bottom"
                                                         data-bs-title="<spring:message code="favorite"/>"
                                                         data-bs-trigger="hover">
                                                        <form:form
                                                                action="${item.favorite ? removeFavorite : addFavorite}"
                                                                method="post">
                                                            <input name="redirectUrl"
                                                                   value="/search?${requestScope['javax.servlet.forward.query_string']}"
                                                                   type="hidden"/>
                                                            <button type="submit"
                                                                    class="btn nav-icon-button"
                                                                    data-bs-toggle="tooltip"
                                                                    data-bs-placement="bottom"
                                                                    data-bs-title="<spring:message code="favorite"/>"
                                                                    id="<c:out value="${item.id}"/>.f1">
                                                                <img src="<c:url value="${ item.favorite ?  '/svg/filled-heart.svg' : '/svg/heart.svg'}"/>"
                                                                     alt="<spring:message code="favorite"/>"
                                                                     class="icon-xs fill-text">
                                                            </button>
                                                        </form:form>
                                                    </div>
                                                </div>
                                            </div>

                                            <span class="card-text">
                                            <strong><spring:message code="owner"/></strong>:
                                            <c:out value="${item.user.email}"/>
                                        </span>

                                            <br>

                                            <span class="card-text">
                                            <strong><spring:message code="createdAt"/></strong>:
                                            <spring:message code="date.format"
                                                            arguments="${date.year},${date.monthValue},${date.dayOfMonth}"/>
                                        </span>

                                            <br>
                                            <span class="card-text">
                                            <c:if test="${item.avgScore eq 0}">
                                                <strong><spring:message code="notes.noScore"/></strong>
                                            </c:if>
                                            <c:if test="${item.avgScore ne 0}">
                                                <strong><spring:message code="score"/></strong>:
                                                <fmt:formatNumber type="number" maxFractionDigits="1"
                                                                  value="${item.avgScore}"/>
                                            </c:if>
                                        </span>
                                            <input type="checkbox" class="select-checkbox d-none"/>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>
            </section>

            <!-- PAGINATION -->
            <c:if test="${maxPage gt 1}">
                <fragment:paging maxPage="${maxPage}" pageNumber="${currentPage}"/>
            </c:if>
        </c:if>
    </section>

</main>

<fragment:custom-toast message=""/>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous"></script>

<script>
    const {institutions, careerMap, subjectMap} = JSON.parse('${institutionData}');
</script>

<script src="<c:url value="/js/autocomplete.js"/>"></script>
<script src="<c:url value="/js/ics-autocomplete.js"/>"></script>
<script src="<c:url value="/js/ascdesc.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>

<script src="<c:url value="/js/search-buttons.js"/>"></script>
<c:if test="${not empty results}">
    <script src="<c:url value="/js/note-list.js"/>"></script>
</c:if>

<script>
    const liveToast = document.getElementById('liveToast');
    for (let copyButton of document.getElementsByClassName('copy-button')) {
        copyButton.addEventListener('click', () => {
            displayToast('<spring:message code="toast.linkCopied"/>')
        })
    }
</script>

<script>
    <c:if test="${favoriteAdded eq true}">
    displayToast('<spring:message code="toast.addFavorite"/>')
    </c:if>
    <c:if test="${favoriteRemoved eq true}">
    displayToast('<spring:message code="toast.removeFavorite"/>')
    </c:if>
</script>

</body>

</html>