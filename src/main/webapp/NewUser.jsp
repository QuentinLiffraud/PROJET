<%-- 
    Document   : NewUser
    Created on : 11 déc. 2018, 16:07:24
    Author     : ClemTouil
--%>

<%@ page pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8" />
        <link rel="stylesheet" href="css/NewUser.css" />
    </head>

    <body>
        <form method="post" action="inscription">
            <fieldset id="cadre">
                <h2>Inscription</h2>
                
                <label for="email">Adresse email <span class="requis">*</span></label>
                <input type="text" id="email" name="email" value="" size="20" maxlength="60" />
                <br /><br />


                <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
                <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" />
                <br /><br />


                <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>
                <input type="password" id="confirmation" name="confirmation" value="" maxlength="20" />
                <br /><br />

                <label for="nom">Nom d'utilisateur</label>
                <input type="text" id="nom" name="nom" value="" size="20" maxlength="20" />
                <br /><br />
                
                <!-- Message d'erreur si on détècte un problème -->
                <div style="color:red">${errorMessage}</div></br>
                
                <input type="submit" name="action" value="Inscription" class="btn" />
                <br />
            </fieldset>
        </form>
    </body>
</html>