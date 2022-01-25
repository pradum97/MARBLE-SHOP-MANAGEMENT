package com.shop.management.Controller;

import com.shop.management.util.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class Feedback implements Initializable {
    private Properties properties;
    DBConnection dbConnection;
    @FXML
    public Button bn_feedback_submit;
    String button_bg_color , button_text_color;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        properties = dbConnection.getProperties("color.properties");

        button_bg_color = properties.getProperty("BUTTON_BG_COLOR");
        button_text_color = properties.getProperty("BUTTON_TEXT_COLOR");


        setTheme();

    }

    private void setTheme() {

        bn_feedback_submit.setStyle(
                "-fx-background-color:"+button_bg_color+";" +
                        " -fx-text-fill:"+button_text_color
        );
    }
}
