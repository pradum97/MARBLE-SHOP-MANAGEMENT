package com.shop.management.Controller;

import com.shop.management.Main;
import javafx.application.Platform;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

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