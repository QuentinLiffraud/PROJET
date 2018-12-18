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
public class PriceLocalisationEntity {
    private int  codePostal;
    private float prix;

    public PriceLocalisationEntity(int codePostal, float prix ){
    this.codePostal = codePostal;
    this.prix = prix;
}
    
    public int getCodePostal() {
        return codePostal;
    }

    public float getPrix() {
        return prix;
    }
}

