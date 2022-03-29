package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Discount;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
    public TextArea descriptionTF;
    public Button submitBn;
    public TextField discountNameC;
    private CustomDialog customDialog;
    private Method method;
    private DBConnection dbConnection;
    private Properties properties;
    private Discount discount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customDialog = new CustomDialog();
        method = new Method();
        dbConnection = new DBConnection();
        properties = new PropertiesLoader().load("query.properties");

        discount = (Discount) Main.primaryStage.getUserData();

        setPreviousData(discount);

    }

    private void setPreviousData(Discount discount) {

        discountTF.setText(String.valueOf(discount.getDiscount()));
        descriptionTF.setText(discount.getDescription());
        discountNameC.setText(discount.getDiscountName());

    }

    public void updateBn(ActionEvent event) {

        String discountTf = discountTF.getText();
        String descriptionTf = descriptionTF.getText();
        String discountName = discountNameC.getText();

        if (discountName.isEmpty()) {
            method.show_popup("Enter Discount Name ", discountNameC);
            return;
        } else if (discountTf.isEmpty()) {
            method.show_popup("Enter Discount ", discountTF);
            return;
        }
        int discountD = 0;
        try {
            discountD = Integer.parseInt(discountTf.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (discountD > 100) {
            method.show_popup("Enter Discount Less Than 100 ", discountTF);
            return;
        }


        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("UPDATE_DISCOUNT"));
            ps.setInt(1, discountD);
            ps.setString(2, descriptionTf);
            ps.setString(3, discountName);
            ps.setInt(4, discount.getDiscount_id());

            int res = ps.executeUpdate();

            if (res > 0) {

                Stage stage = CustomDialog.stage;

                if (stage.isShowing()) {
                    stage.close();
                }
            }


        } catch (SQLException e) {
            customDialog.showAlertBox("Failed","Duplicate Entry Not Allow");
        } finally {

            DBConnection.closeConnection(connection, ps, null);
        }

    }

    public void cancel(ActionEvent event) {

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if (stage.isShowing()){
            stage.close();
        }
    }
}
