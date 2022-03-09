package com.shop.management;

import com.shop.management.Controller.Login;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Method.Method;
import com.shop.management.Model.UserDetails;
import com.shop.management.util.AppConfig;
import com.shop.management.util.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dashboard implements Initializable {
    @FXML
    public GridPane gridTopMenu;
    public Label dateL;
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
    private CustomDialog customDialog;
    private Method method;
    private Main main;
    public static Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main_container.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/setting.css")).toExternalForm());
        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        customDialog = new CustomDialog();
        main = new Main();
        replaceScene("dashboard/saleReport.fxml");
        getMenuData();
        setCustomImage();
        setUserData();
        keyBoardShortcut();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
            dateL.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

    }

    private void keyBoardShortcut() {

        Scene scene = Main.primaryStage.getScene();

        scene.getAccelerators().put(
                KeyCombination.keyCombination("CTRL+A"),
                () -> showAddProductDialog()
        );

        scene.getAccelerators().put(
                KeyCombination.keyCombination("CTRL+F"),
                this::addFeedback
        );

        scene.getAccelerators().put(
                KeyCombination.keyCombination("CTRL+SHIFT+L"),
                () -> bnLogout(null)
        );
    }

    private void showAddProductDialog() {

        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(CustomDialog.class.getResource("dashboard/addProduct.fxml")));
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON)));
            stage.setTitle("ADD NEW PRODUCT");
            stage.setMaximized(false);
            Scene scene = new Scene(parent);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Main.primaryStage);
            stage.showAndWait();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addFeedback() {

        customDialog.showFxmlDialog("feedbackDialog.fxml", "FEEDBACK");

    }


    private void onClickAction( MenuItem appearance, Menu product, MenuItem gst, MenuItem discount, MenuItem help,
                               MenuItem shopData, MenuItem category) {

        appearance.setOnAction(event -> customDialog.showFxmlDialog2("setting/appearance.fxml", "APPEARANCE"));

        discount.setOnAction(event -> {

            customDialog.showFxmlDialog2("setting/discountConfig.fxml", "DISCOUNT");


        });


        gst.setOnAction(event -> customDialog.showFxmlDialog2("setting/gstConfig.fxml", "GST"));

        help.setOnAction(event -> customDialog.showFxmlDialog2("setting/help.fxml", "HELP"));
        shopData.setOnAction(event -> customDialog.showFxmlDialog2("shopDetails.fxml", ""));
        category.setOnAction(event -> customDialog.showFxmlDialog2("category.fxml", "CATEGORY"));



    }

    private void showDialog(String fxmlName, String title, double height,
                            double width, StageStyle utility) {

        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(CustomDialog.class.getResource(fxmlName)));
            stage = new Stage();
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppConfig.APPLICATION_ICON)));
            stage.setTitle(title);
            stage.setMaximized(false);
            Scene scene = new Scene(parent, width, height);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/main.css")).toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(Main.primaryStage);
            stage.initStyle(utility);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void getMenuData() {
        int i = 0;
        int cols = 5, colCnt = 0, rowCnt = 0;

        try {
            connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(properties.getProperty("SIDE_MENU"));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String item = rs.getString("menu_name");
                String icon_path = rs.getString("menu_icon_path");
                String menu_location = rs.getString("menu_location");

                switch (menu_location) {

                    case "SIDE" -> {

                        Hyperlink menu_button = new Hyperlink();
                        menu_button.setId("menu_button");

                        main_container.getStylesheets().add(String.valueOf(Main.class.getResource("css/menu.css")));
                        ImageView icon = new ImageView();
                        icon.setFitWidth(18);
                        icon.setFitHeight(18);
                        icon.setImage(method.getImage("src/main/resources/com/shop/management/img/menu_icon/" + icon_path));
                        menu_button.setText(item);

                        menu_button.setOnAction(event -> {

                            String txt = ((Hyperlink) event.getSource()).getText();

                            switch (txt) {
                                case "HOME" -> replaceScene("dashboard/home.fxml");
                                case "PROFILE" ->{
                                    Main.primaryStage.setUserData(Login.currentlyLogin_Id);
                                    replaceScene("dashboard/userprofile.fxml");
                                }
                                case "USERS" -> replaceScene("dashboard/users.fxml");
                                case "ALL PRODUCTS" -> replaceScene("dashboard/allProducts.fxml");
                                case "SALE PRODUCTS" -> replaceScene("dashboard/saleProducts.fxml");
                                case "SALES REPORT" -> replaceScene("dashboard/saleReport.fxml");
                                case "STOCK REPORT" -> replaceScene("dashboard/stockReport.fxml");
                            }


                        });

                        menu_button.setGraphic(icon);

                        gridPane.add(menu_button, 0, i);
                        gridPane.setVgap(10);

                        i++;
                    }

                    case "TOP" -> {

                        switch (item) {
                            case "SETTING" -> {

                                MenuButton menu_button = new MenuButton();

                                // general --start
                                Menu gen = new Menu("GENERAL");
                                MenuItem category = new MenuItem("CATEGORY");
                                MenuItem appearance = new MenuItem("APPEARANCE");
                                appearance.setVisible(false);
                                gen.getItems().addAll(category,appearance);

                               // general -- end

                                MenuItem shopData = new MenuItem("SHOP DETAILS");
                                MenuItem help = new MenuItem("HELP");


                                // product -- start
                                Menu product = new Menu("PRODUCT");
                                MenuItem gst = new MenuItem("GST");
                                MenuItem discount = new MenuItem("DISCOUNT");
                                product.getItems().addAll(gst, discount);

                                // product --  end

                                menu_button.getItems().addAll(gen, product,shopData, help);

                                onClickAction( appearance, product, gst, discount, help,shopData,category);


                                ImageView icon = new ImageView();
                                icon.setFitWidth(18);
                                icon.setFitHeight(18);
                                menu_button.setStyle("-fx-cursor: hand;-fx-background-color: #0881ea ; -fx-background-radius: 5");

                                icon.setImage(method.getImage("src/main/resources/com/shop/management/img/menu_icon/" + icon_path));

                                menu_button.setGraphic(icon);


                                gridTopMenu.add(menu_button, colCnt, rowCnt);
                                colCnt++;

                                if (colCnt > cols) {
                                    rowCnt++;
                                    colCnt = 0;
                                }

                            }
                            case "FEEDBACK" -> {

                                MenuButton menu_button = new MenuButton("");

                                MenuItem addFeedback = new MenuItem("➕ ADD FEEDBACK");
                                MenuItem view_feedback = new MenuItem("VIEW FEEDBACK");


                                menu_button.getItems().addAll(addFeedback, view_feedback);

                                addFeedback.setOnAction(event -> addFeedback());

                                view_feedback.setOnAction(event -> showDialog("viewFeedback.fxml", "ALL FEEDBACK", 650, 750, StageStyle.DECORATED));


                                ImageView icon = new ImageView();
                                icon.setFitWidth(18);
                                icon.setFitHeight(18);


                                icon.setImage(method.getImage("src/main/resources/com/shop/management/img/menu_icon/" + icon_path));

                                menu_button.setGraphic(icon);


                                gridTopMenu.add(menu_button, colCnt, rowCnt);
                                colCnt++;
                                if (colCnt > cols) {
                                    rowCnt++;
                                    colCnt = 0;
                                }

                                break;
                            }

                            case "ADD PRODUCT" -> {

                                Label button = new Label("➕ ADD PRODUCT");
                                button.setStyle("-fx-padding: 5 10 5 10 ; -fx-background-color: #0881ea ; -fx-text-fill: white;" +
                                        "-fx-background-radius: 5 ; -fx-cursor: hand");

                                button.setOnMouseClicked(event ->  showAddProductDialog());

                                gridTopMenu.add(button, colCnt, rowCnt);
                                colCnt++;

                                if (colCnt > cols) {
                                    rowCnt++;
                                    colCnt = 0;
                                }

                            }
                        }
                    }
                }

            }
        } catch (SQLException e) {
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
