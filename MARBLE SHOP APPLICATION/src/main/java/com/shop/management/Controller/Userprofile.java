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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;
import java.util.ResourceBundle;

public class Userprofile implements Initializable {

    @FXML
    public ImageView userImage;
    public Label fullName;
    public Label userName;
    public Label userGender;
    public Label userRole;
    public Label userEmail;
    public Label userPhone;
    public Label userAddress;
    public Button bnChangePassword;
    public Button bnEdit;
    private Connection connection;
    private DBConnection dbConnection;
    private Properties properties;
    CustomDialog customDialog;
    Method method;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        customDialog = new CustomDialog();


        int userId = ((UserDetails) Main.primaryStage.getUserData()).getUserID();

        setUserData(userId);

    }
    private void setUserData(int userId) {

        UserDetails userDetails =new GetUserProfile().getUser(userId);

        if (null == userDetails){
            customDialog.showAlertBox("Failed","User Not Find Please Re-Login");
        }else {

            fullName.setText(userDetails.getFirstName()+" "+ userDetails.getLastName());
            userRole.setText(userDetails.getRole());
            userName.setText(userDetails.getUsername());
            userGender.setText(userDetails.getGender());
            userEmail.setText(userDetails.getEmail());
            userPhone.setText(String.valueOf(userDetails.getPhone()));
            userAddress.setText(userDetails.getFullAddress());

            String path = "src/main/resources/com/shop/management/img/userImages/"+ userDetails.getUserImage();

            userImage.setImage(method.getImage(path));
        }
    }

    public void editProfile(ActionEvent event) {

        customDialog.showFxmlDialog("update/updateProfile.fxml","EDIT PROFILE");
    }

    public void changePassword(ActionEvent event) {

        customDialog.showFxmlDialog("dashboard/forgotPassword.fxml","CHANGE PASSWORD");
    }
}
