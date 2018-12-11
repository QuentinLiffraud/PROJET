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

        <title>Inscription</title>

        <link type="text/css" rel="stylesheet" href="NewUser.css" />

    </head>

    <body>

        <form method="post" action="inscription">

            <fieldset>

                <legend>Inscription</legend>

                <p>Vous pouvez vous inscrire via ce formulaire.</p>


                <label for="email">Adresse email <span class="requis">*</span></label>

                <input type="text" id="email" name="email" value="" size="20" maxlength="60" />

                <br />


                <label for="motdepasse">Mot de passe <span class="requis">*</span></label>

                <input type="password" id="motdepasse" name="motdepasse" value="" size="20" maxlength="20" />

                <br />


                <label for="confirmation">Confirmation du mot de passe <span class="requis">*</span></label>

                <input type="password" id="confirmation" name="confirmation" value="" size="20" maxlength="20" />

                <br />


                <label for="nom">Nom d'utilisateur</label>

                <input type="text" id="nom" name="nom" value="" size="20" maxlength="20" />

                <br />


                <input type="submit" value="Inscription" class="sansLabel" />

                <br />

            </fieldset>

        </form>

    </body>

</html>