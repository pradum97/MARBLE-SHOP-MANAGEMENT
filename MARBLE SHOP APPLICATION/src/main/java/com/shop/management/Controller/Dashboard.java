package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    public ListView<String> menu_listView;
    public BorderPane main_container;
    private Connection connection;
    private DBConnection dbConnection;
    private Properties properties;
    Text text;

    private ObservableList<String> menu_item = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        text = new Text();

        setTheme();
        setMenuData();

    }

    private void setTheme() {

        menu_listView.setStyle("");
    }

    private void setMenuData() {

        try {
            connection = dbConnection.getConnection();
            properties =dbConnection.getProperties("query.properties");
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("SIDE_MENU"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()){

                String item = rs.getString("menu_name");
                menu_item.add(item);
                text.setText(item);
            }


            menu_listView.setItems(menu_item);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Dashboard : "+e.getMessage());

        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
