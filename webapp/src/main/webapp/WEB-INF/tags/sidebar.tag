<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<%@ attribute name="user" required="false" type="ar.edu.itba.paw.models.user.User" %>

<aside class="sidebar right">
    <c:if test="${user ne null}">
        <a class="btn nav-icon-button" href="${baseUrl}/my-favorites" data-bs-toggle="tooltip"
           data-bs-placement="right"
           data-bs-title="<spring:message code="profileNotes.directories.favorites"/>" data-bs-trigger="hover">
            <img src="<c:url value="/svg/filled-heart.svg"/>"
                 alt="<spring:message code="profileNotes.directories.favorites"/>"
                 class="icon-m fill-dark-primary"/>
        </a>

        <a class="btn nav-icon-button" href="${baseUrl}/my-career" data-bs-toggle="tooltip"
           data-bs-placement="right"
           data-bs-title="<spring:message code="myCareer.title"/>" data-bs-trigger="hover">
            <img src="<c:url value="/svg/book-alt.svg"/>"
                 alt="<spring:message code="myCareer.title"/>"
                 class="icon-m fill-dark-primary"/>
        </a>

        <a class="btn nav-icon-button" href="${baseUrl}/my-notes" data-bs-toggle="tooltip"
           data-bs-placement="right"
           data-bs-title="<spring:message code="myNotes.title"/>" data-bs-trigger="hover">
            <img src="<c:url value="/svg/my-notes.svg"/>"
                 alt="<spring:message code="myNotes.title"/>"
                 class="icon-m fill-dark-primary"/>
        </a>

        <c:if test="${user.isAdmin eq true}">
            <a class="btn nav-icon-button" href="${baseUrl}/manage/users" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="manageUsers.title"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/user-slash.svg"/>"
                     alt="<spring:message code="manageUsers.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>

            <a class="btn nav-icon-button" href="${baseUrl}/manage/careers" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="manageCareers.title"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/books.svg"/>"
                     alt="<spring:message code="manageCareers.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>

        </c:if>
    </c:if>

    <script src="<c:url value="/js/sidebar.js"/>"></script>
</aside>