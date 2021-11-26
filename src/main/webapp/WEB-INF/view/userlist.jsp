<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <jsp:include page="libs.jsp"/>

    <%--&lt;%&ndash;<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css"&ndash;%&gt;--%>
          <%--&lt;%&ndash;type="text/css">&ndash;%&gt;--%>
    <%--&lt;%&ndash;<link rel="stylesheet" href="https://static.pingendo.com/bootstrap/bootstrap-4.2.1.css">&ndash;%&gt;--%>
    <%--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"--%>
            <%--integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"--%>
            <%--crossorigin="anonymous"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js"--%>
            <%--integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut"--%>
            <%--crossorigin="anonymous"></script>--%>
    <%--<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"--%>
            <%--integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"--%>
            <%--crossorigin="anonymous"></script>--%>
    <script type="text/javascript">
        function displayModal(refModal, user_id, email, name, surname, username, password, isactive, isadmin, islist) {
            document.getElementById("refModal").action = refModal;
            document.getElementById("form_user_id").value = user_id;
            document.getElementById("form_email").value = email;
            document.getElementById("form_name").value = name;
            document.getElementById("form_surname").value = surname;
            document.getElementById("form_password").value = password;
            document.getElementById("form_username").value = username;
            document.getElementById("form_isactive").checked = isactive;
            document.getElementById("form_isadmin").checked = isadmin;
            document.getElementById("form_islist").checked = islist;

        }
    </script>

</head>

<body>
<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Внимание!!</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form:form id="refModal" action="${pageContext.request.contextPath}/user/edit" method="post">
                <div class="modal-body">
                    <table class="table text-secondary">
                        <tr><td>Имя:</td><td><input name="name" class="form-control" id="form_name"/></td>
                            <td>Фамилия:</td><td><input name="surname" class="form-control" id="form_surname"/></td></tr>

                        <tr><td>Email:</td><td><input name="email" class="form-control" id="form_email"/></td></tr>

                        <tr><td>Логин:</td><td><input name="username" class="form-control" id="form_username"/>
                            <td>Пароль:</td><td><input name="password" class="form-control" id="form_password"/></td></tr>

                        <tr><td>Доступ:</td><td><input name="isactive" type="checkbox" class="form-control" id="form_isactive"/></td>
                            <td>Администратор:</td><td><input name="isadmin" type="checkbox" class="form-control" id="form_isadmin"/></td>
                        </tr>
                        <tr><td>Отображать в списке:</td><td><input name="islist" type="checkbox" class="form-control" id="form_islist"/></td>
                        </tr>


                    </table>
                    <input type="hidden" name="id" id="form_user_id" value=""/>
                </div>
                <div class="modal-footer">


                    <button type="submit" class="btn btn-primary">Сохранить</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>

                </div>
            </form:form>
        </div>
    </div>
</div>

<div class="py-1">
    <div class="container-fluid">
        <jsp:include page="head.jsp"/>


        <div class="row">
            <div class="col" style="">
<%--                <form:form id="refEdit">--%>
                    <button
                            class="btn btn-primary"
<%--                            onclick="alert('Клик!')"--%>
                            onclick="displayModal('${pageContext.request.contextPath}/user/edit',0,'','' ,'' ,'' ,'' ,false ,false ,false )"
                            data-toggle="modal" data-target="#exampleModal"
                            data-myvalue="troudbal" data-bb="troudbal"
                    >Создать

                    </button>


<%--                </form:form>--%>

                <div class="row">
                    <div class="col">
                        <div class="table">

                            <table class="table table-hover table-sm">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Имя</th>
                                    <th>Login</th>
                                    <th>Email</th>
                                    <th>Доступ</th>
                                    <th>Администратор</th>
                                    <th>Отображать в списке</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="user" items="${userList}">
                                    <tr onclick="displayModal('${pageContext.request.contextPath}/user/edit',${user.id},'${user.email}', '${user.name}' , '${user.surname}', '${user.userName}', '${user.password}', ${user.active}, ${user.admin}, ${user.list});"
                                        data-toggle="modal" data-target="#exampleModal"
                                        data-myvalue="troudbal" data-bb="troudbal">
                                        <td>${user.id}</td>
                                        <td>${user.fullName}</td>
                                        <td>${user.userName}</td>
                                        <td>${user.email}</td>
                                        <td>${user.active}</td>
                                        <td>${user.admin}</td>
                                        <td>${user.list}</td>
                                    </tr>

                                </c:forEach>

                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>


</body>

</html>