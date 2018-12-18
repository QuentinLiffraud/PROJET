/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author Flo
 */
public class TurnoverClient {

    private String nom;
    private float prix;

    public TurnoverClient(String nom, float prix) {
        this.nom = nom;
        this.prix = prix;
    }

    public String getDescription() {
        return nom;
    }

    public float getPrix() {
        return prix;
    }
}
