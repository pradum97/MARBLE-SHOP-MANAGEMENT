package com.shop.management;

import com.shop.management.Main;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dashboard implements Initializable {

    public ListView<String> menu_listView;
    public BorderPane main_container;
    public Button bn_logout;
    public StackPane contentArea;
    private Connection connection;
    private DBConnection dbConnection;
    private Properties properties;
    Text text;
    @FXML
    private GridPane gridPane;

    private ObservableList<String> menu_item = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        text = new Text();
        replaceScene("dashboard/home.fxml");
        Main.primaryStage.setMaximized(true);


        setMenuData();

        setCustomImage();

    }

    private void setCustomImage() {

        InputStream is = null;
        try {
            is = new FileInputStream("src/main/resources/com/shop/management/img/menu_icon/logout_ic.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView logout_img = new ImageView();
        Image img = new Image(is);
        logout_img.setImage(img);
        logout_img.setFitHeight(20);
        logout_img.setFitWidth(20);

        bn_logout.setGraphic(logout_img);

    }

    private void setMenuData() {
        int i = 0;

        try {
            connection = dbConnection.getConnection();
            properties = dbConnection.getProperties("query.properties");
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("SIDE_MENU"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String item = rs.getString("menu_name");
                String icon_path = rs.getString("menu_icon_path");

                Hyperlink menu_button = new Hyperlink();

                menu_button.setId("menu_button");

                main_container.getStylesheets().add(String.valueOf(Main.class.getResource("css/menu.css")));
                ImageView icon = new ImageView();
                icon.setFitWidth(18);
                icon.setFitHeight(18);

                File file = new File("src/main/resources/com/shop/management/img/menu_icon/" + icon_path);
                InputStream is = new FileInputStream(file.getAbsolutePath());
                Image img = new Image(is);
                icon.setImage(img);
                menu_button.setText(item);

                menu_button.setOnAction(event -> {

                    String txt = ((Hyperlink) event.getSource()).getText();

                    switch (txt) {

                        case "HOME":
                            replaceScene("dashboard/home.fxml");
                            break;
                        case "PROFILE":
                            replaceScene("dashboard/userprofile.fxml");
                            break;
                        case "USERS":
                            replaceScene("dashboard/users.fxml");
                            break;
                        case "SETTING":
                            replaceScene("dashboard/setting.fxml");
                            break;
                        case "ALL PRODUCT":
                            replaceScene("dashboard/myProducts.fxml");
                            break;
                        case "SELL PRODUCT":
                            replaceScene("dashboard/sellProducts.fxml");
                            break;
                        case "SELL REPORT":
                            replaceScene("dashboard/sellReport.fxml");
                            break;
                        case "STOCK REPORT":
                            replaceScene("dashboard/stockReport.fxml");

                            break;
                    }


                });

                menu_button.setGraphic(icon);

                gridPane.add(menu_button, 0, i);
                gridPane.setVgap(15);

                i++;
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Dashboard : " + e.getMessage());

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void replaceScene(String fxml_file_name) {


        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxml_file_name));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(parent);

        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }
}
