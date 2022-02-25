package com.shop.management.Controller;

import com.shop.management.Main;
import com.shop.management.Method.Method;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class Full_Image implements Initializable {


    public ImageView img;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String path =(String) Main.primaryStage.getUserData();

        if (null != path){
            img.setImage(new Method().getImage(path));
        }
    }
}
