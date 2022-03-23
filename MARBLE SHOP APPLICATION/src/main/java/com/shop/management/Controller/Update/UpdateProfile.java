package com.shop.management.Controller.Update;

import com.shop.management.Controller.Login;
import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.GetUserProfile;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.UserDetails;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfile implements Initializable {
    @FXML
    public TextField first_name_f;
    public TextField last_name_f;
    public TextField username_f;
    public TextField phone_f;
    public TextField email_f;
    public ComboBox<String> gender_comboBox;
    public ComboBox<String> role_combobox;
    public TextArea full_address_f;
    public Button bnCancel;
    public Button bnUpdate;
    public ComboBox<String> combo_accountStatus;
    private Method method;
    private Properties properties;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private int userId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        properties = new PropertiesLoader().load("query.properties");

        userId = ((int) Main.primaryStage.getUserData());
        setUserDetails(userId);
    }

    private void setUserDetails(int userId) {

        GetUserProfile getUserProfile = new GetUserProfile();
        UserDetails userDetails = getUserProfile.getUser(userId);

        if (null == userDetails) {
            customDialog.showAlertBox("Failed", "User Not Find Please Re-Login");
            return;
        }
        first_name_f.setText(userDetails.getFirstName());
        last_name_f.setText(userDetails.getLastName());
        username_f.setText(userDetails.getUsername());
        phone_f.setText(String.valueOf(userDetails.getPhone()));
        email_f.setText(userDetails.getEmail());
        full_address_f.setText(userDetails.getFullAddress());
        gender_comboBox.getItems().add(userDetails.getGender());
        gender_comboBox.getSelectionModel().selectFirst();
        role_combobox.getItems().add(userDetails.getRole());
        role_combobox.getSelectionModel().selectFirst();
        role_combobox.setItems(method.getRole());
        gender_comboBox.setItems(method.getGender());
        combo_accountStatus.setItems(method.getAccountStatus());
        combo_accountStatus.setDisable(userDetails.getUserID() == Login.currentlyLogin_Id);

        switch (userDetails.getAccountStatus()) {

            case "Active" -> {
                combo_accountStatus.getSelectionModel().select(1);
            }
            case "Inactive" -> {
                combo_accountStatus.getSelectionModel().select(0);
            }
        }

    }

    public void update_bn(ActionEvent event) {

        Connection connection = null;
        PreparedStatement ps_insert_data = null;

        String first_name = first_name_f.getText();
        String last_name = last_name_f.getText();
        String username = username_f.getText();
        String phone = phone_f.getText();
        String email = email_f.getText();
        String full_address = full_address_f.getText();

        Pattern pattern = Pattern.compile(new StaticData().emailRegex);

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
        long phoneNum ;
        try {
            phoneNum = Long.parseLong(phone);
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("Registration Failed ", "Enter 10-digit Phone Number Without Country Code");
            return;
        }
        Pattern phone_pattern = Pattern.compile("^\\d{10}$");
        Matcher phone_matcher = phone_pattern.matcher(phone);

        if (!phone_matcher.matches()) {
            customDialog.showAlertBox("Registration Failed ", "Enter 10-digit Phone Number Without Country Code");
            return;
        } else if (email.isEmpty()) {
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
        }

        try {

            connection = dbConnection.getConnection();
            ps_insert_data = connection.prepareStatement(properties.getProperty("UPDATE_USER"));
            ps_insert_data.setString(1, first_name);
            ps_insert_data.setString(2, last_name);
            ps_insert_data.setString(3, gender_comboBox.getValue());
            ps_insert_data.setString(4, role_combobox.getValue());
            ps_insert_data.setString(5, email);
            ps_insert_data.setString(6, username);
            ps_insert_data.setLong(7, phoneNum);
            ps_insert_data.setString(8, full_address);
            ps_insert_data.setInt(9, combo_accountStatus.getSelectionModel().getSelectedIndex());
            ps_insert_data.setInt(10, userId);

            int result = ps_insert_data.executeUpdate();

            if (result > 0) {

                customDialog.showAlertBox("Congratulations 🎉🎉🎉", "Successfully Updated");

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                if (stage.isShowing()) {

                    stage.close();
                }

            } else {
                customDialog.showAlertBox("", "Updating Failed");
            }
        } catch (SQLException e) {
            customDialog.showAlertBox("Updating Failed", e.getMessage());

            e.printStackTrace();
        } finally {

            if (null != connection) {
                try {
                    connection.close();
                    if (ps_insert_data != null) {
                        ps_insert_data.close();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void bnCancel(ActionEvent event) {

        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();

        if (stage.isShowing()){
            stage.close();
        }
    }
}