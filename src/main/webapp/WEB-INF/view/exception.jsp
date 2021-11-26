 <%@ page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<jsp:include page="head.jsp"/>

<body>

    <div class="container text-center">
        <br>
        <h4 class="my-3">${status}</h4>
        <h2>${typeMessage}</h2>
        <h4 class="my-5">${message}</h4>
    </div>
<!--
<c:forEach items="${exception.stackTrace}" var="stackTrace">
    ${stackTrace}
</c:forEach>
-->
</body>
</html>