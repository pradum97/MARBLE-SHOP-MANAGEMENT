package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.TAX;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    private CustomDialog customDialog;
    private TAX tax;
    private Properties propUpdate ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tax = (TAX) Main.primaryStage.getUserData();
        if (null == tax) {
            return;
        }

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        PropertiesLoader propLoader = new PropertiesLoader();
        propUpdate = propLoader.getUpdateProp();

        setPreviousData(tax);

    }

    private void setPreviousData(TAX tax) {

        hsn_sacTf.setText(String.valueOf(tax.getHsn_sac()));
        sgstTF.setText(String.valueOf(tax.getSgst()));
        cgstTF.setText(String.valueOf(tax.getCgst()));
        igstTF.setText(String.valueOf(tax.getIgst()));
        gstNameTF.setText(tax.getGstName());
        descriptionTF.setText(tax.getTaxDescription());

       /* hsn_sacTf.setEditable(false);
        hsn_sacTf.setFocusTraversable(false);

        hsn_sacTf.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                customDialog.showAlertBox("Not Allow", "You Can't Update HSN Code");
            }
        });*/

    }

    public void updateTax(ActionEvent event) {

        String sgst = sgstTF.getText();
        String cgst = cgstTF.getText();
        String igst = igstTF.getText();
        String gstName = gstNameTF.getText();
        String description = descriptionTF.getText();
        String hsn_sacS = hsn_sacTf.getText();

        int  hsn_sac = 0;

        try {
            hsn_sac = Integer.parseInt(hsn_sacS.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            hsn_sacTf.setText("");

            return;
        }

        if (hsn_sacS.isEmpty()) {
            method.show_popup("Enter HSN / SAC", hsn_sacTf);
            return;
        }else if (isHsnCodeExist(hsn_sac)){
            method.show_popup("HSN Code Already Exists", hsn_sacTf);
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

        int sGst = 0, cGst = 0, iGst = 0;


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

            ps = connection.prepareStatement(propUpdate.getProperty("UPDATE_GST"));
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

    public void cancel(ActionEvent event) {

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        if (stage.isShowing()){
            stage.close();
        }
    }

    public void enterPress(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER){
            updateTax(null);
        }
    }

    private boolean isHsnCodeExist(int value){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {

            connection = dbConnection.getConnection();
            String query = "select hsn_sac from tbl_product_tax where hsn_sac = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1,value);

            rs = ps.executeQuery();

            if(rs.next()){
                if(tax.getHsn_sac() != rs.getLong("hsn_sac")){  return true; }else { return false; }
            }else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            DBConnection.closeConnection(connection , ps , rs);
        }
    }
}
