<%-- 
    Document   : Connexion
    Created on : 1 dec. 2018, 12:15:57
    Author     : Clément
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Page de Connexion</title>
        <link rel="stylesheet" href="css/Connexion.css" />
    </head>
    <body>
        <form action="<c:url value="/" />" mehod="POST">
            <fieldset id="cadre">
            <h2>Connexion</h2>
            <label for="log"> Login :<span class="requis">*</span></label>
            <input name="login" /></br></br>
            <label for="mdp"> Password :<span class="requis">*</span></label>
            <input type="password" name="password" /></br>
            
            <div style="color:red">${errorMessage}</div></br>
            </br><input type="submit" name="action" value="Connexion" />
            <input type="submit" name="action" value="S'inscrire" />
            </fieldset>
        </form>
    </body>
</html>