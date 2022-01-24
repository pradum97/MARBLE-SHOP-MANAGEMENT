package com.shop.management;

import com.shop.management.Method.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    static Stage primaryStage;
    private AppConfig appConfig;
    static Stage splash_stage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        splash_stage = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("splash_screen.fxml"));
        Scene scene = new Scene(root, 600, 400);
        splash_stage.initStyle(StageStyle.UNDECORATED);
        splash_stage.setScene(scene);
        splash_stage.show();
    }

    public void changeScene(String fxml, String title) {

        try {

            if (null != primaryStage) {
                //  primaryStage.initModality(Modality.NONE);

                primaryStage.setResizable(true);
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(appConfig.APPLICATION_ICON)));
                primaryStage.setTitle(appConfig.APPLICATION_NAME);
                Parent pane = FXMLLoader.load(getClass().getResource(fxml));
                Scene scene = new Scene(pane);
                primaryStage.setScene(scene);
                primaryStage.setTitle(appConfig.APPLICATION_NAME + " ( " + title + " ) ");
                splash_stage.hide();
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