package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Products;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class AddSize implements Initializable {

    public TextField purchasePrice;
    public TextField productMrp;
    public TextField minSellPrice;
    public TextField productHeight;
    public TextField productWidth;
    public ComboBox<String> productSizeUnit;
    public TextField productQuantity;
    public ComboBox<String> productQuantityUnit;
    public Button bnAddSize;
    private Products products;
    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private Properties properties;
    private double profitPrice = 20; // in %


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = (Products) Main.primaryStage.getUserData();

        if (null == products) {

            System.out.println("Data Not Found");
            return;
        }

        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");

        productSizeUnit.setItems(method.getSizeUnit());
        productQuantityUnit.setItems(method.getSizeQuantityUnit());


        purchasePrice.textProperty().addListener((observableValue, old, newValue) -> {

            double purchasePrice_d = 0, minPrice = 0;

            try {
                purchasePrice_d = Double.parseDouble(purchasePrice.getText());
            } catch (NumberFormatException e) {
                method.show_popup("Enter valid Purchase Price", purchasePrice);
                return;
            }

            minPrice = purchasePrice_d + (profitPrice * purchasePrice_d / 100);

            minSellPrice.setText(String.valueOf(minPrice));

        });

    }

    public void enterPress(KeyEvent keyEvent) {

    }

    public void bnAddSize(ActionEvent event) {


        String heightS = productHeight.getText();
        String widthS = productWidth.getText();
        String quantityS = productQuantity.getText();
        String purchasePrice_s = purchasePrice.getText();
        String prodMrp = productMrp.getText();
        String minSellPrice_s = minSellPrice.getText();
        double mrp = 0, min_Sell_Price = 0, purchase_price = 0;

        if (purchasePrice_s.isEmpty()) {
            method.show_popup("ENTER PURCHASE PRICE ", purchasePrice);
            return;
        }
        try {
            purchase_price = Double.parseDouble(purchasePrice_s.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID PURCHASE PRICE", "ENTER VALID PURCHASE PRICE");
            e.printStackTrace();
            return;
        }

        if (prodMrp.isEmpty()) {
            method.show_popup("ENTER PRODUCT MRP ", productMrp);
            return;
        }
        try {
            mrp = Double.parseDouble(prodMrp.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID MRP", "ENTER VALID MRP");
            e.printStackTrace();
            return;
        }
        if (minSellPrice_s.isEmpty()) {
            method.show_popup("ENTER MIN SELLING PRICE ", minSellPrice);
            return;
        }
        try {
            min_Sell_Price = Double.parseDouble(minSellPrice_s.replaceAll("[^0-9.]", ""));

        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID MIN SELL PRICE", "ENTER VALID INVALID MIN SELL PRICE");
            e.printStackTrace();
            return;
        }

        if (heightS.isEmpty()) {
            method.show_popup("ENTER PRODUCT HEIGHT", productHeight);
            return;
        } else if (widthS.isEmpty()) {
            method.show_popup("ENTER PRODUCT WIDTH", productWidth);
            return;
        } else if (null == productSizeUnit.getValue()) {
            method.show_popup("CHOOSE SIZE UNIT", productSizeUnit);
            return;

        } else if (quantityS.isEmpty()) {

            method.show_popup("ENTER PRODUCT QUANTITY", productQuantity);
            return;
        } else if (null == productQuantityUnit.getValue()) {

            method.show_popup("CHOOSE QUANTITY UNIT", productQuantityUnit);
            return;
        }

        int height = 0;
        int width = 0;
        long quantity = 0;


        try {
            height = Integer.parseInt(heightS.replaceAll("[^0-9.]", ""));
            width = Integer.parseInt(widthS.replaceAll("[^0-9.]", ""));

        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID PRODUCT SIZE", "ENTER VALID HEIGHT AND WIDTH ");
            return;
        }
        try {
            quantity = Long.parseLong(quantityS.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID QUANTITY", "ENTER VALID QUANTITY");
            e.printStackTrace();
        }
        String sizeUnit = productSizeUnit.getValue();
        String quantityUnit = productQuantityUnit.getValue();


        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection) {
                customDialog.showAlertBox("Failed", "Connection Failed");
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("ADD_SIZE"));
            ps.setDouble(1, purchase_price);
            ps.setDouble(2, mrp);
            ps.setDouble(3, min_Sell_Price);
            ps.setInt(4, height);
            ps.setInt(5, width);
            ps.setString(6, sizeUnit);
            ps.setLong(7, quantity);
            ps.setString(8, quantityUnit);
            ps.setInt(9, products.getProductID());

            int res = ps.executeUpdate();

            if (res > 0) {

                Stage stage = new CustomDialog().stage2;

                if (stage.isShowing()) {
                    stage.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(connection, ps, null);
        }

    }

}