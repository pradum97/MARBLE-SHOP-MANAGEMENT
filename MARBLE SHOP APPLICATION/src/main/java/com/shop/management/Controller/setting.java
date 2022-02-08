package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ReplaceScene;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class setting implements Initializable {

    public StackPane contentArea;
    ReplaceScene replaceScene;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        replaceScene = new ReplaceScene();


        replaceScene.replace("setting/general.fxml",contentArea);
    }

    public void bnGeneral(ActionEvent event) {

        replaceScene.replace("setting/general.fxml",contentArea);
    }

    public void bnAppearance(ActionEvent event) {
        replaceScene.replace("setting/appearance.fxml",contentArea);
    }

    public void productConfig(ActionEvent event) {

        replaceScene.replace("setting/productConfig.fxml",contentArea);
    }

}

