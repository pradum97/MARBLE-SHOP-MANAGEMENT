package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;

public class AddCustomer implements Initializable {
    public TextField nameTf;
    public TextField phoneTf;
    public TextField addressTf;
    public TextArea descriptionTf;

    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();

    }

    public void submitBn(ActionEvent event) {

        String sName = nameTf.getText();
        String sPhone = phoneTf.getText();
        String address = addressTf.getText();
        String description = descriptionTf.getText();

        if (sName.isEmpty()) {
            method.show_popup("Enter  Full Name", nameTf);
            return;
        } else if (sPhone.isEmpty()) {
            method.show_popup("Enter  Phone Number", phoneTf);
            return;
        }else if (address.isEmpty()) {
            method.show_popup("Enter Full Address", addressTf);
            return;
        }

        long phoneNum = 0;

        try {
            phoneNum = Long.parseLong(sPhone);
        } catch (NumberFormatException e) {
           customDialog.showAlertBox("Invalid","Phone Number Not Valid! Please Enter Valid Phone Number");
           return;
        }

        Connection connection = null;
        PreparedStatement ps = null;

        try {

            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            String query = "INSERT INTO tbl_customer (CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS , description) VALUES (?,?,?,?)";
            ps = connection.prepareStatement(query);
            ps.setString(1, sName);
            ps.setLong(2,phoneNum);
            ps.setString(3, address);

            if (description.isEmpty()){
                ps.setNull(4, Types.NULL);
            }else {
                ps.setString(4, description);
            }

            int res = ps.executeUpdate();

            if (res > 0) {

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                if (stage.isShowing()) {
                    stage.close();
                }

                customDialog.showAlertBox("success", "Customer Successfully Added");


            }

        } catch (SQLException e) {
            e.printStackTrace();
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
