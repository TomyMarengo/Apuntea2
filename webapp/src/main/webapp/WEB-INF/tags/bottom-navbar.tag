<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="hierarchy" required="false" type="ar.edu.itba.paw.models.DirectoryPath" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<div class="bottom-navbar">

    <c:if test="${hierarchy ne null}">
        <a href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}"/>">
            <div class="bottom-navbar-item overflow-hidden">
                <c:out value="${hierarchy.rootDirectory.name}"/>
            </div>
        </a>
        <c:if test="${hierarchy.length gt 3}">
            <div class="bottom-navbar-separator overflow-hidden">
                <spring:message code="navbar.bottom.separator"/>
            </div>
            <div class="bottom-navbar-item overflow-hidden">
                <spring:message code="navbar.bottom.ellipsis"/>
            </div>
        </c:if>
        <c:if test="${hierarchy.length gt 2}">
            <div class="bottom-navbar-separator overflow-hidden">
                <spring:message code="navbar.bottom.separator"/>
            </div>
            <a href="<c:url value="${baseUrl}/directory/${hierarchy.parentDirectory.id}"/>">
                <div class="bottom-navbar-item overflow-hidden">
                    <c:out value="${hierarchy.parentDirectory.name}"/>
                </div>
            </a>
        </c:if>
        <c:if test="${hierarchy.length gt 1}">
            <div class="bottom-navbar-separator overflow-hidden">
                <spring:message code="navbar.bottom.separator"/>
            </div>
            <a href="<c:url value="${baseUrl}/directory/${hierarchy.currentDirectory.id}"/>">
                <div class="bottom-navbar-item overflow-hidden">
                    <c:out value="${hierarchy.currentDirectory.name}"/>
                </div>
            </a>
        </c:if>
        <c:if test="${title != null}">
            <div class="bottom-navbar-separator overflow-hidden">
                <spring:message code="navbar.bottom.separator"/>
            </div>
        </c:if>
    </c:if>

    <c:if test="${title != null}">
        <c:set var="titleData" value="${fn:split(title, ':')}" />
        <a href="<c:url value="${titleData[0]}"/>">
            <div class="bottom-navbar-item overflow-hidden active">
                <c:out value="${titleData[1]}"/>
            </div>
        </a>
    </c:if>
</div>