<%-- 
    Document   : Page Client
    Created on : 11 déc. 2018, 16:16:58
    Author     : ClemTouil
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<sql:setDataSource 
	driver="org.apache.derby.jdbc.ClientDriver"
	url="jdbc:derby://localhost:1527/sample"
	user="app" password="app"
/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/Client.css" />
        <title>${userName}</title>
      
    </head>
    <body>
        <sql:query var="commande">
            SELECT PURCHASE_ORDER.ORDER_NUM,PRODUCT.DESCRIPTION,PRODUCT.PURCHASE_COST,PURCHASE_ORDER.QUANTITY,PURCHASE_ORDER.SHIPPING_COST,
            PURCHASE_ORDER.SHIPPING_DATE,PRODUCT.PRODUCT_ID,PURCHASE_ORDER.SALES_DATE
                        FROM PURCHASE_ORDER
                            INNER JOIN PRODUCT
                            ON PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID
                        WHERE CUSTOMER_ID=?
            
            <sql:param value="${id}" />
        </sql:query>
        <sql:query var="nomProduit">
                SELECT PRODUCT.DESCRIPTION,PRODUCT.PURCHASE_COST FROM PRODUCT
        </sql:query>  
                <h1>Bienvenue ${userName}</h1>     
        <form action="<c:url value="/" />" mehod="POST">
            <fieldset id="cadre">
                <h2>Commande</h2>
            <label for="prod">  Produit :</label>
            <select name="produit">
                <c:forEach var="rec" items="${nomProduit.rows}">
                    <option value="${rec.DESCRIPTION}">${rec.DESCRIPTION}</option>
                </c:forEach>
            </select>
            <label for="qnt"> Quantite :</label>
            <input name="quantite" /></br>
            <label for="fdp"> Frais de port :</label>
            <input name="fraisport" /></br>
            <label for="dateA"> Date d'achat</label>
            <input type="date" name="dateAchat" /></br>
            <label for="dateL"> Date de livraison</label>
            <input type="date" name="dateLivraison" /></br>
            
            </br><input type="submit" name="action" value="Commander"/>
            </fieldset>
        </form>    
                
        
                </br>
        <table border="1">
            <tr>
                <th>Numéro de Commande</th>
                <th>Numéro Produit</th>
                <th>Description</th>
                <th id="qt">Quantité</th>
                <th id="px">Prix</th>
                <th id="fp">Frais de port</th>
                <th id="da">Date d'achat</th>
                <th id="dl">Date livraison</th>
                
                <c:forEach var="record" items="${commande.rows}">
                <tr><td>${record.ORDER_NUM}</td><td>${record.PRODUCT_ID}</td><td>${record.DESCRIPTION}</td><td>${record.QUANTITY}</td><td>${record.PURCHASE_COST}</td>
                <td>${record.SHIPPING_COST}</td><td>${record.SALES_DATE}</td><td>${record.SHIPPING_DATE}</td></tr>
                </c:forEach>
            </tr>
        </table>
        
        </br><form action="<c:url value="/" />" method="POST" id="deco">
            <input type='submit' name='action' value='deconnexion'>
	</form>
    </body>
