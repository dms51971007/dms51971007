<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="input" uri="http://www.springframework.org/tags/form" %>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <jsp:include page="libs.jsp"/>
    <script type="text/javascript">
        $(function () {
            $('#datetimepicker3').datetimepicker({
                locale: 'ru'
            });
        });
        $(function () {
            $('#datetimepicker2').datetimepicker({
                locale: 'ru'
            });
        });

    </script>

</head>

<body>


<div class="py-1">
    <div class="container-fluid">
        <jsp:include page="head.jsp"/>

        <div class="row">
            <jsp:include page="menu.jsp"/>
            <div class="col-md-9 " style="">
                <div class="col-md-12">

                    <form:form action="${pageContext.request.contextPath}/task/savetask"
                               modelAttribute="task_to" method="post">
                        <form:hidden path="id"/>
                        <form:hidden path="dateCreate"/>
                        <form:hidden path="dateComplete"/>

                        <input hidden name="user_id" id="user_id" value="${user_id}"/>
                        <input hidden name="page" id="page" value="${page}"/>
                        <input hidden name="responsible_id" id="responsible_id" value="${task_to.responsible.id}"/>
                        <input hidden name="isDone" id="isDone" value="${task_to.isDone}"/>
                        <input hidden name="isViewed" id="isViewed" value="${task_to.isViewed}"/>

                        <table class="table text-secondary">
                            <tr>
                                    <%--<td> Дата создания:</td>--%>
                                    <%--<td>--%>
                                    <%--&lt;%&ndash;<form:input path="dateCreate" class="form-control"/>&ndash;%&gt;--%>
                                    <%--<div class="input-group date" id="datetimepicker1" data-target-input="nearest">--%>
                                    <%--<form:input type="date" path="dateCreate"--%>
                                    <%--class="form-control datetimepicker-input"--%>
                                    <%--data-target="#datetimepicker1"/>--%>
                                    <%--<div class="input-group-append" data-target="#datetimepicker1"--%>
                                    <%--data-toggle="datetimepicker">--%>
                                    <%--<div class="input-group-text"><i class="fa fa-calendar"></i></div>--%>
                                    <%--</div>--%>
                                    <%--</div>--%>

                                    <%--</td>--%>
                                <td> Дата начала:</td>
                                <td>

                                    <div class="input-group date" id="datetimepicker2" data-target-input="nearest">
                                        <form:input type="text" class="form-control datetimepicker-input"
                                                    data-target="#datetimepicker2" path="dateBegin"/>
                                        <div class="input-group-append" data-target="#datetimepicker2"
                                             data-toggle="datetimepicker">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                    </div>


                                </td>
                                <td> Дата окончания:</td>
                                <td>
                                        <%--<form:input path="dateComplete" class="form-control"/>--%>
                                    <div class="input-group date" id="datetimepicker3" data-target-input="nearest">
                                        <form:input type="text" class="form-control datetimepicker-input"
                                                    data-target="#datetimepicker3" path="dateEnd"/>
                                        <div class="input-group-append" data-target="#datetimepicker3"
                                             data-toggle="datetimepicker">
                                            <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                        </div>
                                    </div>

                                </td>
                                    <%--<td> Дата завершения:</td>--%>
                                    <%--<td>--%>
                                    <%--&lt;%&ndash;<form:input path="dateEnd" class="form-control"/>&ndash;%&gt;--%>
                                    <%--<div class="input-group date" id="datetimepicker4" data-target-input="nearest">--%>
                                    <%--<form:input type="date" path="dateComplete"--%>
                                    <%--class="form-control datetimepicker-input"--%>
                                    <%--data-target="#datetimepicker4"/>--%>
                                    <%--<div class="input-group-append" data-target="#datetimepicker4"--%>
                                    <%--data-toggle="datetimepicker">--%>
                                    <%--<div class="input-group-text"><i class="fa fa-calendar"></i></div>--%>
                                    <%--</div>--%>
                                    <%--</div>--%>

                                    <%--</td>--%>
                            </tr>
                            <tr>
                                <td colspan="8">


                                </td>
                            </tr>
                            <tr>
                                <td>Заголовок:</td>
                                <td colspan="7"><form:input path="title" class="form-control"/></td>
                            </tr>
                            <tr>
                                <td> Примечание:</td>
                                <td colspan="7">
                                    <pre><form:textarea path="memo" rows="3" class="form-control"/></pre>
                                </td>
                            </tr>
                        </table>

                        <%--<tr>--%>
                        <%--<td colspan="2">Заказчик:</td>--%>
                        <%--<td colspan="2"><b>${task.createdBy.fullName}</b></td>--%>
                        <%--<td colspan="2">Исполнитель:</td>--%>
                        <%--<td colspan="2"><b>${task.responsible.fullName}</b></td>--%>
                        <%--<tr>--%>
                        <%--<td> Дата создания:</td>--%>
                        <%--<td><b>${f:formatLocalDateTime(task.dateCreate, 'dd.MM.yyyy HH:mm:ss')}</b></td>--%>
                        <%--<td> Дата начала:</td>--%>
                        <%--<td><b>${f:formatLocalDateTime(task.dateBegin, 'dd.MM.yyyy HH:mm:ss')}</b></td>--%>
                        <%--<td> Дата окончания:</td>--%>
                        <%--<td><b>${f:formatLocalDateTime(task.dateEnd, 'dd.MM.yyyy HH:mm:ss')}</b></td>--%>
                        <%--<td> Дата завершения:</td>--%>
                        <%--<td><b>${f:formatLocalDateTime(task.dateComplete, 'dd.MM.yyyy HH:mm:ss')}</b></td>--%>
                        <%--</tr>--%>

                        <%--<tr>--%>
                        <%--<td colspan="8"> Комментарии:</td>--%>
                        <%--</tr>--%>
                        <input type="submit" class="btn btn-primary"/>
                    </form:form>
                </div>

            </div>
        </div>
    </div>
</div>
</body>

</html>