<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="hierarchy" required="false" type="ar.edu.itba.paw.models.directory.DirectoryPath" %>
<%@ attribute name="category" required="false" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<%@ attribute name="user" required="false" type="ar.edu.itba.paw.models.user.User" %>
<%@ attribute name="blob" required="false" type="ar.edu.itba.paw.models.directory.Directory" %>

<div class="bottom-navbar">
    <c:if test="${hierarchy ne null and (hierarchy.length > 1 or category ne 'directory')}">
        <div class="d-none d-lg-flex align-items-center">

            <a href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}"/>">
                <div class="bottom-navbar-item bn-title">
                    <c:out value="${hierarchy.rootDirectory.name}"/>
                </div>
            </a>
            <c:if test="${hierarchy.length gt 3}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg" alt="dropdown"/>
                <div class="bottom-navbar-item">
                    <spring:message code="navbar.bottom.ellipsis"/>
                </div>
            </c:if>
            <c:if test="${hierarchy.length gt 2}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg" alt="dropdown"/>
                <a href="<c:url value="${baseUrl}/directory/${hierarchy.parentDirectory.id}"/>">
                    <div class="bottom-navbar-item overflow-hidden">
                        <c:out value="${hierarchy.parentDirectory.name}"/>
                    </div>
                </a>
            </c:if>

            <c:if test="${category ne 'directory' and hierarchy.length gt 1}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg" alt="dropdown"/>
                <a href="<c:url value="${baseUrl}/directory/${hierarchy.currentDirectory.id}"/>">
                    <div class="bottom-navbar-item overflow-hidden">
                        <c:out value="${hierarchy.currentDirectory.name}"/>
                    </div>
                </a>
            </c:if>

            <c:if test="${title != null}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg" alt="dropdown"/>
            </c:if>

        </div>

        <div class="dropdown d-lg-none d-xl-none d-xxl-none">
            <div class="d-flex justify-content-between align-items-center">
                <button type="button" class="bn-dropdown-hierarchy-button dropdown-toggle nav-icon-button" data-bs-toggle="dropdown"
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

                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg" alt="dropdown"/>

            </div>
        </div>
    </c:if>

    <c:if test="${title != null}">
        <c:set var="titleData" value="${fn:split(title, ':')}"/>
        <c:if test="${category ne 'directory' or user.userId ne blob.user.userId}">
            <a href="<c:url value="${titleData[0]}"/>">
                <div class="bottom-navbar-item bn-title active">
                    <c:out value="${titleData[1]}"/>
                </div>
            </a>
        </c:if>
        <c:if test="${category eq 'directory' and user.userId eq blob.user.userId}">
            <div class="btn-group h-100">
                <button class="bn-dropdown-title-button"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <span class="d-block bottom-navbar-item bn-title active">
                        <c:out value="${titleData[1]}"/>
                    </span>
                    <img src="<c:url value="/svg/chevron-down.svg"/>" class="icon-s dropdown-icon fill-bg" alt="dropdown"/>
                </button>
                <ul class="dropdown-menu">
                    <hr class="p-0 m-0">
                    <li>
                        <button class="dropdown-item d-flex gap-2 align-items-center justify-content-center"
                                data-bs-toggle="modal" data-bs-target="#editDirectoryModal"
                                onclick="editBNavDirectory('<c:out value="${blob.id}"/>', '<c:out value="${blob.name}"/>',
                                        '<c:out value="${blob.visible}"/>', '<c:out value="${blob.iconColor}"/>',
                                        '<c:out value="${blob.parentId}"/>')">
                            <img src="<c:url value="/svg/pencil.svg"/>"
                                 alt="<spring:message code="edit"/>"
                                 class="icon-xs fill-text">
                            <span><spring:message code="editDirectory"/></span>
                        </button>
                    </li>
                    <hr class="p-0 m-0">
                </ul>
            </div>
        </c:if>
    </c:if>
</div>
