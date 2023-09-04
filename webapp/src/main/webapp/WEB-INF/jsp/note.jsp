<%--
  Created by IntelliJ IDEA.
  User: david
  Date: 4/9/23
  Time: 10:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Note</title>
</head>
<body>
    <h1>Note</h1>
    <object
        type="application/pdf"
        data="http://localhost:8080/notes/${noteId}/file"
        width="100%"
        height="80%">
    </object>

</body>
</html>
