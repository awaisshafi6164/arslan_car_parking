package com.example.arslancarparking;

public class DataClass {
    private String productName;
    private String productRegistration;
    private String productPrice;
    private String productPhone;
    private String productCategory;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductRegistration() {
        return productRegistration;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductPhone() {
        return productPhone;
    }

    public String getProductCategory() {
        return productCategory;
    }


    public DataClass(String productName, String productRegistration, String productPrice, String productPhone, String productCategory) {
        this.productName = productName;
        this.productRegistration = productRegistration;
        this.productPrice = productPrice;
        this.productPhone = productPhone;
        this.productCategory = productCategory;
    }

    public DataClass(){

    }
}
