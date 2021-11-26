<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ taglib prefix="c" uri="http://xmlns.jcp.org/jsp/jstl/core" %>--%>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>--%>

    <%--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"--%>
    <%--type="text/css">--%>
    <%--<link rel="stylesheet" href="https://static.pingendo.com/bootstrap/bootstrap-4.2.1.css">--%>
    <jsp:include page="libs.jsp"/>

</head>

<body>

<div class="py-1">
    <div class="container-fluid">
        <jsp:include page="head.jsp"/>


        <div class="row">
            <jsp:include page="menu.jsp"/>
            <div class="col-md-10" style="">
                <div class="row">
                    <div class="col-md-3">
                        <c:if test="${user_id!=-1}">
                            <form:form id="addTask" action="${pageContext.request.contextPath}/task/addtask"
                                       method="get">
                                <button type="submit" class="btn btn-primary">Создать</button>
                                <input type="hidden" name="user_id" value="${user_id}"/>
                                <input type="hidden" name="page" value="${page}"/>

                            </form:form>
                        </c:if>
                    </div>
                    <div class="col-md-9 border-right">
                        <div class="dropdown">
                            <button class="btn btn-primary dropdown-toggle float-right" type="button" id="dropdownMenuButton"
                                    data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                ${filter_user_name}
                            </button>
                            <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <a class="dropdown-item"
                                   href="${pageContext.request.contextPath}/task/tasklist?user_id=${user_id}&page=${page}&filter_user_id=-1">Сбросить</a>
                                <div class="dropdown-divider"></div>

                                <c:forEach var="user" items="${userListFilter}">
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/task/tasklist?user_id=${user_id}&page=${page}&filter_user_id=${user.id}">${user.fullName}</a>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="table">

                            <table class="table table-sm table-hover">
                                <thead>
                                <th class="w-12">#</th>
                                <th class="w-18">Дата</th>
                                <th class="w-55">Наименование</th>
                                <th class="w-18">Срок</th>
                                <th class="w-15">${(user_id==-1)?'Исполнитель':'Заказчик'}</th>
                                </thead>
                                <tbody>


                                <c:forEach var="task" items="${taskList}">
                                    <tr onclick="window.location.href='${pageContext.request.contextPath}/task/showtask?task_id=${task.id}&user_id=${user_id}&page=${page}'; return false;" class=' ${task.isDone?"text-success":"text-danger"} ${task.isViewed?"":"font-weight-bold"}'>
                                        <td>${task.id}</td>
                                        <td>${f:formatLocalDateTime(task.dateBegin, 'dd.MM.yyyy HH:mm:ss')}</td>
                                        <td>${task.title}</td>
                                        <td>${f:formatLocalDateTime(task.dateEnd, 'dd.MM.yyyy HH:mm:ss')}</td>

                                        <td>${(user_id==-1)?task.responsible.fullName:task.createdBy.fullName}</td>

                                    </tr>

                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <ul class="pagination">

                            <li class="page-item"><a class="page-link"
                                                     href="${pageContext.request.contextPath}/task/tasklist?user_id=${user_id}&page=${pageList[0]}">Prev</a>
                            </li>
                            <c:forEach begin="1" end="5" varStatus="loop">
                                <li class="page-item ${(pageList[loop.index]==page)?'active':''}"><a class="page-link"
                                                                                                     href="${pageContext.request.contextPath}/task/tasklist?user_id=${user_id}&page=${pageList[loop.index]}">${pageList[loop.index]}</a>
                                </li>
                            </c:forEach>
                            <li class="page-item"><a class="page-link"
                                                     href="${pageContext.request.contextPath}/task/tasklist?user_id=${user_id}&page=${pageList[6]}">Next</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>

</html>