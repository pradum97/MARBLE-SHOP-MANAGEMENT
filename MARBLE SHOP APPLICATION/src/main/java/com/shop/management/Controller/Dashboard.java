package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private Connection connection;
    private DBConnection dbConnection;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        System.out.println(dbConnection.connection());

    }
}
