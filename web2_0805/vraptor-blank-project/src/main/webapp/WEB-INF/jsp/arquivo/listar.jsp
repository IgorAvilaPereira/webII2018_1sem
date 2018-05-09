<%-- 
    Document   : listar
    Created on : 08/05/2018, 20:02:52
    Author     : iapereira
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        Welcome ....${usuarioSessao.login}
        
        <table border="1">
            <c:forEach items="${vetArquivo}" var="arquivo">
                <tr> 
                    <td> <a href="${pageContext.request.contextPath}/arquivo/excluir/${arquivo.name}">Excluir</a> </td>
                    <td> <img height="100px" width="100px" src="${pageContext.request.contextPath}/arquivo/visualizar/${arquivo.name}"> </td> </tr>
                    </c:forEach>
        </table>
                <a href="${pageContext.request.contextPath}/arquivo/tela_adicionar">Adicionar</a>

    </body>
</html>
