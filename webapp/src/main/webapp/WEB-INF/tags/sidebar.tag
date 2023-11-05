<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<aside class="sidebar right">
    <a class="btn nav-icon-button" href="${baseUrl}/manage/careers" data-bs-toggle="tooltip"
       data-bs-placement="right"
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
    <script src="<c:url value="/js/sidebar.js"/>"></script>
</aside>