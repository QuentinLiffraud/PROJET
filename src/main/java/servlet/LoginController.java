/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modele.*;

/**
 *
 * @author Flo
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            //Action appelée ? 
            String action = request.getParameter("action");
            System.out.println("action = " + action);

            String jspView = "";  // la page vers laquelle on redirigera l'utilisateur
            if (null != action) {
                switch (action) {
                    case "Connexion":
                        if (! checkLogin(request)) {
                            jspView = "Connexion.jsp"; // L'utilisateur re-saisit ses infos, le nom ou le mot de passe est manquant
                            break;
                        } else {
                            jspView = "Page Client.jsp"; // Le client accède à sa page et pourra ajouter une commande
                            break;
                        }

                    case "deconnexion":
                        doLogout(request);
                        jspView = "Connexion.jsp";  // Un autre utilisateur peut se connecter
                        break;
                    case "Commander":
                        AjouterCommande(request);
                        jspView = "Page Client.jsp"; // Si un champ est manquant, on reste sur la page client car il n'a pas voulu se déconnecter
                        break;
                        
                    case "S'inscrire":
                        jspView = "NewUser.jsp";
                        break;
                    case "Inscription":  // Le client a rempli ses informations
                        if (AjoutUtilisateur(request)) {
                            jspView = "Connexion.jsp"; // L'utilisateur peut maintenant se connecter
                            break;
                        } else {
                            jspView = "NewUser.jsp"; // L'utilisateur re-saisit ses infos
                            break;
                        }
                }
            }

            // Est-ce que l'utilisateur est connecté ?
            // On cherche l'attribut userName dans la session
            String userName = findUserInSession(request);
            String userAdmin = findAdminInSession(request);

            if (userName != null) {
                jspView = "Page Client.jsp";
            } else if (userAdmin != null) {
                jspView = "Graphe.jsp"; // La page des graphiques est accessible par l'admin !
            } else if (action == null) {  // Un des champs nom et mot de passe est vide (ou les 2)
                jspView = "Connexion.jsp"; // Page par défaut au départ !
            }

            // On redirige l'utilisateur vers la bonne page (jsp)
            request.getRequestDispatcher(jspView).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * Vérifie l'identifiant et le mot de passe d'un utilisateur afin de
     * l'authentifier
     *
     * @param request
     * @throws SQLException
     */
    private boolean checkLogin(HttpServletRequest request) throws SQLException {
        boolean verif = false;
        DAO dao = new DAO(DataSourceFactory.getDataSource());

        // Les paramètres transmis dans la requête
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        
        // Si l'utilisateur a oublié de saisir un champ : 
        if ( login.equals("") || password.equals("") || login.length() == 0 || password.length() == 0) {
            request.setAttribute("errorMessage", "Login/Password incorrect");
        } else {
            /* Le login/password défini dans web.xml */
            String loginAd = getInitParameter("loginAdmin");
            String passwordAd = getInitParameter("passwordAdmin");
            String userName = getInitParameter("ID");

            // Connexion admin ?
            if (loginAd.equals(login) && passwordAd.equals(password)) {
                HttpSession session = request.getSession(true); // démarre la session
                session.setAttribute("userAdmin", userName);
                verif = true;
            } else {  // Connexion client
                if (dao.verifClientConnexion(login, password)) {
                    // On a trouvé la combinaison login / password
                    // On stocke l'information dans la session
                    HttpSession session = request.getSession(true); // démarre la session
                    //Nom du client (Non admin)
                    String name = dao.nomClient(login, password);
                    session.setAttribute("userName", name);
                    session.setAttribute("id", password);
                    verif = true;
                } else { // On positionne un message d'erreur pour l'afficher dans la JSP
                    request.setAttribute("errorMessage", "Login/Password incorrect");
                }
            }
        }
        
        return verif;
    }

    /**
     * Déconnecte un utilisateur
     *
     * @param request
     */
    private void doLogout(HttpServletRequest request) {
        // On termine la session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     *
     * @param request
     * @return
     */
    private String findUserInSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (String) session.getAttribute("userName");
    }

    /**
     *
     * @param request
     * @return
     */
    private String findAdminInSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session == null) ? null : (String) session.getAttribute("userAdmin");
    }

    /**
     *
     * @param request
     * @throws SQLException
     */
    private void AjouterCommande(HttpServletRequest request) throws SQLException {
        DAO dao = new DAO(DataSourceFactory.getDataSource());

        int quantite = 0, id = 0;
        float fraisport = 0.0f;
        
        try {
            quantite = Integer.parseInt(request.getParameter("quantite"));
            id = Integer.parseInt(request.getParameter("id"));
            fraisport = Float.parseFloat(request.getParameter("fraisport"));
        } catch (NumberFormatException e) {  // Si le client oublie un champ concernant des chiffres
            request.setAttribute("errorMessage", "Au moins un champ n'est pas renseigné !");
        }

        String dateAchat = request.getParameter("dateAchat");
        String dateLivraison = request.getParameter("dateLivraison");
        String description = request.getParameter("produit");
        
        System.out.println("quantite !!!" + quantite);
        System.out.println("id !!!" + id);
        System.out.println("fraisport !!!" + fraisport);
        
        if ( dateAchat.length() == 0 || dateLivraison.length() == 0 || description.length() == 0) {  // Vérification des champs textes obligatoires (tous)
            request.setAttribute("errorMessage", "Au moins un champ n'est pas renseigné !");
        } else {
            // Ajouter une commande dans la base de données SAMPLE
            PurchaseEntity commande = new PurchaseEntity(1, id, quantite, 30, fraisport, dateAchat, dateLivraison, description);
            dao.ajoutCommande(commande);
        }
    }

    /* Vérifie qu'un utilisateur ait corectement saisi ses informations d'inscription 
       Si c'est le cas, l'utilisateur est enregistré
     */
    private boolean AjoutUtilisateur(HttpServletRequest request) throws SQLException, ServletException, IOException {
        boolean verif = false;

        // Les paramètres transmis dans la requête
        String email = request.getParameter("email");
        String nomUser = request.getParameter("nom");
        String adresse = request.getParameter("adresse");
        String ville = request.getParameter("ville");
        String tel = request.getParameter("tel");
        
        /* Si les informations sont valides, 
        notamment le champ de vérification du mot de passe doit être identique au mot de passe de l'utilisateur 
        
            On ajoute cet utilisateur dans la base de données
         */
        // Vérification de l'email :
        String regex = "^(.+)@(.+)$"; // L'email doit être de la forme nom@fournisseur.fr par exemple
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {  // Si l'email n'est pas syntaxiquement correct
            request.setAttribute("errorMessage", "Email incorrect");  // On positionne un message d'erreur pour l'afficher dans la JSP       
        } else if ( ville.length() == 0 || adresse.length() == 0 || tel.length() == 0 ) {  // Vérification des champs obligatoires (ville, adresse et telephone)
            request.setAttribute("errorMessage", "Au moins un champ n'est pas renseigné !");
        } else {
            /* On peut inscrire le client */
            DAO dao = new DAO(DataSourceFactory.getDataSource());
            dao.enregistreNouvelUtilisateur(email, adresse, nomUser, ville, tel);
            verif = true;  // L'utilisateur a correctement saisi ses infos
        }

        return verif;
    }
}
