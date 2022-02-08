package com.shop.management.Controller;

import com.shop.management.Main;
import com.shop.management.Method.CopyImage;
import com.shop.management.CustomDialog;
import com.shop.management.util.DBConnection;
import com.shop.management.Method.Method;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp implements Initializable {

    @FXML
    public TextField first_name_f;
    public TextField last_name_f;
    public TextField username_f;
    public TextField phone_f;
    public TextField email_f;
    public ComboBox<String> gender_comboBox;
    public ComboBox<String> role_combobox;
    public TextArea full_address_f;
    public PasswordField password_f;
    public TextField con_password_f;
    public ImageView profile_photo;
    public Button profile_img_choose;
    public Button already_account;
    public Button submit_bn;
    private Method method;
    private Properties properties;
    private DBConnection dbConnection;
    private String imageName;
    private CustomDialog customDialog;
    private Main main;
    String imgAvatarPath;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        main = new Main();
        properties = method.getProperties("query.properties");

        setData();

        String type  =(String) Main.primaryStage.getUserData();
        if (type == "adduser"){

            already_account.setVisible(false);
        }
    }

    private void setData() {

        gender_comboBox.setItems(method.getGender());
        role_combobox.setItems(method.getRole());


    }

    public void already_account_bn(ActionEvent event) {

        main.changeScene("login.fxml", "Login Here");
    }

    public void submit_bn(ActionEvent event) {

       startSignup();
    }

    private void clearValue() {

        first_name_f.setText("");
        last_name_f.setText("");
        username_f.setText("");
        phone_f.setText("");
        email_f.setText("");
        full_address_f.setText("");
        password_f.setText("");
        con_password_f.setText("");

        Stage stage = CustomDialog.stage;

        if (stage.isShowing()){
            stage.close();
        }

        imgAvatarPath = "";
        profile_photo.setImage(method.getImage("src/main/resources/com/shop/management/img/icon/person_ic.png"));
        profile_img_choose.setText("Upload Photo");

    }

    public void chooseAvatar(ActionEvent event) {

        customDialog.showFxmlDialog2("avatar.fxml","CHOOSE YOUR PROFILE AVATAR");
        setAvatar();

    }

    public void setAvatar(){
        String path = "src/main/resources/com/shop/management/img/Avatar/";
        try {
            imgAvatarPath = (String) Main.primaryStage.getUserData();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        String img = path+imgAvatarPath;

        if (null != imgAvatarPath){
           profile_photo.setImage(new Method().getImage(img));
        }
    }

    public void enterPress(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            //do something

            startSignup();
        }
    }

    private void startSignup() {

        Connection connection = null;
        PreparedStatement ps_insert_data = null;

        String mac_address = method.get_mac_address();
        String first_name = first_name_f.getText();
        String last_name = last_name_f.getText();
        String username = username_f.getText();
        String phone = phone_f.getText();
        String email = email_f.getText();
        String full_address = full_address_f.getText();
        String password = password_f.getText();
        String confirm_password = con_password_f.getText();

        Pattern pattern = Pattern.compile(method.emailRegex);

        Matcher matcher = pattern.matcher(email);

        if (first_name.isEmpty()) {
            method.show_popup("Enter First Name", last_name_f);
            return;

        } else if (username.isEmpty()) {
            method.show_popup("Enter Username", username_f);
            return;

        } else if (phone.isEmpty()) {
            method.show_popup("Enter 10-digit Phone Number", phone_f);
            return;

        }
        long phoneNum = 0;
        try {
            phoneNum = Long.parseLong(phone);
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("Registration Failed ","Enter 10-digit Phone Number Without Country Code");
            return;
        }
        Pattern phone_pattern = Pattern.compile("^\\d{10}$");
        Matcher phone_matcher = phone_pattern.matcher(phone);

        if (!phone_matcher.matches()){
            customDialog.showAlertBox("Registration Failed ","Enter 10-digit Phone Number Without Country Code");
            return;
        }
        else if (email.isEmpty()) {
            method.show_popup("Enter Valid Email", email_f);
            return;

        } else if (!matcher.matches()) {
            method.show_popup("Enter Valid Email", email_f);
            return;

        } else if (null == gender_comboBox.getValue()) {
            method.show_popup("Choose Your Gender", gender_comboBox);
            return;

        } else if (null == role_combobox.getValue()) {
            method.show_popup("Choose role_combobox", role_combobox);
            return;
        } else if (full_address.isEmpty()) {
            method.show_popup("Enter Full Address", full_address_f);
            return;
        } else if (password.isEmpty()) {
            method.show_popup("Enter Password", password_f);
            return;
        } else if (confirm_password.isEmpty()) {
            method.show_popup("Enter Confirm Password", con_password_f);
            return;
        } else if (null == imgAvatarPath) {
            method.show_popup("please upload Photo", profile_img_choose);
            return;
        } else if (!password.equals(confirm_password)) {
            method.show_popup("confirm password doesn't match", con_password_f);
            return;
        } else if (mac_address.isEmpty()) {
            mac_address = "Not-Found";
        }
        imageName = new CopyImage().copy(new File("src/main/resources/com/shop/management/img/Avatar/"+imgAvatarPath).getAbsolutePath(), "userImages/profileImg");

        try {

            connection = dbConnection.getConnection();
            ps_insert_data = connection.prepareStatement(properties.getProperty("SIGNUP"));

            ps_insert_data.setString(1, first_name);
            ps_insert_data.setString(2, last_name);
            ps_insert_data.setString(3, gender_comboBox.getValue());
            ps_insert_data.setString(4, role_combobox.getValue());
            ps_insert_data.setString(5, email);
            ps_insert_data.setString(6, username);
            ps_insert_data.setString(7, password);
            ps_insert_data.setLong(8, phoneNum);
            ps_insert_data.setString(9, full_address);
            ps_insert_data.setString(10, imageName);
            ps_insert_data.setString(11, mac_address);

            int result = ps_insert_data.executeUpdate();

            if (result > 0) {


                int userID = Login.currentlyLogin_Id;

                if (userID == 0){
                    main.changeScene("login.fxml", "Login Here");
                    customDialog.showAlertBox("Congratulations 🎉🎉🎉", "Registration Successful");
                }else {

                    clearValue();
                }



            } else {
                customDialog.showAlertBox("", "Registration Failed");
            }
        } catch (SQLException e) {
            customDialog.showAlertBox("Registration Failed", e.getMessage());

            if (null != imageName) {
                File file = new File("src/main/resources/com/shop/management/img/userImages/" + imageName);
                if (file.exists()) {
                    try {
                        FileUtils.forceDelete(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Image deleted : " + imageName);
                }
            }
            e.printStackTrace();
        } finally {

            if (null != connection) {
                try {
                    connection.close();
                    ps_insert_data.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
