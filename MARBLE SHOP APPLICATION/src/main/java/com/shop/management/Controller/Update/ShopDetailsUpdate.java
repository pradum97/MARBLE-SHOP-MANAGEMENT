package com.shop.management.Controller.Update;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.CloseConnection;
import com.shop.management.Method.Method;
import com.shop.management.Model.Shop;
import com.shop.management.util.DBConnection;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;

public class ShopDetailsUpdate implements Initializable {
    public TextField shopNameTf;
    public TextField phone_1Tf;
    public TextField phone_2TF;
    public TextField emailTF;
    public TextField addressTF;

    private DBConnection dbConnection;
    private Method method;
    private CustomDialog customDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();


        Shop shopDetails = (Shop) Main.primaryStage.getUserData();

        if (null == shopDetails) {
            customDialog.showAlertBox("", "Details Not Found Please Re-Start Application");
            return;
        }

        setOldValue(shopDetails);
    }

    private void setOldValue(Shop shopDetails) {

        shopNameTf.setText(shopDetails.getShopName());
        phone_1Tf.setText(shopDetails.getShopPhone_1());
        phone_2TF.setText(shopDetails.getShopPhone_2());
        emailTF.setText(shopDetails.getShopEmail());
        addressTF.setText(shopDetails.getShopAddress());
    }

    public void bnUpdate(MouseEvent event) {

        String sName = shopNameTf.getText();
        String sPhone_1 = phone_1Tf.getText();
        String sPhone_2 = phone_2TF.getText();
        String sEmail = emailTF.getText();
        String sAddress = addressTF.getText();

        if (sName.isEmpty()) {

            method.show_popup("ENTER SHOP NAME", shopNameTf);
            return;
        } else if (sPhone_1.isEmpty()) {

            method.show_popup("ENTER SHOP PHONE-1", phone_1Tf);
            return;
        } else if (sEmail.isEmpty()) {

            method.show_popup("ENTER SHOP EMAIL", emailTF);
            return;
        } else if (sAddress.isEmpty()) {

            method.show_popup("ENTER SHOP FULL ADDRESS", addressTF);
            return;
        }
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();

            String query = "UPDATE tbl_shop_details SET shop_name = ? , shop_phone_1 = ? , shop_phone_2 = ? , shop_email = ? ,\n" +
                    "                            shop_address = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1,sName);
            ps.setString(2,sPhone_1);

            if (sPhone_2.isEmpty()) {
                ps.setNull(3, Types.NULL);
            }else {
                ps.setString(3,sPhone_2);
            }

            ps.setString(4,sEmail);
            ps.setString(5,sAddress);

            int res = ps.executeUpdate();

            if (res > 0){

                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

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
