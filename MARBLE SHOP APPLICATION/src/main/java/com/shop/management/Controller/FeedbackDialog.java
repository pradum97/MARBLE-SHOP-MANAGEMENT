package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Model.Feedback;
import com.shop.management.util.DBConnection;
import com.shop.management.Method.Method;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackDialog implements Initializable {
    @FXML
    public TextField fullName;
    public TextField email;
    public TextField phone;
    public TextArea comments;
    public Rating rate;
    public Button bn_feedback_submit;
    private Properties colorProperties, queryProp;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private Method method;

    private String button_bg_color, button_text_color;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        colorProperties = method.getProperties("color.properties");
        queryProp = method.getProperties("query.properties");

        button_bg_color = colorProperties.getProperty("BUTTON_BG_COLOR");
        button_text_color = colorProperties.getProperty("BUTTON_TEXT_COLOR");
        rate.setRating(4);


    }

    public void enterPress(KeyEvent e) {

        if (e.getCode() == KeyCode.ENTER) {
            //do something

            submitFeedback(null);
        }
    }

    public void submitFeedback(ActionEvent event) {

        String fullname = fullName.getText();
        String emailID = email.getText();
        String phoneNum = phone.getText();
        String comment = comments.getText();
        double rating = rate.getRating();

        rate.setRating(4);

        Pattern pattern = Pattern.compile(method.emailRegex);
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

                customDialog.showAlertBox("Failed ",
                        "Enter 10-digit Phone Number Without Country Code");
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
                System.out.println("connection Faield");
                return;
            }
            ps = con.prepareStatement(queryProp.getProperty("FEEDBACK"));
            ps.setString(1, fullname);
            ps.setString(2, emailID);
            ps.setString(3, phoneNum);
            ps.setString(4, String.valueOf(rating));
            ps.setString(5, comment);

            int res = ps.executeUpdate();

            if (res > 0) {

                customDialog.showAlertBox("Successfully Submit Your Feedback",
                        " Thank you for your feedback");
                email.setText("");
                phone.setText("");
                comments.setText("");
                fullName.setText("");

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
}
