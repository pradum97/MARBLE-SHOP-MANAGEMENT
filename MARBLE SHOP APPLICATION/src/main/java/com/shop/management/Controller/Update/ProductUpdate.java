package com.shop.management.Controller.Update;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.GetDiscount;
import com.shop.management.Method.GetTax;
import com.shop.management.Method.Method;
import com.shop.management.Model.Discount;
import com.shop.management.Model.Products;
import com.shop.management.Model.TAX;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class ProductUpdate implements Initializable {

    public TextField productName;
    public ComboBox<String> productCategory;
    public ComboBox<Discount> productDiscount;
    public ComboBox<TAX> productTax;
    public ComboBox<String> productColor;
    public ComboBox<String> productType;
    public TextField productDescription;
    private Products products;
    private DBConnection dbconnection;
    private Method method;
    private CustomDialog customDialog;
    private Properties properties;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbconnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");

        products = (Products) Main.primaryStage.getUserData();
        if (null == products) {
            customDialog.showAlertBox("Failed", "Data Not Found");
            return;

        }
        setPreviousData();


    }

    private void setPreviousData() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbconnection.getConnection();

            if (null == connection) {
                System.out.println("Product Update : Connection failed");
                return;
            }


            ps = connection.prepareStatement("SELECT tp.product_id,tp.discount_id ,tp.tax_id ,\n" +
                    "       td.discount_id , td.discount_name ,td.discount,td.description, tpt.tax_id ,tpt.hsn_sac ,\n" +
                    "       tpt.tax_id ,tpt.sgst,tpt.cgst,tpt.igst,tpt.description,tpt.\"gstName\"\n" +
                    "\n" +
                    "FROM   tbl_products as tp\n" +
                    "           Left JOIN tbl_discount as td  ON ( tp.discount_id = td.discount_id )\n" +
                    "           Left Join tbl_product_tax as tpt  on ( tp.tax_id = tpt.tax_id )  where product_id = ?");
            ps.setInt(1, products.getProductID());
            rs = ps.executeQuery();

            if (rs.next()) {

                // discount
                int discountID = rs.getInt("discount_id");
                int discount = rs.getInt("discount");
                String description = rs.getString("description");
                String discountName = rs.getString("discount_name");

                // tax
                int hsn_sac = rs.getInt("hsn_sac");
                int taxId = rs.getInt("tax_id");
                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

                if (discount > 0){
                    Discount dis = new Discount(discountID,discountName , discount, description);
                    productDiscount.getItems().add(dis);
                    productDiscount.getSelectionModel().selectFirst();

                }
                if (taxId > 0){

                    TAX tax = new TAX(taxId,hsn_sac, sgst, cgst, igst, gstName, tax_description);
                    productTax.getItems().add(tax);
                    productTax.getSelectionModel().selectFirst();
                }
            } else {

                System.out.println("not found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

            DBConnection.closeConnection(connection,ps,rs);
        }


        productName.setText(products.getProductName());
        productDescription.setText(products.getProductDescription());

        productCategory.getItems().add(products.getCategory());

        productColor.getItems().add(String.valueOf(products.getProductColor()));
        productType.getItems().add(products.getProductType());

        productCategory.getSelectionModel().selectFirst();
        productColor.getSelectionModel().selectFirst();
        productType.getSelectionModel().selectFirst();


        productCategory.setItems(method.getProductCategory());
        productColor.setItems(method.getProductColor());
        productType.setItems(method.getProductType());

        productDiscount.setOnMouseClicked(event -> {

            productDiscount.getSelectionModel().clearSelection();
            productDiscount.setPromptText("Not applicable");
            productDiscount.setItems(new GetDiscount().get());

        });

        productTax.setOnMouseClicked(event -> {
                    productTax.getSelectionModel().clearSelection();
                    productTax.setPromptText("Not applicable");
                    productTax.setItems(new GetTax().getGst());
                }

        );


    }

    public void enterPress(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {

            bnUpdate(null);
        }


    }

    public void bnUpdate(ActionEvent event) {

        String prodName = productName.getText();
        String prodDescription = productDescription.getText();

        Discount discount = productDiscount.getSelectionModel().getSelectedItem();
        TAX tax = productTax.getSelectionModel().getSelectedItem();

        if (prodName.isEmpty()) {
            method.show_popup("ENTER PRODUCT NAME ", productName);
            return;
        } else if (null == productCategory.getValue()) {
            method.show_popup("CHOOSE PRODUCT CATEGORY", productCategory);
            return;
        } else if (null == productColor.getValue()) {
            method.show_popup("CHOOSE PRODUCT COLOR ", productColor);
            return;
        } else if (null == productType.getValue()) {
            method.show_popup("CHOOSE PRODUCT TYPE ", productType);
            return;
        }

        String prodColor = productColor.getValue();
        String prodType = productType.getValue();
        String prodCategory = productCategory.getValue();

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbconnection.getConnection();
            if (null == connection) {
                System.out.println("ProductUpdate : Connection failed");
                return;
            }

            String query = "UPDATE TBL_PRODUCTS SET PRODUCT_NAME = ?, PRODUCT_DESCRIPTION = ?, PRODUCT_COLOR = ?," +
                    " PRODUCT_TYPE = ?,CATEGORY = ?, DISCOUNT_ID = ?, TAX_ID = ? WHERE PRODUCT_ID = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, prodName);
            ps.setString(2, prodDescription);
            ps.setString(3, prodColor);
            ps.setString(4, prodType);
            ps.setString(5, prodCategory);

            if (null == discount) {
                ps.setNull(6, Types.NULL);
            } else {
                ps.setInt(6, discount.getDiscount_id()); // dis
            }
            if (null == tax) {
                ps.setNull(7, Types.NULL);
            } else {
                ps.setInt(7, tax.getTaxID()); //tax
            }

            ps.setInt(8, products.getProductID());

            int res = ps.executeUpdate();

            if (res > 0) {
                Stage stage =  new  CustomDialog().stage;
                if (stage.isShowing()) {
                    stage.close();
                }
            }

        } catch (SQLException e) {
            customDialog.showAlertBox("Failed",e.getMessage());
        }


    }
}
