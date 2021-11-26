<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include page="libs.jsp"/>


    <script type="text/javascript">
        function displayModal(ref, user_id, task_id, page, memo_id) {
            document.getElementById("refDelete").action = ref;
            document.getElementById("del_user_id").value = user_id;
            document.getElementById("del_task_id").value = task_id;
            document.getElementById("del_page").value = page;
            document.getElementById("del_memo_id").value = memo_id;


        }
    </script>

</head>

<body>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Внимание!!</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                Удалить?
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

                <form:form id="refDelete" action="" method="get">
                    <button type="submit" class="btn btn-primary">Удалить</button>
                    <input type="hidden" name="user_id" id="del_user_id" value=""/>
                    <input type="hidden" name="task_id" id="del_task_id" value=""/>
                    <input type="hidden" name="page" id="del_page" value=""/>
                    <input type="hidden" name="memo_id" id="del_memo_id" value=""/>

                </form:form>
            </div>
        </div>
    </div>
</div>


<div class="py-1">
    <div class="container-fluid">
        <jsp:include page="head.jsp"/>

        <div class="row">
            <jsp:include page="menu.jsp"/>
            <div class="col-md-9 " style="">
                <table class="table text-secondary">
                    <tr class="text ${task_to.isDone?"text-success":"text-danger"}">
                        <td colspan="1">Заказчик:</td>
                        <td colspan="2"><b>${task_to.createdBy.fullName}</b></td>
                        <td colspan="1">Исполнитель:</td>
                        <td colspan="2"><b>${task_to.responsible.fullName}</b></td>
                        <td colspan="2"></b>
                            <c:if test="${task_to.createdBy.id == auth_user.id}">

                                <button type="button" class="btn btn-primary "
                                        onclick="location.href='${pageContext.request.contextPath}/task/edittask?user_id=${user_id}&task_id=${task_to.id}&page=${page}'">
                                    Изменить
                                </button>
                                <button type="button" class="btn btn-primary"
                                        onclick="displayModal('${pageContext.request.contextPath}/task/deletetask',${user_id},${task_to.id},${page},0)"
                                        data-toggle="modal" data-target="#exampleModal"
                                        data-myvalue="troudbal" data-bb="troudbal">Удалить
                                </button>
                            </c:if>

                            <c:if test="${task_to.responsible.id == auth_user.id}" >
                                <button type="button" class="btn ${task_to.isDone?"btn-danger":"btn-success"}"
                                        onclick="location.href='${pageContext.request.contextPath}/task/finishtask?user_id=${user_id}&task_id=${task_to.id}&page=${page}'">
                                        ${task_to.isDone?"Отменить":"Выполнить"}
                                </button>
                            </c:if>

                            <%--<button type="button" class="btn btn-primary"--%>
                            <%--onclick="location.href='${pageContext.request.contextPath}/edittask?user_id=${active_user}&task_id=${task_to.id}&page=${page}'">Удалить--%>
                            <%--</button>--%>

                        </td>
                    <tr>
                        <td><b>ID</b></td>
                    <td><b>${task_to.id}</b></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    </tr>

                    <tr>
                        <td> Дата создания:</td>
                        <td><b>${f:formatLocalDateTime(task_to.dateCreate, 'dd.MM.yyyy HH:mm:ss')}</b></td>
                        <td> Дата начала:</td>
                        <td><b>${f:formatLocalDateTime(task_to.dateBegin, 'dd.MM.yyyy HH:mm:ss')}</b></td>
                        <td> Дата окончания:</td>
                        <td><b>${f:formatLocalDateTime(task_to.dateEnd, 'dd.MM.yyyy HH:mm:ss')}</b></td>
                        <td> Дата завершения:</td>
                        <td><b>${f:formatLocalDateTime(task_to.dateComplete, 'dd.MM.yyyy HH:mm:ss')}</b></td>

                    </tr>

                    <tr>
                        <td>Заголовок:</td>
                        <td colspan="7"><b>${task_to.title}</b></td>
                    </tr>
                    <tr>
                        <td> Примечание:</td>
                        <td colspan="7">
                            <pre><b>${task_to.memo}</b></pre>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="8"> Комментарии:</td>
                    </tr>

                    <c:forEach var="memo" items="${task_to.memoList}">
                        <tr>
                            <td>${memo.createdBy.fullName}<br>${f:formatLocalDateTime(memo.dateCreate, 'dd.MM.yyyy HH:mm:ss')}
                                <c:if test="${task_to.createdBy.id == auth_user.id}">

                                    <button type="button" class="btn btn-primary"
                                            onclick="displayModal('${pageContext.request.contextPath}/task/deletememo',${user_id},${task_to.id},${page},${memo.id})"
                                            data-toggle="modal" data-target="#exampleModal"
                                            data-myvalue="troudbal" data-bb="troudbal">Удалить
                                    </button>
                                </c:if>
                            </td>
                                <%--href="${pageContext.request.contextPath}/deletememo?user_id=${active_user}&task_id=${task.id}&page=${active_page}&memo_id=${memo.id}"                            --%>
                            <td colspan="7">${memo.memo}
                                <c:if test="${not empty fn:trim(memo.fileName)}">
                                    <br>
                                    <a href="${pageContext.request.contextPath}/task/imagedisplay?id=${memo.id}">${memo.fileName}</a>
                                    <br>
                                    <img src="${pageContext.request.contextPath}/task/imagedisplay?id=${memo.id}"
                                         height="200"/>

                                </c:if>

                            </td>
                        </tr>
                    </c:forEach>
                    <form:form method="POST"
                               action="${pageContext.request.contextPath}/task/savememo/?_csrf=${_csrf.token}"
                               modelAttribute="memo_to" enctype="multipart/form-data">
                        <div class="col-md-12">
                            <input type="hidden" name="user_id" id="user_id" value="${user_id}"/>
                            <input type="hidden" name="page" id="page" value="${page}"/>
                            <input type="hidden" name="task_id" id="task_id" value="${task_to.id}"/>
                            <tr>
                                <td colspan="1">
                                    <input type="submit" class="btn btn-primary" value="Отправить">

                                </td>

                                <td colspan="7">
                                    <textarea type="text" rows="3" name="memo" placeholder="memo"
                                              class="form-control"></textarea>
                                    <br>
                                    <div class="custom-file" id="customFile" lang="es">
                                        <input type="file" accept=".jpg,.jpeg" name="fileUpload"
                                               class="custom-file-input" id="exampleInputFile"
                                               aria-describedby="fileHelp"
                                               onchange="$(this).next().after().text($(this).val().split('\\').slice(-1)[0])">
                                        <label class="custom-file-label" for="exampleInputFile">
                                            Select file...
                                        </label>
                                    </div>
                                </td>
                            </tr>

                        </div>
                    </form:form>


                </table>
                <%--
                                    <form:form method="post" action="doupload" enctype="multipart/form-data">
                                        <table border="0">
                                            <tr>
                                                <td>Pick file #2:</td>
                                                <td><input type="file" name="fileUpload" size="50"/></td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" align="center"><input type="submit" value="Upload"/></td>
                                            </tr>
                                        </table>
                                    </form:form>
                --%>

            </div>

        </div>
    </div>
</div>

</body>

</html>