package com.shop.management;

import com.shop.management.util.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    static Stage primaryStage;
    private AppConfig appConfig;
    static Stage splash_stage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        /*splash_stage = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("splash_screen.fxml"));
        Scene scene = new Scene(root, 600, 400);
        splash_stage.initStyle(StageStyle.UNDECORATED);
        splash_stage.setScene(scene);
        primaryStage.setMinWidth(600.0);
        primaryStage.setMinHeight(500.0);
        splash_stage.show();*/

        changeScene("dashboard.fxml","test");


    }

    public void changeScene(String fxml, String title) {

        try {

            if (null != primaryStage) {



                primaryStage.setResizable(true);
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(appConfig.APPLICATION_ICON)));
                primaryStage.setTitle(appConfig.APPLICATION_NAME);
                Parent pane = FXMLLoader.load(getClass().getResource(fxml));
                Scene scene = new Scene(pane,1000.0,500.0);
                primaryStage.setScene(scene);
                primaryStage.setY(100.0);
                primaryStage.setX(100.0);
                primaryStage.setMinWidth(1000.0);
                primaryStage.setMinHeight(500.0);
                primaryStage.setTitle(appConfig.APPLICATION_NAME + " ( " + title + " ) ");
               // splash_stage.hide();
                primaryStage.show();

            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}