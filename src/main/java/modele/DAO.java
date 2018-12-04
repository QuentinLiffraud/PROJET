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
     * Ajoute une commande à la table Purchase_order
     *
     * @param commande
     * @throws SQLException
     */
    public void ajoutCommande(PurchaseEntity commande) throws SQLException {

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
        String sql2 = "INSERT INTO PURCHASE_ORDER VALUES(?,?,?,?,?,?,?)";
        try (Connection connection = myDataSource.getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)) {

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
}
