<%--suppress HtmlUnknownTarget --%>
<%--suppress JspAbsolutePathInspection --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg">
    <div class="container-fluid d-flex align-items-center">
        <a class="navbar-brand text-dark-primary" href="/">Apuntea</a>
        <div class="d-flex justify-content-center align-items-center" style="height: 58px">
            <button class="btn nav-icon-button" type="button">
                <img src="../../svg/bell.svg" alt="Notifications" class="icon-s fill-dark-primary" />
            </button>
            <button id="darkModeToggle" class="btn nav-icon-button" type="button">
                <img id="darkModeIcon" src="../../svg/sun.svg" alt="Dark Mode" class="icon-s fill-dark-primary" />
            </button>
            <a href="profile">
                <button class="button-primary nav-icon-button nav-user-circle d-flex align-items-center justify-content-center" type="button">
                    <img src="../../svg/user.svg" alt="User" class="icon-s fill-bg" />
                </button>
            </a>
        </div>
    </div>
</nav>
