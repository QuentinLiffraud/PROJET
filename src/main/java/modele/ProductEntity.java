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
public class ProductEntity {

    private int productID;
    private String productCode;
    private float price;
    private String available;
    private String description;

    public ProductEntity(int productID, String productCode, float price, String available, String description) {
        this.productID = productID;
        this.productCode = productCode;
        this.price = price;
        this.available = available;
        this.description = description;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductCode() {
        return productCode;
    }

    public float getPrice() {
        return price;
    }

    public String getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
