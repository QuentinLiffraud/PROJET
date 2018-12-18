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
public class DiscountCodeEntity {
    
    private String code;
    private double rate;
    
    public DiscountCodeEntity(String code, double rate){
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }   
}