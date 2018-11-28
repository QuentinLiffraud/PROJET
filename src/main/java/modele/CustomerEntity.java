/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author Clément
 */
public class CustomerEntity {
    
    private int customerID;
    private String discountCode;
    private String zipCode;
    private String email;
    private String name;
    
    public CustomerEntity(int customerID, String discountCode, String zipCode, String email, String name){
        this.customerID = customerID;
        this.discountCode = discountCode;
        this.zipCode = zipCode;
        this.email = email;
        this.name = name;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
