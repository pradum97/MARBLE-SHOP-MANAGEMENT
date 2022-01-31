package com.shop.management;

import com.shop.management.util.AppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class CustomDialog {

    public static Stage stage;
    public static Stage stage2;

    public Parent showFxmlDialog(String fxml_file, String title)  {

        try {
            Parent  parent = FXMLLoader.load(CustomDialog.class.getResource(fxml_file));
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON)));
            stage.initStyle(StageStyle.UTILITY);
            stage.setTitle(AppConfig.APPLICATION_NAME + " ( " + title + " ) ");
            stage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            return parent;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Alert showAlertBox(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.initOwner(Main.primaryStage);
        alert.showAndWait();

        return alert;
    }

    public Parent showFxmlDialog2(String fxml_file, String title)  {

        try {
            Parent  parent = FXMLLoader.load(CustomDialog.class.getResource(fxml_file));
            stage2 = new Stage();
            stage2.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON)));
            stage2.initStyle(StageStyle.UTILITY);
            stage2.setTitle(AppConfig.APPLICATION_NAME + " ( " + title + " ) ");
            stage2.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
            stage2.setScene(scene);
            stage2.setResizable(false);
            stage2.showAndWait();
            return parent;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
