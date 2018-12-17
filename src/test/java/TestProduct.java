/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import modele.*;
import modele.PurchaseEntity;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Flo
 */
public class TestProduct {

    private static DataSource myDataSource; // La source de données à utiliser
    private static Connection myConnection;
    private DAO myDAO;

    @Before
    public void setUp() throws SQLException {
        myDataSource = DataSourceFactory.getDataSource();
        myDAO = new DAO(myDataSource);
    }

    @Test
    public void verifPurchaseForCustomer() throws SQLException {
        String id = "1";
        assertTrue(!myDAO.produitClient(id).isEmpty());
    }

    @Test
    public void verifPurchaseForNoCustomer() throws SQLException {
        String id = "105";
        assertTrue(myDAO.produitClient(id).isEmpty());
    }

    @Test
    public void verifNumberPurchase() throws SQLException {
        List<PurchaseEntity> commande = myDAO.produitClient("1");
        int nb_commande = commande.size();
        assertTrue(nb_commande == 1);
    }

    public static DataSource getDataSource() throws SQLException {
        org.apache.derby.jdbc.ClientDataSource ds = new org.apache.derby.jdbc.ClientDataSource();
        ds.setDatabaseName("sample");
        ds.setUser("app");
        ds.setPassword("app");
        // The host on which Network Server is running
        ds.setServerName("localhost");
        // port on which Network Server is listening
        ds.setPortNumber(1527);
        return ds;
    }
}