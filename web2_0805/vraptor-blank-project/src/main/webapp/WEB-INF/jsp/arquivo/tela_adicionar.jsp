<%-- 
    Document   : tela_adicionar
    Created on : 08/05/2018, 19:36:52
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
        ${mensagem}
        <form action="${pageContext.request.contextPath}/arquivo/adicionar" enctype="multipart/form-data"  method="post">
            Arquivo: <input type="file" name="arquivo">            
            <input type="submit">                      
        </form>
    </body>
</html>
