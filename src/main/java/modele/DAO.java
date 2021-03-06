/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Quentin, Florent
 */
public class DAO {

    private final DataSource myDataSource;

    /**
     * @param dataSource la source de données à utiliser
     */
    public DAO(DataSource dataSource) {
        this.myDataSource = dataSource;
    }

    /**
     * 
     * @param email
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean verifClientConnexion(String email, String id) throws SQLException {
        boolean verif = false;
        String sql = "SELECT COUNT(*) AS Nombre FROM CUSTOMER WHERE EMAIL=? AND CUSTOMER_ID=? ";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    verif = resultSet.getInt("Nombre") == 1;
                }
            } catch (SQLException ex) {
                Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
                throw new SQLException(ex.getMessage());
            }
            
            return verif;
        }
    }

    /**
     * Renvoie le nom du client en fonction de son email et son id
     *
     * @param email
     * @param id
     * @return
     * @throws SQLException
     */
    public String nomClient(String email, String id) throws SQLException {
        String result = "";
        String sql = "SELECT NAME FROM CUSTOMER WHERE EMAIL=? AND CUSTOMER_ID=? ";

        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, id);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getString("NAME");
                }
            } catch (SQLException ex) {
                Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
                throw new SQLException(ex.getMessage());
            }
            return result;
        }
    }

    /**
     * 
     * @return le nouvel ID afin de pouvoir insérer une nouvelle ligne dans la table PURCHASE_ORDER
     * @throws SQLException 
     */
    public int newOrderNum() throws SQLException {
        String sql = "SELECT MAX(order_num)+1 FROM PURCHASE_ORDER";
        int newId = -1;
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("ORDER_NUM");
                }
            }
        }
        
        return newId;
    }
    
    /**
     * Ajoute une commande à la table Purchase_order
     *
     * @param commande
     * @throws SQLException
     */
    public void ajoutCommande(PurchaseEntity commande) throws SQLException {
        int newId = newOrderNum();
        String sql2 = "INSERT INTO PURCHASE_ORDER VALUES(?,?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql2)) {

            stmt.setInt(1, newId);
            stmt.setInt(2, commande.getQuantite());
            stmt.setFloat(3, commande.getPrix());
            stmt.setFloat(4, commande.getFraisport());
            stmt.setString(5, commande.getDateachat());
            stmt.setString(6, commande.getDatelivraison());
            stmt.setString(7, commande.getDescription());

            stmt.executeUpdate();
        }
    }

    /**
     * @return tous les codes ZIP de la table MICRO_MARKET
     */
    public List<String> getAllZipCodes() throws SQLException {
        String sql = "SELECT ZIP_CODE AS ZIP FROM MICRO_MARKET";
        List<String> zips = new ArrayList<>();
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                /* Parcours de tous les codes ZIP */ 
                while (rs.next()) {
                    zips.add(rs.getString("ZIP"));  // ajoute tous les codes à notre liste
                }
            }
        }
        
        return zips;
    }
    
    /**
     * @return Retrouve l'ID du dernier enregistrement et passe à l'ID suivant pour l'insertion d'un nouvel utilisateur
     * @throws SQLException 
     */
    public int newUserID() throws SQLException {
        String sql = "SELECT MAX(CUSTOMER_ID)+1 AS NEWID FROM CUSTOMER";
        int newId = -1;
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    newId = rs.getInt("NEWID");
                }
            }
        }
        
        return newId;
    }
    
    /**
     * Enregistre un nouvel utilisateur après avoir vérifié ses informations
     * @param email
     * @param adresse
     * @param nomUser 
     * @throws java.sql.SQLException 
     */
    public void enregistreNouvelUtilisateur(String email, String adresse, String nomUser, String ville, String tel) throws SQLException {
        int newId = newUserID();
        
        /* !!!! TODO Insert password => Déployer BDD + hash (afin qu'ils ne soient pas en clair) */
        String sql2 = "INSERT INTO CUSTOMER(CUSTOMER_ID, DISCOUNT_CODE, ZIP, NAME, ADDRESSLINE1, CITY, PHONE, EMAIL) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql2)) {

            stmt.setInt(1, newId);
            
            /* pour le discount_code et le zip, insère un élément aléatoire */
            List<Character> discount_code = Arrays.asList('H', 'L', 'M', 'N');
            Random rand = new Random();
            char randomDiscountCode = discount_code.get(rand.nextInt(discount_code.size()));
            
            /* Code ZIP aléatoire 
                ATTENTION ! Ce champ est une clé étrangère dans la table MICRO_MARKET
                Nous allons donc insérer un code ZIP aléatoire parmi le contenu de cette table
             */
            List<String> zips = getAllZipCodes();
            int randomPosition = rand.nextInt(10);  // nombre aléatoire entre 0 et 10
            String zip = zips.get(randomPosition); // Sélection d'un code ZIP aléatoire
            
            
            // Insertion des valeurs récupérées
            stmt.setString(2, String.valueOf(randomDiscountCode)); // On converti le caractère obtenu en string pour le passer en paramètre de la requête
            stmt.setString(3, zip);  
            
            /* Colonne nom d'utilisateur */
            if (nomUser != null)  /* Si l'utilisateur a saisi un nom dans le formulaire */
                stmt.setString(4, nomUser);
            else
                stmt.setNull(4, Types.VARCHAR);

            stmt.setString(5, adresse);
            stmt.setString(6, ville);
            stmt.setString(7, tel);
            stmt.setString(8, email);
            
            stmt.executeUpdate();
        }       
    }


    /**
     * Renvoie la liste des produits du client passé en paramètre
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public List<PurchaseEntity> produitClient(String id) throws SQLException {
        ArrayList<PurchaseEntity> result = new ArrayList<>();

        String sql = "SELECT PURCHASE_ORDER.ORDER_NUM,PRODUCT.DESCRIPTION,PRODUCT.PURCHASE_COST,PURCHASE_ORDER.QUANTITY,PURCHASE_ORDER.SHIPPING_COST,PURCHASE_ORDER.SALES_DATE,PURCHASE_ORDER.SHIPPING_DATE \n"
                + "FROM PURCHASE_ORDER\n"
                + "    INNER JOIN PRODUCT\n"
                + "    ON PURCHASE_ORDER.PRODUCT_ID = PRODUCT.PRODUCT_ID\n"
                + "WHERE CUSTOMER_ID=?";

        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int order = rs.getInt("ORDER_NUM");
                    int quantite = rs.getInt("QUANTITY");
                    float prix = rs.getFloat("PURCHASE_COST");
                    float sc = rs.getFloat("SHIPPING_COST");
                    String dateach = rs.getString("SALES_DATE");
                    String dateliv = rs.getString("SHIPPING_DATE");
                    String description = rs.getString("DESCRIPTION");

                    result.add(new PurchaseEntity(order, Integer.parseInt(id), quantite, prix, sc, dateach, dateliv, description));

                }
            } catch (SQLException ex) {
                Logger.getLogger("DAO").log(Level.SEVERE, null, ex);
                throw new SQLException(ex.getMessage());
            }
            return result;
        }
    }
    
    /**
     * Créer la Map utilisée par le graphique du Chiffre d'affaire par Catégorie
     *
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    public Map<String, Float> PriceCategoryEntity(String dateDebut, String dateFin) throws Exception {
        Map<String, Float> result = new HashMap<>();
        if (dateDebut == null) {
            dateDebut = "2010-05-24";
        }
        if (dateFin == null) {
            dateFin = "2012-05-24";
        }
        // Une requête SQL paramétrée
        String sql = "SELECT SUM(QUANTITY*PURCHASE_COST) AS TOTAL,PRODUCT_CODE.DESCRIPTION\n"
                + "FROM PRODUCT INNER JOIN PURCHASE_ORDER ON PRODUCT.PRODUCT_ID = PURCHASE_ORDER.PRODUCT_ID \n"
                + "INNER JOIN PRODUCT_CODE ON PRODUCT_CODE.PROD_CODE=PRODUCT.PRODUCT_CODE\n"
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY PRODUCT_CODE.DESCRIPTION ORDER BY TOTAL";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                String description = rs.getString("DESCRIPTION");
                float prix = rs.getFloat("TOTAL");
                // On l'ajoute à la liste des résultats
                result.put(description, prix);
            }
        }
        
        return result;
    }
    
    /**
         * Créer la Map utilisée par le graphique du Chiffre d'affaire par Client
     *
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    public Map<String, Float> TurnoverClient(String dateDebut, String dateFin) throws Exception {
        Map<String, Float> result = new HashMap<>();
        if (dateDebut == null) {
            dateDebut = "2010-05-24";
        }
        if (dateFin == null) {
            dateFin = "2012-05-24";
        }
        // Une requête SQL paramétrée
        String sql = "SELECT SUM(QUANTITY*PURCHASE_COST) AS BENEF,NAME "
                + "FROM PRODUCT INNER JOIN PURCHASE_ORDER ON PRODUCT.PRODUCT_ID = PURCHASE_ORDER.PRODUCT_ID "
                + "INNER JOIN CUSTOMER ON CUSTOMER.CUSTOMER_ID = PURCHASE_ORDER.CUSTOMER_ID "
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY NAME ORDER BY BENEF";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                String nom = rs.getString("NAME");
                float prix = rs.getFloat("BENEF");
                // On l'ajoute à la liste des résultats
                result.put(nom, prix);
            }
        }
        return result;
    }
    
    
    /**
     * Créer la Map utilisée par le graphique du Chiffre d'affaire par code
     * Postal
     *
     * @param dateDebut
     * @param dateFin
     * @return
     * @throws Exception
     */
    public Map<Integer, Float> PriceLocalisationEntity(String dateDebut, String dateFin) throws Exception {
        Map<Integer, Float> result = new HashMap<>();
        if (dateDebut == null) {
            dateDebut = "2010-05-24";
        }
        if (dateFin == null) {
            dateFin = "2012-05-24";
        }
        // Une requête SQL paramétrée
        String sql = "SELECT SUM(QUANTITY*PURCHASE_COST) AS TOTAL,CUSTOMER.ZIP\n"
                + "FROM PRODUCT INNER JOIN PURCHASE_ORDER ON PRODUCT.PRODUCT_ID = PURCHASE_ORDER.PRODUCT_ID \n"
                + "INNER JOIN CUSTOMER ON CUSTOMER.CUSTOMER_ID = PURCHASE_ORDER.CUSTOMER_ID\n"
                + "WHERE SALES_DATE BETWEEN ? AND ? "
                + "GROUP BY CUSTOMER.ZIP ORDER BY TOTAL";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql);) {
            stmt.setString(1, dateDebut);
            stmt.setString(2, dateFin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // On récupère les champs nécessaires de l'enregistrement courant
                int codePostal = rs.getInt("ZIP");
                float prix = rs.getFloat("TOTAL");
                // On l'ajoute à la liste des résultats
                result.put(codePostal, prix);
            }
        }
        return result;
    }
}
