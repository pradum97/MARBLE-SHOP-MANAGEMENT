package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Method.Method;
import com.shop.management.Model.UserDetails;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
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
    private DBConnection dbConnection;
    private Properties properties;
    private CustomDialog customDialog;
    private Method method;
    private int userId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        properties =new PropertiesLoader().load("query.properties");
        customDialog = new CustomDialog();


        userId = ((int) Main.primaryStage.getUserData());

        setUserData(userId);

    }

    private void setUserData(int userId) {

        if (userId != Login.currentlyLogin_Id) {
            bnChangePassword.setVisible(false);
            bnChangePassword.managedProperty().bind(bnChangePassword.visibleProperty());
        }

        UserDetails userDetails = new GetUserProfile().getUser(userId);

        if (null == userDetails) {
            customDialog.showAlertBox("Failed", "User Not Find Please Re-Login");
        } else {

            fullName.setText(userDetails.getFirstName() + " " + userDetails.getLastName());
            userRole.setText(userDetails.getRole());
            userName.setText(userDetails.getUsername());
            userGender.setText(userDetails.getGender());
            userEmail.setText(userDetails.getEmail());
            userPhone.setText(String.valueOf(userDetails.getPhone()));
            userAddress.setText(userDetails.getFullAddress());

            String path = "img/Avatar/" + userDetails.getUserImage();

            userImage.setImage(new ImageLoader().load(path));
        }
    }

    public void editProfile(ActionEvent event) {
       Main.primaryStage.setUserData(userId);

        customDialog.showFxmlDialog2("update/updateProfile.fxml", "EDIT PROFILE");

        setUserData(userId);
    }

    public void changePassword(ActionEvent event) {

        customDialog.showFxmlDialog("dashboard/forgotPassword.fxml", "CHANGE PASSWORD");
    }
}
