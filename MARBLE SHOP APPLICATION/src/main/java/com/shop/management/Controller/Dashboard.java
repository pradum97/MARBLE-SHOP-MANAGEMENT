package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    public HBox dashboard_main_container;
    public VBox side_menu;
    public VBox screen_container;
    public Hyperlink menu_btn;
    private Connection connection;
    private DBConnection dbConnection;
    private Properties properties;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();

        try {
            connection = dbConnection.getConnection();
            properties =dbConnection.getProperties("query.properties");
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("Sidemanu"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString("menu_name"));
                menu_btn.setText(rs.getString("menu_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
