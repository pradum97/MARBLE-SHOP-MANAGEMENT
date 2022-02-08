package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.CloseConnection;
import com.shop.management.Method.Method;
import com.shop.management.Model.Discount;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

public class DiscountUpdate implements Initializable {

    @FXML
    public TextField discountTF;
    public ComboBox<String> discountTypeCombo;
    public TextArea descriptionTF;
    public Button submitBn;
    CustomDialog customDialog;
    Method method;
    DBConnection dbConnection ;
    Properties properties;
    Discount discount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customDialog = new CustomDialog();
        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");

         discount = (Discount) Main.primaryStage.getUserData();

        setPreviousData(discount);

    }

    private void setPreviousData(Discount discount) {

        discountTF.setText(String.valueOf(discount.getDiscount()));
        descriptionTF.setText(discount.getDescription());
        discountTypeCombo.getItems().add(discount.getDiscountType());
        discountTypeCombo.getSelectionModel().selectFirst();

        discountTypeCombo.setItems(method.getDiscountType());
    }

    public void updateBn(ActionEvent event) {


        String discountTf = discountTF.getText();
        String descriptionTf = descriptionTF.getText();

        if (discountTf.isEmpty()){
            method.show_popup("Enter Discount ",discountTF);
            return;
        }


        int discountD = 0;
        try {
            discountD = Integer.parseInt(discountTf.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (discountD > 100){
            method.show_popup("Enter Discount Less Than 100 ",discountTF);
            return;
        }
        if (null == discountTypeCombo.getValue()){
            method.show_popup("CHOOSE DISCOUNT TYPE ",discountTypeCombo);
            return;
        }

        String discountType = discountTypeCombo.getValue();


        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection){
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("UPDATE_DISCOUNT"));
            ps.setInt(1,discountD);
            ps.setString(2,discountType);
            ps.setString(3,descriptionTf);
            ps.setInt(4,discount.getDiscount_id());

            int res = ps.executeUpdate();

            if (res > 0 ){

                Stage stage = CustomDialog.stage;

                if (stage.isShowing()){
                    stage.close();
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

            CloseConnection.closeConnection(connection,ps,null);
        }

    }
}
