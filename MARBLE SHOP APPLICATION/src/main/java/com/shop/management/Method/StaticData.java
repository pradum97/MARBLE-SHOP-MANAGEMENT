package com.shop.management.Method;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StaticData {

    public String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";


    public ObservableList<String> getGender() {

        return FXCollections.observableArrayList("Male", "Female", "Other");
    }

    public ObservableList<String> getBillingType() {

        return FXCollections.observableArrayList("REGULAR", "GST", "PROPOSAL");
    }

    public  ObservableList<Object> getLimit(){

        return FXCollections.observableArrayList(20,50,100,150,200,300,500,700,1000);
    }

    public ObservableList<String> getProductType() {

        return FXCollections.observableArrayList("Wall", "Floor" , "Other");
    }

    public ObservableList<String> getDiscountType() {

        return FXCollections.observableArrayList("PERCENTAGE","FLAT");
    }

    public ObservableList<String> getProductCategory() {

        return FXCollections.observableArrayList("Tiles", "Marble","Granite","Other");
    }

    public ObservableList<String> getSizeUnit() {

        return FXCollections.observableArrayList("MM", "CM","INCH","FT");
    }
    public ObservableList<String> getSizeQuantityUnit() {

        return FXCollections.observableArrayList("PCS", "PKT");
    }
    public ObservableList<String> getProductColor() {

        return FXCollections.observableArrayList("White", "Dark grey","Blue" ,"Brown"
                ,"Black" ,"Gold " ,"Orange" , "Red" ,"Green", "Black porcelain","Multicolour" , "Other");

    }
    public ObservableList<String> getAccountStatus() {

        return FXCollections.observableArrayList("Inactive","Active" );
    }

}
