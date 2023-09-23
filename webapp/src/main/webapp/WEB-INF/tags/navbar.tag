<%--suppress HtmlUnknownTarget --%>
<%--suppress JspAbsolutePathInspection --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ tag pageEncoding="UTF-8"%>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl" />
<spring:message var="profile" code="profile"/>
<spring:message var="notifications" code="notifications"/>
<spring:message var="darkMode" code="darkMode"/>

<nav class="navbar navbar-expand-lg">
    <div class="container-fluid d-flex align-items-center">
        <a class="navbar-brand text-dark-primary" href="${baseUrl}">Apuntea</a>
        <div class="d-flex justify-content-center align-items-center" >
            <!--<button class="btn nav-icon-button" type="button">
                <img src="/svg/bell.svg" alt="${notifications}" class="icon-s fill-dark-primary" />
            </button>-->
            <button id="darkModeToggle" class="btn nav-icon-button" type="button">
                <img id="darkModeIcon" src="<c:url value="/svg/sun.svg"/>" alt="${darkMode}" class="icon-s fill-dark-primary" />
            </button>
            <a href="${baseUrl}/profile" class="mx-2">
                <button class="btn button-primary nav-user-circle d-flex align-items-center justify-content-center" type="button">
                    <img src="<c:url value="/svg/user.svg"/>" alt="${profile}" class="icon-s fill-bg" />
                </button>
            </a>
        </div>
    </div>
</nav>
