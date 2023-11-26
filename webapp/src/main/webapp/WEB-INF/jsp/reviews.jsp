<%--suppress HtmlFormInputWithoutLabel --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fragment" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <c:if test="${note eq null}">
        <title> Apuntea | <spring:message code="reviews"/></title>
    </c:if>
    <c:if test="${note ne null}">
        <title> Apuntea | <c:out value="${note.name}"/></title>
    </c:if>
    <link rel="shortcut icon" type="image/x-icon" href="<c:url value="/image/apuntea-icon.png"/>">

    <link rel="stylesheet" href="<c:url value="/css/main.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/elements.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/sizes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/backgrounds.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/texts.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/buttons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/color-picker.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/icons.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/general/boxes.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/bars.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/search/table-list.css"/>"/>
    <link rel="stylesheet" href="<c:url value="/css/sections/user/profile-notes.css"/>"/>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Lato:wght@400;700;900&display=swap" rel="stylesheet">

</head>
<body>

<header>
    <!-- NAVBAR -->
    <fragment:navbar user="${user}" institutionId="${user.institutionId}" careerId="${user.career.careerId}"/>

    <c:if test="${note ne null}">
        <fragment:bottom-navbar title="${baseUrl}/notes/${noteId},${note.name}" user="${user}"/>
    </c:if>

    <c:if test="${note eq null}">
        <spring:message code="reviews" var="myReviewsBn"/>
        <c:if test="${owner.userId ne user.userId}">
            <fragment:bottom-navbar title="${baseUrl}/user/${owner.userId}/note-board,${owner.displayName}" reviewsSection="${true}" user="${user}"/>
        </c:if>
        <c:if test="${owner.userId eq user.userId}">
            <fragment:bottom-navbar title="${baseUrl}/user/${owner.userId}/reviews,${myReviewsBn}" user="${user}"/>
        </c:if>
    </c:if>

</header>

<main>
    <c:if test="${owner.userId ne user.userId}">
        <fragment:sidebar user="${user}"/>
    </c:if>
    <c:if test="${owner.userId eq user.userId}">
        <fragment:sidebar user="${user}" active="reviews"/>
    </c:if>
    <section class="container mt-5">
        <c:if test="${empty reviews}">
            <div class="card box mt-4 gap-2 p-4 d-flex flex-column justify-content-center align-items-center">
                <div class="d-flex">
                    <img src="<c:url value="/image/no-task.png"/>" alt="Empty Folder" class="icon-xl"/>
                    <h3><spring:message code="directories.noContent"/></h3>
                </div>
                <p>
                    <spring:message code="reviews.noContent.description"/>
                </p>
            </div>

        </c:if>

        <c:if test="${not empty reviews}">
            <!-- HORIZONTAL LIST -->
            <section class="container mt-4 p-0">
                <c:if test="${note eq null}">
                    <c:url var="searchUrl" value="${baseUrl}/user/${userId}/reviews"/>
                </c:if>
                <c:if test="${note ne null}">
                    <c:url var="searchUrl" value="${baseUrl}/notes/${note.id}/reviews"/>
                </c:if>

                <form:form modelAttribute="searchForm"
                           action="${searchUrl}"
                           method="get"
                           id="searchForm">
                    <form:hidden path="pageNumber" id="pageNumber" value="1"/>

                    <c:if test="${note eq null}">
                        <h3>
                            <c:if test="${owner.userId ne user.userId}">
                                <spring:message code="reviews.user.title" arguments="${owner.displayName}"/>
                            </c:if>
                            <c:if test="${owner.userId eq user.userId}">
                                <spring:message code="reviews.myReviews"/>
                            </c:if>


                        </h3>
                    </c:if>
                    <c:if test="${note ne null}">
                        <h3>
                            <spring:message code="reviews.note.title" arguments="${note.name}"/>
                        </h3>
                    </c:if>

                    <div class="d-flex justify-content-between my-2">
                        <c:if test="${note eq null}">
                            <h6>
                                <spring:message code="profileNotes.averageScore"/>: <c:out value="${ownerScore}"/>⭐
                            </h6>
                        </c:if>
                        <c:if test="${note ne null}">
                            <h6>
                                <spring:message code="score"/>:
                                <fmt:formatNumber type="number" maxFractionDigits="1" value="${note.avgScore}"/> ⭐
                            </h6>
                        </c:if>

                        <div data-bs-toggle="tooltip" data-bs-placement="right"
                             data-bs-title="<spring:message code="search.button.pageSize"/>"
                             data-bs-trigger="hover">
                            <form:select path="pageSize" class="form-select bg-bg"
                                         onchange="document.getElementById('searchForm').submit()">
                                <form:option value="5">5</form:option>
                                <form:option value="10">10</form:option>
                                <form:option value="15">15</form:option>
                                <c:if test="${searchForm.pageSize ne 5 and searchForm.pageSize ne 10 and searchForm.pageSize ne 15}">
                                    <form:option value="${searchForm.pageSize}"><c:out
                                            value="${searchForm.pageSize}"/></form:option>
                                </c:if>
                            </form:select>
                        </div>
                    </div>
                </form:form>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <c:if test="${note eq null}">
                                <th class="col-3"><spring:message code="note"/></th>
                                <th class="col-3"><spring:message code="username"/></th>
                                <th class="col-1"><spring:message code="score"/></th>
                                <th class="col-5"><spring:message code="reviews.comment"/></th>
                            </c:if>
                            <c:if test="${note ne null}">
                                <th class="col-3"><spring:message code="username"/></th>
                                <th class="col-1"><spring:message code="score"/></th>
                                <th class="col-8"><spring:message code="reviews.comment"/></th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="review" items="${reviews}">
                            <tr class="note-found no-select" id="<c:out value="${review.user.userId}"/>" style="height: 57px;">
                                <c:if test="${note eq null}">
                                    <td>
                                        <a class="link-info" href="${baseUrl}/notes/${review.note.id}">
                                            <c:out value="${review.note.name}"/>
                                        </a>
                                    </td>
                                </c:if>
                                <td>
                                    <a class="link-info" href="${baseUrl}/user/${review.user.userId}/note-board">
                                        <c:out value="${review.user.displayName}"/>
                                    </a>
                                </td>
                                <td>
                                    <fmt:formatNumber type="number" maxFractionDigits="1" value="${review.score}"/> ⭐
                                </td>
                                <td>
                                    <span style="white-space: initial; overflow-wrap: break-word">
                                        <c:out value="${review.content}"/>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </c:if>
        <!-- PAGINATION -->
        <c:if test="${maxPage gt 1}">
            <fragment:paging maxPage="${maxPage}" pageNumber="${currentPage}"/>
        </c:if>
    </section>
</main>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-HwwvtgBNo3bZJJLYd8oVXjrBZt8cqVSpeBNS5n7C8IVInixGAoxmnlMuBnhbgrkm"
        crossorigin="anonymous">
</script>

<script src="<c:url value="/js/crud-buttons.js"/>"></script>
<script src="<c:url value="/js/popups.js"/>"></script>
<script src="<c:url value="/js/notes.js"/>"></script>

