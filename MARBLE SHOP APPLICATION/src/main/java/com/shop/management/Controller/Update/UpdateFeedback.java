package com.shop.management.Controller.Update;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.Feedback;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateFeedback implements Initializable {
    @FXML
    public TextField fullName;
    public TextField email;
    public TextField phone;
    public TextArea comments;
    public Rating rate;
    public Button bn_feedback_submit;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private Method method;
    private Feedback feed;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        feed = (Feedback) Main.primaryStage.getUserData();
        setData(feed);
    }

    private void setData(Feedback feed) {

        fullName.setText(feed.getFullName());
        email.setText(feed.getEmail());
        phone.setText(String.valueOf(feed.getFeed_phone()));
        rate.setRating(Double.parseDouble(feed.getStar()));
        comments.setText(feed.getMessage());
    }

    public void updateFeedback(ActionEvent event) {

        String fullname = fullName.getText();
        String emailID = email.getText();
        String phoneNum = phone.getText();
        String comment = comments.getText();
        double rating = rate.getRating();

        Pattern pattern = Pattern.compile(new StaticData().emailRegex);
        Matcher matcher = pattern.matcher(emailID);

        if (fullname.isEmpty()) {
            method.show_popup("Enter Full Name", fullName);
            return;
        } else if (!emailID.isEmpty()) {

            if (!matcher.matches()) {
                method.show_popup("Enter Valid Email", email);
                return;
            }
        } else if (!phoneNum.isEmpty()) {

            Pattern phone_pattern = Pattern.compile("^\\d{10}$");
            Matcher phone_matcher = phone_pattern.matcher(phoneNum);

            if (!phone_matcher.matches()) {

                customDialog.showAlertBox("Failed ", "Enter 10-digit Phone Number Without Country Code");
                return;
            }

        } else if (comment.isEmpty()) {

            method.show_popup("Enter Comments", comments);
            return;
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {

            con = dbConnection.getConnection();

            if (null == con) {
                return;
            }

            ps = con.prepareStatement(new PropertiesLoader().getUpdateProp().getProperty("FEEDBACK_UPDATE"));
            ps.setString(1, fullname);
            ps.setString(2, emailID);
            ps.setString(3, phoneNum);
            ps.setString(4, String.valueOf(rating));
            ps.setString(5, comment);
            ps.setInt(6, feed.getFeed_id());

            int res = ps.executeUpdate();

            if (res > 0) {

                email.setText("");
                phone.setText("");
                comments.setText("");
                fullName.setText("");


                Stage stage = CustomDialog.stage;

                if (stage.isShowing()) {
                    stage.close();
                }

            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
        } finally {

            try {

                if (null != con) {
                    con.close();
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
}