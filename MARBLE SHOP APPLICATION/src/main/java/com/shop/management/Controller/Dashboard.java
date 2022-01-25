package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private Connection connection;
    private DBConnection dbConnection;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
       connection = dbConnection.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from tbl_role");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                System.out.println(rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
