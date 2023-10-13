<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="hierarchy" required="false" type="ar.edu.itba.paw.models.directory.DirectoryPath" %>
<%@ attribute name="category" required="false" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>

<div class="bottom-navbar">
    <c:if test="${hierarchy ne null and (hierarchy.length > 1 or category ne 'directory')}">
        <div class="d-none d-lg-flex">

            <a href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}"/>">
                <div class="bottom-navbar-item bn-title">
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

            <c:if test="${category ne 'directory' and hierarchy.length gt 1}">
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

        </div>

        <div class="dropdown d-lg-none d-xl-none d-xxl-none">
            <div class="d-flex justify-content-between align-items-center">
                <button type="button" class="bottom-navbar-dropdown-button dropdown-toggle nav-icon-button" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    <spring:message code="navbar.bottom.middleEllipsis"/>
                </button>

                <div class="dropdown-menu">
                    <li>
                        <a class="dropdown-item" href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}"/>">
                            <c:out value="${hierarchy.rootDirectory.name}"/>
                        </a>
                    </li>

                    <c:if test="${hierarchy.length gt 3}">
                        <li class="dropdown-item disabled">
                            <spring:message code="navbar.bottom.ellipsis"/>
                        </li>
                    </c:if>

                    <c:if test="${hierarchy.length gt 2}">
                        <li>
                            <a class="dropdown-item"
                               href="<c:url value="${baseUrl}/directory/${hierarchy.parentDirectory.id}"/>">
                                <c:out value="${hierarchy.parentDirectory.name}"/>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${category ne 'directory' and hierarchy.length gt 1}">
                        <li>
                            <a class="dropdown-item" href="<c:url value="${baseUrl}/directory/${hierarchy.currentDirectory.id}"/>">
                                    <c:out value="${hierarchy.currentDirectory.name}"/>
                            </a>
                        </li>
                    </c:if>
                </div>

                <div class="bottom-navbar-separator overflow-hidden">
                    <spring:message code="navbar.bottom.separator"/>
                </div>

            </div>
        </div>
    </c:if>

    <c:if test="${title != null}">
        <c:set var="titleData" value="${fn:split(title, ':')}"/>
        <a href="<c:url value="${titleData[0]}"/>">
            <div class="bottom-navbar-item bn-title active">
                <c:out value="${titleData[1]}"/>
            </div>
        </a>
    </c:if>
</div>