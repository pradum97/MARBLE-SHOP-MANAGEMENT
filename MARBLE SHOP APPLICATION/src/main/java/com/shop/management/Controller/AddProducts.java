package com.shop.management.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProducts implements Initializable {
    public TableView sizeTableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeTableView.setPlaceholder(new Label("Size Not Available"));


    }

    public void uploadImage_bn(ActionEvent event) {
    }

    public void submit_bn(ActionEvent event) {
    }


}
