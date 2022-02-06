package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class AddDiscount implements Initializable {

    @FXML
    public TextField discountTF;
    public ComboBox<String> discountTypeCombo;
    public TextArea descriptionTF;
    CustomDialog customDialog;
    Method method;
    DBConnection dbConnection ;
    Properties properties;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customDialog = new CustomDialog();
        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");

        discountTypeCombo.setItems(method.getDiscountType());
        discountTypeCombo.getSelectionModel().selectFirst();



    }

    public void submitBn(ActionEvent event) {
        String discountTf = discountTF.getText();
        String descriptionTf = descriptionTF.getText();

        if (discountTf.isEmpty()){
            method.show_popup("Enter Discount ",discountTF);
            return;
        }else if (null == discountTypeCombo.getValue()){
            method.show_popup("CHOOSE DISCOUNT TYPE ",discountTypeCombo);
            return;
        }

        String discountType = discountTypeCombo.getValue();


        double discountD = 0;
        try {
            discountD = Double.parseDouble(discountTf.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection){
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("ADD_DISCOUNT"));
            ps.setDouble(1,discountD);
            ps.setString(2,discountType);
            ps.setString(3,descriptionTf);

            int res = ps.executeUpdate();

            if (res > 0 ){

              //  customDialog.showAlertBox("","Successful");
                Stage stage = CustomDialog.stage2;

                if (stage.isShowing()){
                    stage.close();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

            try {
                if (null != connection){
                    connection.close();
                }
                if (null != ps){
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
}
