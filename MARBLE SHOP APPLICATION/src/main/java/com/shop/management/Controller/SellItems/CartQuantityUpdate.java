package com.shop.management.Controller.SellItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.CloseConnection;
import com.shop.management.Method.Method;
import com.shop.management.Model.CartModel;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();

        cartModel = (CartModel) Main.primaryStage.getUserData();
        if (null == cartModel) {
            System.out.println("not found");
            return;
        }

        getProductStock();

    }

    private void setDefaultValue() {

        quantityTf.setText(String.valueOf(cartModel.getQuantity()));

        BigDecimal sell_price = BigDecimal.valueOf(cartModel.getSellingPrice());


        sellingPriceTf.setText(sell_price.stripTrailingZeros().toPlainString());

        availableQuantityL.setText(availableQuantity + " -" + quantity_unit);
        minSellPriceL.setText(minSellingPrice + " INR");
        mrpL.setText(productMrp + " INR");

        quantityUnit.getItems().add(cartModel.getQuantityUnit());
        quantityUnit.getSelectionModel().selectFirst();
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

            ps = connection.prepareStatement("UPDATE tbl_cart SET quantity = ? , quantity_unit = ? , sellprice = ? WHERE cart_id = ?");
            ps.setLong(1, quantity);
            ps.setString(2, quantity_Unit);
            ps.setDouble(3, sellingPrice);
            ps.setInt(4, cartModel.getCartId());

            int res = ps.executeUpdate();

            if (res > 0) {

                Stage stage = CustomDialog.stage;

                if (stage.isShowing()) {
                    stage.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection.closeConnection(connection, ps, rs);
        }

    }

    public void bnUpdate(ActionEvent event) {

        String quan = quantityTf.getText();
        String sellPrice = sellingPriceTf.getText();
        String unit = quantityUnit.getSelectionModel().getSelectedItem();

        long quantity = 0;
        double sellingPrice = 0;

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

        if (quantity <= availableQuantity) {

            if (sellingPrice >= cartModel.getMinSellPrice()) {

                if (sellingPrice <= cartModel.getProductMRP()) {

                    updateQuantity(quantity, unit, sellingPrice);

                } else {

                    method.show_popup("PLEASE ENTER LESS THEN " + cartModel.getProductMRP() + " OR " + cartModel.getProductMRP() + " RS.", sellingPriceTf);
                }


            } else {

                method.show_popup("PLEASE ENTER MORE THAN " + cartModel.getMinSellPrice() + " RS.", sellingPriceTf);
            }

        } else {

            method.show_popup("QUANTITY NOT AVAILABLE", quantityTf);
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

            ps = connection.prepareStatement("select quantity,product_mrp , quantity_unit ,min_sellingprice from tbl_product_stock where stock_id = ? ");
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
            CloseConnection.closeConnection(connection, ps, rs);
        }

        setDefaultValue();
    }
}
