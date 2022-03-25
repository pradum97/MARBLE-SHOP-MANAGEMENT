package com.shop.management.Controller.SellItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Quantity;
import com.shop.management.Model.Stock;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class QuantityDialog implements Initializable {

    public Label size;
    public Label purchasePrice;
    public Label productMrp;
    public Label minSellingPrice;
    public Label quantity_L;
    public TextField quantityTf;
    public ComboBox<String> quantityUnit;
    public TextField sellingPriceTf;
    public HBox sellPriceContainer;
    public Button bnAddCart;
    public Label errorL;
    private Stock stock;
    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private int stock_id;
    private int requiredQuantity;
    private long avlQty;

    private final static String UPDATE_QUANTITY = "UPDATE QUANTITY";
    private final static String ADD_CART = "➕ ADD TO CART";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        stock = (Stock) Main.primaryStage.getUserData();

        if (null == stock) {
            return;
        }

        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();

        getStockSetting();
        checkStockExist();
    }

    private void checkStockExist() {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            ps = connection.prepareStatement("SELECT stock_id, sellprice ,quantity , sellprice ," +
                    " quantity_unit FROM TBL_CART WHERE STOCK_ID = ?");
            ps.setInt(1, stock.getStockID());

            rs = ps.executeQuery();

            if (rs.next()) {

                errorL.setVisible(true);
                errorL.setText("( This item is already in the cart )");

                stock_id = rs.getInt("stock_id");

                quantityTf.setText(String.valueOf(rs.getLong("quantity")));
                sellingPriceTf.setText(String.valueOf(rs.getDouble("sellprice")));

                quantityUnit.getItems().add(rs.getString("quantity_unit"));
                quantityUnit.getSelectionModel().selectFirst();

                double sellingPrice = rs.getDouble("sellprice");

                BigDecimal sell_price = BigDecimal.valueOf(sellingPrice);


                sellingPriceTf.setText(sell_price.stripTrailingZeros().toPlainString());

                bnAddCart.setText(UPDATE_QUANTITY);

            } else {
                errorL.setText("");
                errorL.setVisible(false);
                errorL.managedProperty().bind(errorL.visibleProperty());
                BigDecimal mrp = BigDecimal.valueOf(stock.getProductMRP());
                sellingPriceTf.setText(mrp.stripTrailingZeros().toPlainString());

                quantityUnit.getItems().add(stock.getQuantityUnit());
                quantityUnit.getSelectionModel().selectFirst();

                bnAddCart.setText(ADD_CART);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    private void setDefaultValue() {

        String inr = " INR";
        size.setText(stock.getFullSize());
        purchasePrice.setText(stock.getPurchasePrice() + inr);
        productMrp.setText(stock.getProductMRP() + inr);
        minSellingPrice.setText(stock.getMinSellingPrice() + inr);

        avlQty = (stock.getQuantity()-requiredQuantity);

        quantity_L.setText(avlQty+"-"+stock.getFullQuantity().split(" - ")[1]+" ( Tot Avl : "+stock.getQuantity()+" - Required : "+requiredQuantity+" )");
    }

    public void bnAddToCart(ActionEvent event) {

        String quan = quantityTf.getText();
        String sellPrice = sellingPriceTf.getText();
        String quantity_Unit = quantityUnit.getSelectionModel().getSelectedItem();

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
        } else if (quantity_Unit.isEmpty()) {
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

            if (sellingPrice >= stock.getMinSellingPrice()) {

                if (sellingPrice <= stock.getProductMRP()) {

                    switch (bnAddCart.getText()) {
                        case UPDATE_QUANTITY -> {

                            updateQuantity(quantity, quantity_Unit, sellingPrice);
                        }
                        case ADD_CART -> {

                            addToCart(quantity, quantity_Unit, sellingPrice);

                        }
                    }

                } else {

                    method.show_popup("PLEASE ENTER LESS THEN " + stock.getProductMRP() + " OR " + stock.getProductMRP() + " RS.", sellingPriceTf);
                }
            } else {

                method.show_popup("PLEASE ENTER MORE THAN " + stock.getMinSellingPrice() + " RS.", sellingPriceTf);
            }

        } else {

            method.show_popup("QUANTITY NOT AVAILABLE! Tot Avl : "+avlQty+"-"+stock.getFullQuantity().split(" - ")[1], quantityTf);
        }
    }

    private void getStockSetting() {
        requiredQuantity = 0;

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            String query = "SELECT REQUIRED FROM STOCK_CONTROL";

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

    private void updateQuantity(long quantity, String quantity_Unit, double sellingPrice) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            ps = connection.prepareStatement("UPDATE tbl_cart SET quantity = ? , quantity_unit = ? , sellprice = ? WHERE stock_id = ?");
            ps.setLong(1, quantity);
            ps.setString(2, quantity_Unit);
            ps.setDouble(3, sellingPrice);
            ps.setInt(4, stock_id);
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
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    private void addToCart(long quantity, String quantity_Unit, double sellingPrice) {

        Quantity quantity1 = new Quantity(quantity, quantity_Unit, sellingPrice);

        Main.primaryStage.setUserData(quantity1);
        Stage stage = CustomDialog.stage;

        if (stage.isShowing()) {
            stage.close();
        }

    }

    public void enterPress(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            //do something

            bnAddToCart(null);
        }
    }
}
