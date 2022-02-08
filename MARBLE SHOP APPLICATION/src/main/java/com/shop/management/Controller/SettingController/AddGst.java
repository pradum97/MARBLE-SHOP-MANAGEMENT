package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;
import java.util.ResourceBundle;

public class AddGst implements Initializable {
 @FXML
    public TextField sgstTF;
    public TextField cgstTF;
    public TextField igstTF;
    public TextField gstNameTF;
    public TextField descriptionTF;

    private Method method;
    private DBConnection dbConnection;
    private Properties properties;
    private CustomDialog customDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        customDialog = new CustomDialog();

    }

    public void addTax(ActionEvent event) {

        String sgst = sgstTF.getText();
        String cgst = cgstTF.getText();
        String igst = igstTF.getText();
        String gstName = gstNameTF.getText();
        String description = descriptionTF.getText();

        if (sgst.isEmpty()) {
            method.show_popup("Enter sgst", sgstTF);
            return;
        } else if (cgst.isEmpty()) {
            method.show_popup("Enter cgst", cgstTF);
            return;
        } else if (igst.isEmpty()) {
            method.show_popup("Enter igst", igstTF);
            return;
        } else if (gstName.isEmpty()) {
            method.show_popup("Enter GST Name", gstNameTF);
            return;
        }
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection) {
                return;
            }
            int sGst = 0, cGst = 0, iGst = 0;
            try {
                sGst = Integer.parseInt(sgst.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                customDialog.showAlertBox("Validation Failed", "Please Enter Valid SGST");
                e.printStackTrace();
                return;
            }

            try {
                iGst = Integer.parseInt(igst.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                customDialog.showAlertBox("Validation Failed", "Please Enter Valid IGST");
                e.printStackTrace();
                return;
            }

            try {
                cGst = Integer.parseInt(cgst.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                customDialog.showAlertBox("Validation Failed", "Please Enter Valid CGST");
                e.printStackTrace();
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("SET_GST"));
            ps.setInt(1, sGst);
            ps.setInt(2, cGst);
            ps.setInt(3, iGst);
            ps.setString(4, gstName);

            if (description.isEmpty()) {
                ps.setNull(5, Types.NULL);
            } else {
                ps.setString(5, description);
            }

            int res = ps.executeUpdate();

            if (res > 0) {

                sgstTF.setText("");
                cgstTF.setText("");
                igstTF.setText("");
                gstNameTF.setText("");
                descriptionTF.setText("");

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
