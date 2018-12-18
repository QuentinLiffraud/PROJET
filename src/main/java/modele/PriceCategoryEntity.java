/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author Quentin
 */
class PriceCategoryEntity {
    private String description;
    private float prix;
    
  public PriceCategoryEntity(String description, float prix ){
    this.description = description;
    this.prix = prix;
}

    public String getDescription() {
        return description;
    }

    public float getPrix() {
        return prix;
    }
}