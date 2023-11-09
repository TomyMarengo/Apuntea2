<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<%@ attribute name="user" required="false" type="ar.edu.itba.paw.models.user.User" %>
<%@ attribute name="active" required="false" %>

<aside class="sidebar right">
    <c:if test="${user ne null}">

        <c:if test="${active eq 'note-board'}">
            <a class="btn nav-icon-button active" href="${baseUrl}/user/${user.userId}/note-board" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="myNotes.title"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/my-notes.svg"/>"
                     alt="<spring:message code="myNotes.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>
        <c:if test="${active ne 'note-board'}">
            <a class="btn nav-icon-button" href="${baseUrl}/user/${user.userId}/note-board" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="myNotes.title"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/my-notes.svg"/>"
                     alt="<spring:message code="myNotes.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>

        <c:if test="${active eq 'my-career'}">
            <a class="btn nav-icon-button active" href="${baseUrl}/my-career" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<c:out value="${user.career.name}"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/book-alt.svg"/>"
                     alt="<spring:message code="myCareer.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>
        <c:if test="${active ne 'my-career'}">
            <a class="btn nav-icon-button" href="${baseUrl}/my-career" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<c:out value="${user.career.name}"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/book-alt.svg"/>"
                     alt="<spring:message code="myCareer.title"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>

        <c:if test="${active eq 'my-favorites'}">
            <a class="btn nav-icon-button active" href="${baseUrl}/my-favorites" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="profileNotes.directories.favorites"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/filled-heart.svg"/>"
                     alt="<spring:message code="profileNotes.directories.favorites"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>
        <c:if test="${active ne 'my-favorites'}">
            <a class="btn nav-icon-button" href="${baseUrl}/my-favorites" data-bs-toggle="tooltip"
               data-bs-placement="right"
               data-bs-title="<spring:message code="profileNotes.directories.favorites"/>" data-bs-trigger="hover">
                <img src="<c:url value="/svg/filled-heart.svg"/>"
                     alt="<spring:message code="profileNotes.directories.favorites"/>"
                     class="icon-m fill-dark-primary"/>
            </a>
        </c:if>

        <c:if test="${user.isAdmin eq true}">
            <c:if test="${active eq 'manage-users'}">
                <a class="btn nav-icon-button active" href="${baseUrl}/manage/users" data-bs-toggle="tooltip"
                   data-bs-placement="right"
                   data-bs-title="<spring:message code="manageUsers.title"/>" data-bs-trigger="hover">
                    <img src="<c:url value="/svg/user-slash.svg"/>"
                         alt="<spring:message code="manageUsers.title"/>"
                         class="icon-m fill-dark-primary"/>
                </a>
            </c:if>
            <c:if test="${active ne 'manage-users'}">
                <a class="btn nav-icon-button" href="${baseUrl}/manage/users" data-bs-toggle="tooltip"
                   data-bs-placement="right"
                   data-bs-title="<spring:message code="manageUsers.title"/>" data-bs-trigger="hover">
                    <img src="<c:url value="/svg/user-slash.svg"/>"
                         alt="<spring:message code="manageUsers.title"/>"
                         class="icon-m fill-dark-primary"/>
                </a>
            </c:if>

            <c:if test="${active eq 'manage-careers'}">
                <a class="btn nav-icon-button active" href="${baseUrl}/manage/careers" data-bs-toggle="tooltip"
                   data-bs-placement="right"
                   data-bs-title="<spring:message code="manageCareers.title"/>" data-bs-trigger="hover">
                    <img src="<c:url value="/svg/books.svg"/>"
                         alt="<spring:message code="manageCareers.title"/>"
                         class="icon-m fill-dark-primary"/>
                </a>
            </c:if>
            <c:if test="${active ne 'manage-careers'}">
                <a class="btn nav-icon-button" href="${baseUrl}/manage/careers" data-bs-toggle="tooltip"
                   data-bs-placement="right"
                   data-bs-title="<spring:message code="manageCareers.title"/>" data-bs-trigger="hover">
                    <img src="<c:url value="/svg/books.svg"/>"
                         alt="<spring:message code="manageCareers.title"/>"
                         class="icon-m fill-dark-primary"/>
                </a>
            </c:if>
        </c:if>
    </c:if>

    <script src="<c:url value="/js/sidebar.js"/>"></script>
</aside>