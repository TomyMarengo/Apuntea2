<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="extraLinks" required="true" %>

<div class="bottom-navbar">
    <c:set var="titleData" value="${fn:split(title, ':')}" />
    <a href="${titleData[0]}">
        <div class="bottom-navbar-item active">${titleData[1]}</div>
    </a>
    <div class="d-flex gap-4">
        <c:forEach var="link" begin="0" items="${extraLinks}">
            <c:set var="linkData" value="${fn:split(link, ':')}" />
            <a href="${linkData[0]}">
                <div class="bottom-navbar-item">${linkData[1]}</div>
            </a>
        </c:forEach>
    </div>
</div>
