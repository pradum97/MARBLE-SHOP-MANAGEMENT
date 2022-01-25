package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {

    private DBConnection dbConnection;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dbConnection = new DBConnection();
        Connection connection = dbConnection.getConnection();
    }

    public void forget_password_bn(ActionEvent event) {
    }

    public void login_bn(ActionEvent event) {
    }

    public void create_new_account(ActionEvent event) {


    }
}
