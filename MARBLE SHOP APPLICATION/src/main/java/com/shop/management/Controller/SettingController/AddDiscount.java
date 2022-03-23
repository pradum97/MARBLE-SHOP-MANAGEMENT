package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class AddDiscount implements Initializable {

    @FXML
    public TextField discountTF;
    public TextArea descriptionTF;
    public Button submitBn;
    public TextField discountNameC;
    private CustomDialog customDialog;
    private Method method;
    private DBConnection dbConnection;
    private Properties properties;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customDialog = new CustomDialog();
        method = new Method();
        dbConnection = new DBConnection();
        properties =new PropertiesLoader().load("query.properties");
    }




    public void submitBn(ActionEvent event) {

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

            ps = connection.prepareStatement(properties.getProperty("ADD_DISCOUNT"));
            ps.setInt(1, discountD);
            ps.setString(2, descriptionTf);
            ps.setString(3, discountName);

            int res = ps.executeUpdate();

            if (res > 0) {

                Stage stage = CustomDialog.stage;

                if (stage.isShowing()) {
                    stage.close();
                }
            }


        } catch (SQLException e) {
            e.getStackTrace();

        } finally {

            DBConnection.closeConnection(connection, ps, null);
        }


    }
}
