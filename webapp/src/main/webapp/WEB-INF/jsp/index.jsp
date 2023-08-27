<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" href="./css/main.css">
</head>
<body>
<h2>Hello <c:out value="${user.email}" escapeXml="true"/>!</h2>
</body>
</html>