package com.shop.management.Controller.SellItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.CartModel;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class CartQuantityUpdate implements Initializable {

    public TextField quantityTf;
    public ComboBox<String> quantityUnit;
    public TextField sellingPriceTf;
    public Label availableQuantityL;
    public Label minSellPriceL;
    public Label mrpL;
    private Method method;
    private CartModel cartModel;
    private double minSellingPrice, productMrp;
    private String quantity_unit;
    private long availableQuantity;
    private int requiredQuantity;
    private long avlQty;
    private Properties propUpdate, propRead;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        PropertiesLoader propLoader = new PropertiesLoader();
        propUpdate = propLoader.getUpdateProp();
        propRead = propLoader.getReadProp();

        cartModel = (CartModel) Main.primaryStage.getUserData();
        if (null == cartModel) {
            System.out.println("not found");
            return;
        }

        getStockSetting();
        getProductStock();

    }

    private void setDefaultValue() {

        quantityTf.setText(String.valueOf(cartModel.getQuantity()));
        BigDecimal sell_price = BigDecimal.valueOf(cartModel.getSellingPrice());
        sellingPriceTf.setText(sell_price.stripTrailingZeros().toPlainString());
        minSellPriceL.setText(minSellingPrice + " INR");
        mrpL.setText(productMrp + " INR");
        quantityUnit.getItems().add(cartModel.getQuantityUnit());
        quantityUnit.getSelectionModel().selectFirst();

        avlQty = (availableQuantity - requiredQuantity);

        availableQuantityL.setText(avlQty + "-" + quantity_unit + " ( Tot Avl : " + availableQuantity + "-" + quantity_unit + " - Required : " + requiredQuantity + " )");
    }

    public void enterPress(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            bnUpdate(null);
        }
    }

    private void updateQuantity(long quantity, String quantity_Unit, double sellingPrice) {


        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            ps = connection.prepareStatement(propUpdate.getProperty("UPDATE_CART_QUANTITY"));
            ps.setLong(1, quantity);
            ps.setString(2, quantity_Unit);
            ps.setDouble(3, sellingPrice);
            ps.setInt(4, cartModel.getCartId());

            int res = ps.executeUpdate();

            if (res > 0) {
                Stage stage = CustomDialog.stage2;

                if (stage.isShowing()) {
                    stage.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

    }

    public void bnUpdate(ActionEvent event) {

        String quan = quantityTf.getText();
        String sellPrice = sellingPriceTf.getText();
        String unit = quantityUnit.getSelectionModel().getSelectedItem();

        long quantity;
        double sellingPrice;

        if (quan.isEmpty()) {
            method.show_popup("ENTER QUANTITY", quantityTf);
            return;
        }

        try {
            quantity = Long.parseLong(quan.replaceAll("[^0-9.]", ""));

        } catch (NumberFormatException e) {
            method.show_popup("ENTER VALID QUANTITY", quantityUnit);
            return;
        }

        if (quantity < 1) {
            method.show_popup("ENTER VALID QUANTITY", quantityUnit);
            return;

        } else if (sellPrice.isEmpty()) {

            method.show_popup("ENTER SELLING PRICE", sellingPriceTf);
            return;
        } else if (unit.isEmpty()) {
            method.show_popup("SELECT UNIT", quantityUnit);
            return;
        }

        try {
            sellingPrice = Double.parseDouble(sellPrice.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            method.show_popup("ENTER VALID PRICE", sellingPriceTf);
            return;
        }
        if (sellingPrice < 1) {
            method.show_popup("ENTER VALID PRICE", sellingPriceTf);
            return;

        }
        if (quantity <= avlQty) {
            if (sellingPrice >= cartModel.getMinSellPrice()) {

                if (sellingPrice <= cartModel.getProductMRP()) {

                    updateQuantity(quantity, unit, sellingPrice);

                } else {
                    method.show_popup("PLEASE ENTER LESS THEN " + cartModel.getProductMRP(), sellingPriceTf);
                }
            } else {
                method.show_popup("PLEASE ENTER MORE THAN " + cartModel.getMinSellPrice() + " RS.", sellingPriceTf);
            }
        } else {
            String msg = "QUANTITY NOT AVAILABLE \n AVAILABLE QTY : " + avlQty + " -" + quantity_unit;

            method.show_popup(msg, quantityTf);
        }
    }

    private void getProductStock() {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("Connection failed");
                return;
            }

            ps = connection.prepareStatement(propRead.getProperty("READ_PRODUCT_STOCK_IN_CART_UPDATE"));
            ps.setInt(1, cartModel.getProductStockID());

            rs = ps.executeQuery();

            if (rs.next()) {

                availableQuantity = rs.getLong("quantity");
                quantity_unit = rs.getString("quantity_unit");
                minSellingPrice = rs.getDouble("min_sellingprice");
                productMrp = rs.getDouble("product_mrp");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

        setDefaultValue();
    }

    private void getStockSetting() {
        requiredQuantity = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            String query = propRead.getProperty("READ_STOCK_CONTROL");

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            if (rs.next()) {

                requiredQuantity = rs.getInt("REQUIRED");
                setDefaultValue();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    public void cancel(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (stage.isShowing()) {
            stage.close();
        }
    }
}
