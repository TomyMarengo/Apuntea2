<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title><c:out value="${note.name}"/></title>
</head>
<body>
    <h1><c:out value="${note.name}"/></h1>
    <p><c:out value="${note.avgScore}"/></p>
    <form:form action="/notes/${noteId}/review" method="post" modelAttribute="reviewForm">
        <form:input path="email"/>
        <form:select path="score">
            <form:option value="1">1</form:option>
            <form:option value="2">2</form:option>
            <form:option value="3">3</form:option>
            <form:option value="4">4</form:option>
            <form:option value="5">5</form:option>
        </form:select>
        <input type="submit"/>
    </form:form>

    <object
        type="application/pdf"
        data="http://localhost:8080/notes/${noteId}/download"
        width="100%"
        height="80%">
    </object>

</body>
</html>
