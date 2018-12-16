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
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Quentin
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
     * @param password
     * @param nomUser 
     */
    public void enregistreNouvelUtilisateur(String email, String password, String nomUser) throws SQLException {
        int newId = newUserID();
        
        /* !!!! TODO Insert password => Déployer BDD + hash (afin qu'ils ne soient pas en clair) */
        String sql2 = "INSERT INTO CUSTOMER(CUSTOMER_ID, DISCOUNT_CODE, ZIP, NAME, EMAIL) VALUES(?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql2)) {

            stmt.setInt(1, newId);
            stmt.setString(5, email);
            
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

            stmt.executeUpdate();
        }       
    }
}
