<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="col-md-2 p-0 pl-0" >

    <ul class="list-group-flush leftContainer" >
        <a href="${pageContext.request.contextPath}/task/tasklist?user_id=-1&page=1"
           class="list-group-item list-group-item-action list-group-item-info d-flex justify-content-between align-items-center list-group-item-action">
            Свои задачи</a>

        <c:forEach var="user" items="${userList}">
            <c:if test="${user.id == user_id}">
                <a href="${pageContext.request.contextPath}/task/tasklist?user_id=${user.id}&page=1"
                   class="list-group-item d-flex justify-content-between align-items-center list-group-item-action active">${user.fullName }
                    <span class="badge badge-primary badge-pill">${user.numActiveTask}</span></a>
            </c:if>
            <c:if test="${user.id != user_id}">
                <a href="${pageContext.request.contextPath}/task/tasklist?user_id=${user.id}&page=1"
                   class="list-group-item d-flex justify-content-between align-items-center list-group-item-action">${user.fullName}
                    <span class="badge badge-primary badge-pill">${user.numActiveTask}</span></a>
            </c:if>

        </c:forEach>
    </ul>
</div>

