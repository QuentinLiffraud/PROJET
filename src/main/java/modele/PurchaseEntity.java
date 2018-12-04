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
public class PurchaseEntity {
    
    private int orderNum;
    private int custom_id;
    private int quantite;
    private float prix;
    private float fraisport;
    private String dateachat;
    private String datelivraison;
    private String description;
    
    public PurchaseEntity(int order,int customid, int quantite, float prix, float fdp, String dateach,String dateliv, String descrip){
        this.orderNum = order;
        this.custom_id = customid;
        this.quantite = quantite;
        this.prix = prix;
        this.fraisport = fdp;
        this.dateachat = dateach;
        this.datelivraison = dateliv;
        this.description = descrip;
    }

    public void setCustom_id(int custom_id) {
        this.custom_id = custom_id;
    }

    public int getCustom_id() {
        return custom_id;
    }

    public void setDateachat(String dateachat) {
        this.dateachat = dateachat;
    }

    public String getDateachat() {
        return dateachat;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public void setFraisport(float fraisport) {
        this.fraisport = fraisport;
    }

    public void setDatelivraison(String datelivraison) {
        this.datelivraison = datelivraison;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public int getQuantite() {
        return quantite;
    }

    public float getPrix() {
        return prix;
    }

    public float getFraisport() {
        return fraisport;
    }

    public String getDatelivraison() {
        return datelivraison;
    }

    public String getDescription() {
        return description;
    }
}

