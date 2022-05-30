package com.shop.management;

import com.shop.management.Controller.Login;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Method.Method;
import com.shop.management.Model.UserDetails;
import com.shop.management.util.AppConfig;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private CustomDialog customDialog;
    private Main main;
    public static Stage stage;
    private Properties propRead;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main_container.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/setting.css")).toExternalForm());
        dbConnection = new DBConnection();
        PropertiesLoader propLoader = new PropertiesLoader();
        propRead = propLoader.getReadProp();
        customDialog = new CustomDialog();
        main = new Main();
        getMenuData();
        setCustomImage();
        setUserData();
        keyBoardShortcut();
    }

    private void keyBoardShortcut() {

        Scene scene = Main.primaryStage.getScene();

        scene.getAccelerators().put(
                KeyCombination.keyCombination("CTRL+A"),
                this::showAddProductDialog
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
            stage.getIcons().add(new ImageLoader().load(AppConfig.APPLICATION_ICON));
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

    private void onClickAction(MenuItem appearance, Menu product, MenuItem gst, MenuItem discount, MenuItem licence,
                               MenuItem shopData, MenuItem category, MenuItem profile, MenuItem users, MenuItem stockControl,
                               MenuItem supplier, MenuItem purchaseHistory, MenuItem customer, MenuItem backup) {

        appearance.setOnAction(event -> customDialog.showFxmlDialog2("setting/appearance.fxml", "APPEARANCE"));
        discount.setOnAction(event -> {

            customDialog.showFxmlDialog2("setting/discountConfig.fxml", "DISCOUNT");
            if (Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase())) {
                refreshPage();
            }
        });
        gst.setOnAction(event -> {
            customDialog.showFxmlDialog2("setting/gstConfig.fxml", "GST");
            if (Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase())) {
                refreshPage();
            }
        });
        licence.setOnAction(event -> customDialog.showFxmlDialog2("license/licenseMain.fxml", "My Subscription"));
        backup.setOnAction(event -> customDialog.showFxmlDialog2("db_backup.fxml", "    BACKUP"));
        customer.setOnAction(event -> customDialog.showFxmlFullDialog("setting/customer.fxml", "ALL CUSTOMER"));
        shopData.setOnAction(event -> customDialog.showFxmlDialog2("shopDetails.fxml", ""));
        category.setOnAction(event -> customDialog.showFxmlDialog2("category.fxml", "CATEGORY"));
        users.setOnAction(event -> {
            customDialog.showFxmlFullDialog("dashboard/users.fxml", "ALL USERS");

            if (Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase())) {
                refreshPage();
            }

        });
        stockControl.setOnAction(event -> customDialog.showFxmlFullDialog("setting/stockControl.fxml", "STOCK SETTING"));
        supplier.setOnAction(event -> customDialog.showFxmlFullDialog("stock/allSupplier.fxml", "ALL SUPPLIER"));
        purchaseHistory.setOnAction(event -> customDialog.showFxmlFullDialog("purchaseHistory.fxml", ""));
        profile.setOnAction(event -> {

            Main.primaryStage.setUserData(Login.currentlyLogin_Id);
            customDialog.showFxmlDialog2("dashboard/userprofile.fxml", "MY PROFILE");
            if (Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase())) {
                refreshPage();
            }
        });
    }

    private void refreshPage() {
         replaceScene("dashboard/home.fxml");
        getMenuData();
        setCustomImage();
        setUserData();
    }

    private void showDialog(String fxmlName, String title, double height,
                            double width, StageStyle utility) {

        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(CustomDialog.class.getResource(fxmlName)));
            stage = new Stage();
            stage.getIcons().add(new ImageLoader().load(AppConfig.APPLICATION_ICON));
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

            fullName.setText((userDetails.getFirstName() + " " + userDetails.getLastName()).toUpperCase());

            userRole.setText(userDetails.getRole().toUpperCase());
            String imgPath = "img/Avatar/" + userDetails.getUserImage();
            userImage.setImage(new ImageLoader().load(imgPath));
        }
    }

    private void setCustomImage() {

        ImageView logout_img = new ImageView();
        logout_img.setImage(new ImageLoader().load("img/menu_icon/logout_ic.png"));
        logout_img.setFitHeight(20);
        logout_img.setFitWidth(20);

        bn_logout.setGraphic(logout_img);
    }

    private void getMenuData() {
        int i = 0;
        int cols = 5, colCnt = 0, rowCnt = 0;

        try {
            connection = dbConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(propRead.getProperty("SIDE_MENU"));
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
                        icon.setImage(new ImageLoader().load("img/menu_icon/" + icon_path));

                        if (Objects.equals(Login.currentRoleName.toLowerCase(), "seller".toLowerCase())) {

                            switch (item) {
                                case "ALL PRODUCTS", "SALE PRODUCTS", "STOCK REPORT", "INVOICE",
                                        "RETURN PRODUCT", "PROPOSAL" -> {
                                    menu_button.setText(item);
                                    replaceScene("dashboard/allProducts.fxml");
                                    menu_button.setGraphic(icon);
                                    gridPane.add(menu_button, 0, i);
                                    gridPane.setVgap(10);

                                    i++;
                                }
                            }

                        } else {

                            menu_button.setText(item);
                            menu_button.setGraphic(icon);
                            gridPane.add(menu_button, 0, i);
                            gridPane.setVgap(10);
                            replaceScene("dashboard/home.fxml");

                            i++;
                        }

                        menu_button.setOnAction(event -> {

                            String txt = ((Hyperlink) event.getSource()).getText();

                            switch (txt) {
                                case "HOME" -> replaceScene("dashboard/home.fxml");
                                case "ALL PRODUCTS" -> replaceScene("dashboard/allProducts.fxml");
                                case "SALE PRODUCTS" -> replaceScene("dashboard/saleProducts.fxml");
                                case "SALES REPORT" -> replaceScene("dashboard/saleReport.fxml");
                                case "STOCK REPORT" -> replaceScene("dashboard/stockReport.fxml");
                                case "INVOICE" -> replaceScene("dashboard/invoiceReport.fxml");
                                case "RETURN PRODUCT" -> replaceScene("returnItems/returnProduct.fxml");
                                case "PROPOSAL" -> replaceScene("proposal/proposalMain.fxml");
                            }


                        });
                    }

                    case "TOP" -> {
                        switch (item) {
                            case "SETTING" -> {

                                MenuButton menu_button = new MenuButton();
                                // general --start
                                Menu gen = new Menu("GENERAL");
                                MenuItem appearance = new MenuItem("APPEARANCE");
                                MenuItem stockControl = new MenuItem("STOCK CONTROL");
                                MenuItem supplier = new MenuItem("SUPPLIER");
                                appearance.setVisible(false);
                                gen.getItems().addAll(appearance, stockControl, supplier);

                                // general -- end
                                MenuItem shopData = new MenuItem("SHOP DETAILS");
                                MenuItem profile = new MenuItem("PROFILE");
                                MenuItem users = new MenuItem("USERS");
                                MenuItem purchaseHistory = new MenuItem("PURCHASE HISTORY");
                                MenuItem customer = new MenuItem("VIEW CUSTOMER");
                                MenuItem myLicense = new MenuItem("MY LICENSE");
                                MenuItem backup = new MenuItem("BACKUP");


                                users.setVisible(Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase()));


                                // product -- start
                                Menu product = new Menu("PRODUCT");
                                MenuItem category = new MenuItem("CATEGORY");
                                MenuItem gst = new MenuItem("GST");
                                MenuItem discount = new MenuItem("DISCOUNT");
                                product.getItems().addAll(category, gst, discount);

                                // product --  end

                                menu_button.getItems().addAll(gen, product, profile, users, shopData, purchaseHistory, customer, myLicense,backup);

                                onClickAction(appearance, product, gst, discount, myLicense, shopData, category,
                                        profile, users, stockControl, supplier, purchaseHistory, customer,backup);


                                ImageView icon = new ImageView();
                                icon.setFitWidth(18);
                                icon.setFitHeight(18);

                                icon.setImage(new ImageLoader().load("img/menu_icon/" + icon_path));

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


                                icon.setImage(new ImageLoader().load("img/menu_icon/" + icon_path));

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
                                button.setStyle("-fx-padding: 9 13 9 13 ; -fx-background-color: #053a67 ; -fx-text-fill: white;" +
                                        "-fx-background-radius: 6 ;-fx-focus-traversable: false; -fx-cursor: hand ; -fx-font-family: 'Arial Black'");

                                button.setOnMouseClicked(event -> showAddProductDialog());

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
        ImageView image = new ImageView(new ImageLoader().load("img/icon/warning_ic.png"));
        image.setFitWidth(45);
        image.setFitHeight(45);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setGraphic(image);
        alert.setHeaderText("ARE YOU SURE YOU WANT TO LOGOUT ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            main.changeScene("login.fxml", "LOGIN HERE");
            Login.currentlyLogin_Id = 0;
            Login.currentRoleName = "";
            Login.currentRole_Id = 0;
        } else {
            alert.close();
        }

    }
}
