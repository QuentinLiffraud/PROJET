<%-- 
    Document   : NewUser
    Created on : 11 déc. 2018, 16:07:24
    Author     : ClemTouil
--%>

<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="css/NewUser.css" />
    </head>

    <body>
        <form method="post" action="<c:url value="/" />">
            <fieldset id="cadre">
                <h2>Inscription</h2>

                <label for="email">Adresse email <span class="requis">*</span></label>
                <input type="text" id="email" name="email" value="" size="20" maxlength="60" />
                <br /><br />

                <label for="nom">Nom d'utilisateur <span class="requis">*</span></label>
                <input type="text" id="nom" name="nom" value="" size="20" maxlength="20" />
                <br /><br />

                <label for="adresse">Adresse <span class="requis">*</span></label>
                <input type="text" id="nom" name="adresse" value="" size="20" maxlength="20" />
                <br /><br />
                
                <label for="ville">Ville <span class="requis">*</span></label>
                <input type="text" id="nom" name="ville" value="" size="20" maxlength="20" />
                <br /><br />               
                
                <label for="tel">Téléphone <span class="requis">*</span></label>
                <input type="text" id="nom" name="tel" value="" size="20" maxlength="20" />
                <br /><br />
                
                <!-- Message d'erreur si on détècte un problème -->
                <div style="color:red">${errorMessage}</div></br>

                <input type="submit" name="action" value="Inscription" class="btn" />
                <br />
            </fieldset>
        </form>
    </body>
</html>