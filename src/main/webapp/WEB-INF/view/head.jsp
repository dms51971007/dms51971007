<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="row">
    <div class="col pl-5">
        <nav class="navbar navbar-light bg-faded">

            <a class="navbar-brand" href="#">Manager.Signal-M Пользователь: ${auth_user.fullName}</a>
            <ul class="nav justify-content-end">
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/project/?user_id=-1&page=1">Цеха</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="${pageContext.request.contextPath}/task/tasklist?user_id=-1&page=1">Задачи</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/user/list">Пользователи</a>
                </li>
                <li class="nav-item">
                    <form:form action="${pageContext.request.contextPath}/logout"
                               method="POST">
                        <input type="submit" class="btn btn-primary" value="Logout" />
                    </form:form>
                </li>
            </ul>
        </nav>
    </div>
</div>

