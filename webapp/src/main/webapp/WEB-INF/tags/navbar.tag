<%--suppress HtmlUnknownTarget --%>
<%--suppress JspAbsolutePathInspection --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ tag pageEncoding="UTF-8"%>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl" />
<spring:message var="profile" code="profile.title"/>
<spring:message var="notifications" code="notifications"/>
<spring:message var="darkMode" code="darkMode"/>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid d-flex align-items-center">
        <a class="navbar-brand text-dark-primary" href="${baseUrl}">Apuntea</a>
        <div class="d-flex justify-content-center align-items-center mx-1" >
            <!--<button class="btn nav-icon-button" type="button">
                <img src="/svg/bell.svg" alt="${notifications}" class="icon-s fill-dark-primary" />
            </button>-->
            <button id="darkModeToggle" class="btn nav-icon-button" type="button">
                <img id="darkModeIcon" src="<c:url value="/svg/sun.svg"/>" alt="${darkMode}" class="icon-s fill-dark-primary" />
            </button>
            <div class="btn-group mx-2">
                <button type="button" class="btn button-primary nav-user-circle d-flex align-items-center justify-content-center"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="<c:url value="/svg/user.svg"/>" alt="${profile}" class="icon-s fill-bg" />
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li><a class="dropdown-item" href="${baseUrl}/profile"><spring:message code="profile.title"/></a></li>
                    <!--<li><a class="dropdown-item" href="${baseUrl}/settings"><spring:message code="settings.title"/></a></li>-->
                    <li><a class="dropdown-item" href="${baseUrl}/logout"><spring:message code="logout"/></a></li>
                </ul>
            </div>
        </div>
    </div>
</nav>
