package com.shop.management;

import com.shop.management.Method.TableCreate;
import com.shop.management.util.AppConfig;
import com.shop.management.util.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        new TableCreate().createLicenseTable();
        primaryStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("login.fxml")));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON))));
        stage.setTitle(AppConfig.APPLICATION_NAME);
        stage.setMaximized(true);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void changeScene(String fxml, String title) {

        try {
            if (null != primaryStage && primaryStage.isShowing()) {
                Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
                primaryStage.getScene().setRoot(pane);
                primaryStage.setTitle(AppConfig.APPLICATION_NAME + " ( " + title + " ) ");
                primaryStage.show();
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
    public static void main(String... args) {

        launch(args);
    }


}