package com.shop.management.Method;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StaticData {

    public String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";


    public ObservableList<String> getGender() {

        return FXCollections.observableArrayList("Male", "Female", "Other");
    }
 public ObservableList<String> stockFilter() {

        return FXCollections.observableArrayList("ALL", "Out Of Stock", "LOW" , "MEDIUM" , "HIGH");
    }

    public ObservableList<String> getBillingType() {

        return FXCollections.observableArrayList("REGULAR", "GST", "PROPOSAL");
    }

    public  ObservableList<String> getPaymentMode(){

        return FXCollections.observableArrayList("CASH","PHONE PE","GOOGLE PE" , "UPI" , "OTHER");
    }

    public ObservableList<String> getProductType() {

        return FXCollections.observableArrayList("Wall", "Floor" , "Other");
    }

    public ObservableList<String> getDiscountType() {

        return FXCollections.observableArrayList("PERCENTAGE","FLAT");
    }

    public ObservableList<String> getSizeUnit() {

        return FXCollections.observableArrayList("MM", "CM","INCH","FT");
    }
    public ObservableList<String> getSizeQuantityUnit() {

        return FXCollections.observableArrayList("PCS", "PKT");
    }
    public ObservableList<String> getAccountStatus() {

        return FXCollections.observableArrayList("Inactive","Active" );
    }

}
