<%--suppress HtmlUnknownTarget --%>
<%--suppress JspAbsolutePathInspection --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ tag pageEncoding="UTF-8" %>
<spring:eval expression="@environment.getProperty('base.url')" var="baseUrl"/>
<spring:message var="profile" code="profile.title"/>
<spring:message var="search" code="search.title"/>
<spring:message var="notifications" code="notifications"/>
<spring:message var="darkMode" code="darkMode"/>
<spring:message var="manage" code="manage"/>
<%@ attribute name="user" required="false" type="ar.edu.itba.paw.models.user.User" %>
<c:set var="loggedIn" value="${user ne null}"/>
<c:if test="${user ne null}">
    <c:set var="isAdmin" value="${user.isAdmin}"/>
</c:if>

<script>
    (function () {
        const storedTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-bs-theme', storedTheme);
        localStorage.setItem('theme', storedTheme);
    })();
</script>

<nav class="navbar py-0 px-3">
    <a class="navbar-brand text-dark-primary" href="${baseUrl}">Apuntea</a>
    <div class="d-flex justify-content-center align-items-center gap-2">
        <div class="search-container">

            <a href="${baseUrl}/search?institutionId=${user.institution.institutionId}&careerId=${user.career.careerId}"
               class="btn nav-icon-button search-icon" id="searchNavButton">
                <img src="<c:url value="/svg/search.svg"/>" alt="${search}" class="icon-s fill-dark-primary"/>
            </a>
            <input id="searchNavInput" type="text" class="search-input"
                   placeholder="<spring:message code="search.word.placeholder"/>"/>
        </div>


        <button id="darkModeToggle" class="btn nav-icon-button" type="button">
            <img id="darkModeIcon" src="<c:url value="/svg/sun.svg"/>" alt="${darkMode}"
                 class="icon-s fill-dark-primary"/>
        </button>

        <c:if test="${loggedIn}">
            <div class="btn-group">
                <button class="nav-user-button"
                        data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="<c:url value="/svg/user.svg"/>" alt="${profile}"
                         class="icon-s"/>
                    <img src="<c:url value="/svg/chevron-down.svg"/>" alt="${profile}"
                         class="icon-s fill-dark-primary dropdown-icon"/>
                </button>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <div class="d-flex flex-column dropdown-item disabled my-2">
                            <span class="logged-in"><spring:message code="navbar.loggedIn"/></span>
                            <strong><c:out value="${user.displayName}"/></strong>
                        </div>
                    </li>

                    <hr class="p-0">

                    <li><a class="dropdown-item" href="${baseUrl}/profile"><spring:message
                            code="myProfile.title"/></a></li>

                    <li><a class="dropdown-item" href="${baseUrl}/change-password"><spring:message
                            code="changePassword.title"/></a></li>

                    <hr class="p-0">

                    <li><a class="dropdown-item my-2" href="${baseUrl}/logout"><spring:message
                            code="logout"/></a></li>
                </ul>
            </div>
        </c:if>

        <c:if test="${!loggedIn}">
            <a href="${baseUrl}/login" class="btn rounded-box-no-shadow button-primary mx-2">
                <spring:message code="login"/>
            </a>
            <a href="${baseUrl}/register" class="btn rounded-box-no-shadow login-register-button">
                <spring:message code="register"/>
            </a>
        </c:if>
    </div>

</nav>

<script src="<c:url value="/js/darkmode.js"/>"></script>
<script src="<c:url value="/js/global-search.js"/>"></script>