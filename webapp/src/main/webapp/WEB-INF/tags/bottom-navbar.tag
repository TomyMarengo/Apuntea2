<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="title" required="false" %>
<%@ attribute name="hierarchy" required="false" type="ar.edu.itba.paw.models.directory.DirectoryPath" %>
<%@ attribute name="category" required="false" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<%@ attribute name="user" required="false" type="ar.edu.itba.paw.models.user.User" %>
<%@ attribute name="owner" required="false" type="ar.edu.itba.paw.models.user.User" %>
<%@ attribute name="directory" required="false" type="ar.edu.itba.paw.models.directory.Directory" %>
<%@ attribute name="reviewsSection" required="false"%>


<c:if test="${not empty title}">
    <c:set var="titleData" value="${fn:split(title, ',')}"/>
</c:if>

<c:set value="?userId=${owner ne null ? owner.userId : ''}" var="filterUserParam"/>

<div class="bottom-navbar">
    <c:if test="${owner ne null and category ne null}">
        <a href="${baseUrl}/user/${owner.userId}/note-board" class="bottom-navbar-item bn-title">
            <c:out value="${owner.displayName}"/>
        </a>
        <img src="<c:url value="/svg/vertical-line.svg"/>" class="icon-m dropdown-icon fill-bg"
             alt="dropdown"/>
    </c:if>
    <c:if test="${hierarchy ne null and (hierarchy.length > 1 or category ne 'directory')}">
        <div class="d-none d-lg-flex align-items-center">
            <a href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}${filterUserParam}"/>">
                <div class="bottom-navbar-item bn-title">
                    <c:out value="${hierarchy.rootDirectory.name}"/>
                </div>
            </a>
            <c:if test="${hierarchy.length gt 3}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg"
                     alt="dropdown"/>
                <div class="bottom-navbar-item">
                    <spring:message code="navbar.bottom.ellipsis"/>
                </div>
            </c:if>
            <c:if test="${hierarchy.length gt 2}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg"
                     alt="dropdown"/>
                <a href="<c:url value="${baseUrl}/directory/${hierarchy.parentDirectory.id}${filterUserParam}"/>">
                    <div class="bottom-navbar-item overflow-hidden">
                        <c:out value="${hierarchy.parentDirectory.name}"/>
                    </div>
                </a>
            </c:if>
            <c:if test="${category ne 'directory' and hierarchy.length gt 1}">
                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg"
                     alt="dropdown"/>
                <a href="<c:url value="${baseUrl}/directory/${hierarchy.currentDirectory.id}${filterUserParam}"/>">
                    <div class="bottom-navbar-item overflow-hidden">
                        <c:out value="${hierarchy.currentDirectory.name}"/>
                    </div>
                </a>
            </c:if>

            <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg"
                 alt="dropdown"/>
        </div>

        <div class="dropdown d-lg-none d-xl-none d-xxl-none">
            <div class="d-flex justify-content-between align-items-center">
                <button type="button" class="bn-dropdown-hierarchy-button dropdown-toggle nav-icon-button"
                        data-bs-toggle="dropdown"
                        aria-expanded="false">
                    <spring:message code="navbar.bottom.middleEllipsis"/>
                </button>

                <div class="dropdown-menu">
                    <c:if test="${owner ne null}">
                        <li>
                            <a href="${baseUrl}/user/${owner.userId}/note-board" class="dropdown-item">
                                <c:out value="${owner.displayName}"/>
                            </a>
                        </li>
                    </c:if>
                    <li>
                        <a class="dropdown-item"
                           href="<c:url value="${baseUrl}/directory/${hierarchy.rootDirectory.id}${filterUserParam}"/>">
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
                               href="<c:url value="${baseUrl}/directory/${hierarchy.parentDirectory.id}${filterUserParam}"/>">
                                <c:out value="${hierarchy.parentDirectory.name}"/>
                            </a>
                        </li>
                    </c:if>

                    <c:if test="${category ne 'directory' and hierarchy.length gt 1}">
                        <li>
                            <a class="dropdown-item"
                               href="<c:url value="${baseUrl}/directory/${hierarchy.currentDirectory.id}${filterUserParam}"/>">
                                <c:out value="${hierarchy.currentDirectory.name}"/>
                            </a>
                        </li>
                    </c:if>
                </div>

                <img src="<c:url value="/svg/arrow-right.svg"/>" class="mx-2 icon-s dropdown-icon fill-bg"
                     alt="dropdown"/>
            </div>
        </div>
    </c:if>

    <c:if test="${title != null}">
        <!-- COMMON -->
        <c:if test="${not (owner ne null and empty category) and category ne 'directory' and directory.parentId eq null}">
            <a href="<c:url value="${titleData[0]}"/>">
                <div class="bottom-navbar-item bn-title active">
                    <c:out value="${titleData[1]}"/>
                </div>
            </a>
        </c:if>
        <!-- FOR DIRECTORY PAGE -->
        <c:if test="${category eq 'directory'}">
            <div class="btn-group h-100">
                <c:if test="${directory.parentId eq null or user eq null or user.userId ne directory.user.userId}">
                    <div class="d-none d-lg-flex align-items-center">
                            <span class="d-block bottom-navbar-item bn-title active">
                                <c:out value="${titleData[1]}"/>
                            </span>
                    </div>
                </c:if>
                <c:if test="${directory.user ne null and user ne null and user.userId eq directory.user.userId}">
                    <button class="bn-dropdown-title-button"
                            data-bs-toggle="dropdown" aria-expanded="false">
                            <span class="d-block bottom-navbar-item bn-title active">
                                <c:out value="${titleData[1]}"/>
                            </span>
                        <img src="<c:url value="/svg/chevron-down.svg"/>" class="icon-s dropdown-icon fill-bg"
                             alt="dropdown"/>
                    </button>
                    <ul class="dropdown-menu">
                        <hr class="p-0 m-0">
                        <li>
                            <button class="dropdown-item d-flex gap-2 align-items-center justify-content-center"
                                    data-bs-toggle="modal" data-bs-target="#editDirectoryModal"
                                    onclick="editBNavDirectory('<c:out value="${directory.id}"/>', '<c:out
                                            value="${directory.name}"/>',
                                            '<c:out value="${directory.visible}"/>', '<c:out
                                            value="${directory.iconColor}"/>',
                                            '<c:out value="${directory.parentId}"/>')">
                                <img src="<c:url value="/svg/pencil.svg"/>"
                                     alt="<spring:message code="edit"/>"
                                     class="icon-xs fill-text">
                                <span><spring:message code="editDirectory"/></span>
                            </button>
                        </li>
                        <hr class="p-0 m-0">
                    </ul>
                </c:if>
            </div>
        </c:if>

        <!-- FOR NOTE-BOARD PAGE -->
        <c:if test="${owner ne null and empty category}">
            <div class="bottom-navbar-item bn-title active d-flex align-items-center">
                <a href="<c:url value="${titleData[0]}"/>" class="text-dark-bg">
                        ${titleData[1]}
                </a>
                <c:if test="${not empty reviewsSection}">
                    <img src='${baseUrl}/svg/vertical-line.svg' alt="<spring:message code="separator"/>" class='icon-m dropdown-icon fill-bg'/>
                    <spring:message code="reviews"/>
                </c:if>
            </div>
        </c:if>

    </c:if>
</div>
