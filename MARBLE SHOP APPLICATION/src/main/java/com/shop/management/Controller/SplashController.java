package com.shop.management.Controller;

import com.shop.management.Main;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SplashController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        splash();
    }
    private void splash() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    System.out.println(e);
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        new Main().changeScene("login.fxml","Login Here");
                    }
                });
            }
        }.start();
    }
}