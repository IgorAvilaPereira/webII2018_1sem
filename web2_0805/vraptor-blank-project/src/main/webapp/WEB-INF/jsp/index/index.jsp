<%-- 
    Document   : index
    Created on : 08/05/2018, 20:22:06
    Author     : iapereira
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        ${variable}
        <form action="${pageContext.request.contextPath}/index/login" method="post">
            login: <input type="text" name="login">
            <br>
            senha: <input type="password" name="senha">
            
            <input type="submit">
            
            <a href="${pageContext.request.contextPath}/index/destruir"> Deslogar </a>
            
            
        </form>
    </body>
</html>
