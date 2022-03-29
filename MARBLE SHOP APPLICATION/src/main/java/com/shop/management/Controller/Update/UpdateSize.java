package com.shop.management.Controller.Update;

import com.shop.management.Controller.Login;
import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Stock;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class UpdateSize implements Initializable {


    public TextField purchasePrice;
    public TextField productMrp;
    public TextField minSellPrice;
    public TextField productHeight;
    public TextField productWidth;
    public ComboBox<String> productSizeUnit;
    public TextField productQuantity;
    public ComboBox<String> productQuantityUnit;
    public Button bnAddSize;
    private Stock stock;
    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private Properties properties;
    private double profitPrice = 20; // in %

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stock = (Stock) Main.primaryStage.getUserData();

        if (null == stock) {

            System.out.println("Data Not Found");
            return;
        }

        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        properties = new PropertiesLoader().load("query.properties");

        setPreviousData();


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

    private void setPreviousData() {

        BigDecimal h = BigDecimal.valueOf(stock.getHeight());
        BigDecimal w = BigDecimal.valueOf(stock.getWidth());

        purchasePrice.setText(String.valueOf(stock.getPurchasePrice()));
        productMrp.setText(String.valueOf(stock.getProductMRP()));
        minSellPrice.setText(String.valueOf(stock.getMinSellingPrice()));

        productHeight.setText(h.stripTrailingZeros().toPlainString());
        productWidth.setText(w.stripTrailingZeros().toPlainString());
        productQuantity.setText(String.valueOf(stock.getQuantity()));

        productSizeUnit.getItems().add(stock.getSizeUnit());
        productQuantityUnit.getItems().add(stock.getQuantityUnit());

        productQuantityUnit.getSelectionModel().selectFirst();
        productSizeUnit.getSelectionModel().selectFirst();

        productSizeUnit.setItems(method.getSizeUnit());
        productQuantityUnit.setItems(method.getSizeQuantityUnit());


        productQuantity.setOnMouseClicked(mouseEvent -> {

            if (!productQuantity.isEditable()){
                quantityDialog();
            }
        });
    }

    private void quantityDialog (){

        ImageView image = new ImageView(new ImageLoader().load("img/icon/warning_ic.png"));
        image.setFitWidth(45);
        image.setFitHeight(45);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setGraphic(image);
        alert.setHeaderText("Note : If you update from it, it will not be updated in the purchase history.");
        alert.setContentText("If you've added the wrong quantity, you can update from here.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {

            productQuantity.setEditable(true);

        } else {
            alert.close();
        }
    }

    public void enterPress(KeyEvent keyEvent) {

    }

    public void bnUpdateSize(ActionEvent event) {


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
        if (purchase_price > mrp) {
            method.show_popup("ENTER MRP MORE THAN PURCHASE PRICE", productMrp);
            return;
        } else if (minSellPrice_s.isEmpty()) {
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

        if (purchase_price > min_Sell_Price) {
            method.show_popup("ENTER MINIMUM SELLING PRICE MORE THAN PURCHASE PRICE", minSellPrice);
            return;
        } else if ( min_Sell_Price > mrp){
            method.show_popup("ENTER MINIMUM SELLING PRICE LESS THAN MRP", minSellPrice);
            return;

        }else if (heightS.isEmpty()) {
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

        double height = 0, width = 0;
        long quantity = 0;

        try {
            height = Double.parseDouble(heightS.replaceAll("[^0-9.]", ""));
            width = Double.parseDouble(widthS.replaceAll("[^0-9.]", ""));

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

            ps = connection.prepareStatement(properties.getProperty("UPDATE_SIZE"));
            ps.setDouble(1, purchase_price);
            ps.setDouble(2, mrp);
            ps.setDouble(3, min_Sell_Price);
            ps.setDouble(4, height);
            ps.setDouble(5, width);
            ps.setString(6, sizeUnit);
            ps.setLong(7, quantity);
            ps.setString(8, quantityUnit);
            ps.setInt(9, stock.getStockID());

            int res = ps.executeUpdate();

            if (res > 0) {
                System.out.println("sucess");

                clearCart(connection);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(null, ps, null);
        }

    }

    private void clearCart(Connection con ) {

        Statement ps = null;

        String deleteQuery = "DELETE FROM tbl_cart";
        String sequenceOrder = "ALTER SEQUENCE tbl_cart_cart_id_seq RESTART WITH 1;";

        try {

            if (null == con) {
                return;
            }
            ps = con.createStatement();
            ps.addBatch(deleteQuery);
            ps.addBatch(sequenceOrder);

            ps.executeBatch();
            Stage stage = CustomDialog.stage2;

            if (stage.isShowing()) {
                stage.close();
            }

        } catch (SQLException e) {
            customDialog.showAlertBox("ERROR", "Failed to Clear Cart !");
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(con, null, null);
            try {
                if (ps != null) {
                    ps.close();
                }
                if (null != con){
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void cancel(ActionEvent event) {

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if (stage.isShowing()){
            stage.close();
        }
    }
}
