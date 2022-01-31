package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Method.Method;
import com.shop.management.Model.UserDetails;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.Properties;
import java.util.ResourceBundle;

public class Login implements Initializable {
    public TextField email_f;
    public PasswordField password_f;
    private Main main;
    private Method method;
    private Properties properties;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    public static int currentlyLogin_Id = 0; ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        main = new Main();
        method = new Method();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");
        dbConnection = new DBConnection();


    }

    @FXML
    public void forget_password_bn(ActionEvent event) {

        customDialog.showFxmlDialog("dashboard/forgotPassword.fxml", "Forgot Password");

    }

    public void login_bn(ActionEvent event) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String inputValue = email_f.getText();
        String password = password_f.getText();

        String regexPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        if (inputValue.isEmpty()) {
            method.show_popup("Please enter valid username", email_f);
            return;
        } else if (password.isEmpty()) {
            method.show_popup("Please enter password", password_f);
            return;
        }

        try {
            if (null == properties) {
                System.out.println("Properties File Not Found");
                return;
            }


            connection = dbConnection.getConnection();
            // Email Login
            ps = connection.prepareStatement(properties.getProperty("LOGIN_WITH_EMAIL"));
            ps.setString(1, inputValue);
            ps.setString(2, password);

            rs = ps.executeQuery();

            if (rs.next()) {

                getProfileDetails(rs);

            } else {

                // USERNAME LOGIN
                ps = connection.prepareStatement(properties.getProperty("LOGIN_WITH_USERNAME"));
                ps.setString(1, inputValue);
                ps.setString(2, password);
                rs = ps.executeQuery();

                if (rs.next()) {

                    getProfileDetails(rs);

                } else {

                    long phoneNum = 0;
                    try {
                        phoneNum = Long.parseLong(inputValue);
                    } catch (NumberFormatException e) {
                        // e.printStackTrace();
                    }
                    ;
                    ps = connection.prepareStatement(properties.getProperty("LOGIN_WITH_PHONE"));
                    ps.setLong(1, phoneNum);
                    ps.setString(2, password);
                    rs = ps.executeQuery();

                    if (rs.next()) {
                        getProfileDetails(rs);

                    } else {
                        customDialog.showAlertBox("Authentication Failed", "Invalid email or password !");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        } finally {

            try {
                if (null != connection) {

                    connection.close();
                    if (null != ps) {
                        ps.close();
                    }
                    if (null != rs) {
                        rs.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getProfileDetails(ResultSet rs) throws SQLException {

        int userID = rs.getInt("user_id");
        int userStatus = rs.getInt("account_status");

        if (userStatus == 0){
            customDialog.showAlertBox("Login Failed","Your Account Has Been Inactive Please Contact Administrator");
            return;
        }

        currentlyLogin_Id = userID;
        UserDetails userDetails = new GetUserProfile().getUser(userID);

        if (null == userDetails){

            customDialog.showAlertBox("Failed","User Not Found");

        }else {

            Main.primaryStage.setUserData(userDetails);
            main.changeScene("dashboard.fxml", "Dashboard");
            Main.primaryStage.setMaximized(true);

        }

    }


    public void create_new_account(ActionEvent event) {
        main.changeScene("signup.fxml", "Signup Here");

    }
}
