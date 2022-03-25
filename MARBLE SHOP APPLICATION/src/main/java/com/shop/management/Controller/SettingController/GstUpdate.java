package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.TAX;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class GstUpdate implements Initializable {

    public TextField sgstTF;
    public TextField cgstTF;
    public TextField igstTF;
    public TextField gstNameTF;
    public TextField descriptionTF;
    public TextField hsn_sacTf;

    private Method method;
    private DBConnection dbConnection;
    private Properties properties;
    private CustomDialog customDialog;
    private TAX tax;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tax = (TAX) Main.primaryStage.getUserData();
        if (null == tax) {
            return;
        }

        method = new Method();
        dbConnection = new DBConnection();
        properties = new PropertiesLoader().load("query.properties");
        customDialog = new CustomDialog();

        setPreviousData(tax);

    }

    private void setPreviousData(TAX tax) {

        hsn_sacTf.setText(String.valueOf(tax.getHsn_sac()));
        sgstTF.setText(String.valueOf(tax.getSgst()));
        cgstTF.setText(String.valueOf(tax.getCgst()));
        igstTF.setText(String.valueOf(tax.getIgst()));
        gstNameTF.setText(tax.getGstName());
        descriptionTF.setText(tax.getTaxDescription());

    }

    private boolean isExist(long enterHsnCode){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            connection = dbConnection.getConnection();
            String query = "select HSN_SAC from TBL_PRODUCT_TAX where HSN_SAC = ?";

            System.out.println(query);

            ps = connection.prepareStatement(query);
            ps.setLong(1,enterHsnCode);

            rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            DBConnection.closeConnection(connection , ps , rs);
        }
    }

    public void updateTax(ActionEvent event) {

        String sgst = sgstTF.getText();
        String cgst = cgstTF.getText();
        String igst = igstTF.getText();
        String gstName = gstNameTF.getText();
        String description = descriptionTF.getText();
        String hsn_sacS = hsn_sacTf.getText();

        if (hsn_sacS.isEmpty()) {
            method.show_popup("Enter HSN / SAC", hsn_sacTf);
            return;
        }else if (sgst.isEmpty()) {
            method.show_popup("Enter sgst", sgstTF);
            return;
        } else if (cgst.isEmpty()) {
            method.show_popup("Enter cgst", cgstTF);
            return;
        } else if (igst.isEmpty()) {
            method.show_popup("Enter igst", igstTF);
            return;
        }else if (gstName.isEmpty()) {
            method.show_popup("Enter Gst Name", gstNameTF);
            return;
        }

        int sGst = 0, cGst = 0, iGst = 0, hsn_sac = 0;

        try {
            hsn_sac = Integer.parseInt(hsn_sacS.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            hsn_sacTf.setText("");

            return;
        }
         if (isExist(hsn_sac)){
            method.show_popup("THIS HSN CODE IS ALREADY EXIST!", hsn_sacTf);
            return;
        }


        try {
            sGst = Integer.parseInt(sgst);
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("Validation Failed", "Please Enter Valid SGST");

            return;
        }

        try {
            iGst = Integer.parseInt(igst);
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("Validation Failed", "Please Enter Valid IGST");

            return;
        }

        try {
            cGst = Integer.parseInt(cgst);
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("Validation Failed", "Please Enter Valid CGST");

            return;
        }

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection) {
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("UPDATE_GST"));
            ps.setInt(1, sGst);
            ps.setInt(2, cGst);
            ps.setInt(3, iGst);

            if (null == gstName) {
                ps.setNull(4, Types.NULL);
            }else {
                ps.setString(4, gstName);
            }

                if (null == description) {
                ps.setNull(5, Types.NULL);
            } else {
                ps.setString(5, description);
            }
            ps.setInt(6, hsn_sac);
            ps.setInt(7, tax.getTaxID());

            int res = ps.executeUpdate();

            if (res > 0) {

                sgstTF.setText("");
                hsn_sacTf.setText("");
                cgstTF.setText("");
                igstTF.setText("");
                gstNameTF.setText("");
                descriptionTF.setText("");

                Stage stage = CustomDialog.stage;

                if (stage.isShowing()) {
                    stage.close();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (null != connection) {
                    connection.close();
                }
                if (null != ps) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
