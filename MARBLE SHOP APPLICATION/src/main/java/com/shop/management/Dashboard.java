package com.shop.management;

import com.shop.management.Controller.FeedbackDialog;
import com.shop.management.Controller.Login;
import com.shop.management.Controller.ViewFeedback;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Model.UserDetails;
import com.shop.management.util.AppConfig;
import com.shop.management.util.DBConnection;
import com.shop.management.Method.Method;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dashboard implements Initializable {

    public GridPane gridTopMenu;
    @FXML
    private GridPane gridPane;
    public BorderPane main_container;
    public Button bn_logout;
    public StackPane contentArea;
    public Label fullName;
    public ImageView userImage;
    public Label userRole;
    private Connection connection;
    private DBConnection dbConnection;
    private Properties properties;
    CustomDialog customDialog;
    Method method;
    private Main main;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        customDialog = new CustomDialog();
        main = new Main();
        replaceScene("dashboard/home.fxml");
        setSideMenuData();
        setTopMenuData();
        setCustomImage();
        setUserData();

    }

    private void setTopMenuData() {

        int cols = 2, colCnt = 0, rowCnt = 0;

        try {
            connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("TOP_MENU"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String item = rs.getString("menu_name");
                String icon_path = rs.getString("menu_icon_path");

                Hyperlink menu_button = new Hyperlink();

                menu_button.setId("menu_button");
                menu_button.setStyle("-fx-background-color: blue ; -fx-background-radius: 5");

                main_container.getStylesheets().add(String.valueOf(Main.class.getResource("css/menu.css")));
                ImageView icon = new ImageView();
                icon.setFitWidth(18);
                icon.setFitHeight(18);

                File file = new File("src/main/resources/com/shop/management/img/menu_icon/" + icon_path);
                InputStream is = new FileInputStream(file.getAbsolutePath());
                Image img = new Image(is);
                icon.setImage(img);
                menu_button.setText(item);

                menu_button.setOnAction(event -> {

                    String txt = ((Hyperlink) event.getSource()).getText();

                    switch (txt) {
                        case "ADD PRODUCT" -> {

                            try {
                                Parent parent = FXMLLoader.load(CustomDialog.class.getResource("dashboard/addProduct.fxml"));
                                Stage stage = new Stage();
                                stage.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON)));
                                stage.setTitle("ADD NEW PRODUCT");
                                Scene scene = new Scene(parent);
                                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
                                stage.setScene(scene);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.showAndWait();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        case "FEEDBACK" -> customDialog.showFxmlDialog("feedbackDialog.fxml", "FEEDBACK");
                        case "VIEW FEEDBACK" -> customDialog.showFxmlDialog2("viewFeedback.fxml", "ALL FEEDBACK");
                    }

                });

                menu_button.setGraphic(icon);

                gridTopMenu.add(menu_button, colCnt, rowCnt);
                colCnt++;

                if (colCnt > cols) {
                    rowCnt++;
                    colCnt = 0;
                }

            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Dashboard : " + e.getMessage());

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private void setUserData() {

        UserDetails userDetails = new GetUserProfile().getUser(Login.currentlyLogin_Id);

        if (null == userDetails) {
            customDialog.showAlertBox("Failed", "User Not Find Please Re-Login");
        } else {

            fullName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
            userRole.setText(userDetails.getRole());
            String imgPath = "src/main/resources/com/shop/management/img/userImages/" + userDetails.getUserImage();
            userImage.setImage(method.getImage(imgPath));
        }
    }

    private void setCustomImage() {

        InputStream is = null;
        try {
            is = new FileInputStream("src/main/resources/com/shop/management/img/menu_icon/logout_ic.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ImageView logout_img = new ImageView();
        Image img = new Image(is);
        logout_img.setImage(img);
        logout_img.setFitHeight(20);
        logout_img.setFitWidth(20);

        bn_logout.setGraphic(logout_img);

    }

    private void setSideMenuData() {
        int i = 0;

        try {
            connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("SIDE_MENU"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String item = rs.getString("menu_name");
                String icon_path = rs.getString("menu_icon_path");

                Hyperlink menu_button = new Hyperlink();

                menu_button.setId("menu_button");

                main_container.getStylesheets().add(String.valueOf(Main.class.getResource("css/menu.css")));
                ImageView icon = new ImageView();
                icon.setFitWidth(18);
                icon.setFitHeight(18);

                File file = new File("src/main/resources/com/shop/management/img/menu_icon/" + icon_path);
                InputStream is = new FileInputStream(file.getAbsolutePath());
                Image img = new Image(is);
                icon.setImage(img);
                menu_button.setText(item);

                menu_button.setOnAction(event -> {

                    String txt = ((Hyperlink) event.getSource()).getText();

                    switch (txt) {

                        case "HOME":
                            replaceScene("dashboard/home.fxml");
                            break;
                        case "PROFILE":
                            replaceScene("dashboard/userprofile.fxml");
                            break;
                        case "USERS":
                            replaceScene("dashboard/users.fxml");
                            break;
                        case "SETTING":
                            replaceScene("dashboard/setting.fxml");
                            break;
                        case "ALL PRODUCT":
                            replaceScene("dashboard/allProducts.fxml");
                            break;
                        case "SELL PRODUCT":
                            replaceScene("dashboard/sellProducts.fxml");
                            break;
                        case "SELL REPORT":
                            replaceScene("dashboard/sellReport.fxml");
                            break;
                        case "STOCK REPORT":
                            replaceScene("dashboard/stockReport.fxml");

                            break;
                    }


                });

                menu_button.setGraphic(icon);

                gridPane.add(menu_button, 0, i);
                gridPane.setVgap(10);

                i++;
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Dashboard : " + e.getMessage());

        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void replaceScene(String fxml_file_name) {


        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxml_file_name));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(parent);

        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    public void bnLogout(ActionEvent event) {
        ImageView image = new ImageView(method.getImage("src/main/resources/com/shop/management/img/icon/warning_ic.png"));
        image.setFitWidth(45);
        image.setFitHeight(45);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setGraphic(image);
        alert.setHeaderText("Are you sure you want to Logout");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            main.changeScene("login.fxml", "LOGIN HERE");
            Login.currentlyLogin_Id = 0;
        } else {
            alert.close();
        }

    }
}
