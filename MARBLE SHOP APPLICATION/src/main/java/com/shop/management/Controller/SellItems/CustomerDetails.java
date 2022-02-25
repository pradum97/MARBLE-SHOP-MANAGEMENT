package com.shop.management.Controller.SellItems;

import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Customer;
import com.shop.management.util.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerDetails implements Initializable {
    public TextField c_name;
    public TextField c_phone;
    public TextField c_address;
    public Button bnCheckOut;
    public VBox detailsContainer;
    public Label messageL;
    Method method;
    int customerId;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();

        detailsContainer.setDisable(true);

        c_phone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {


                String newValue = c_phone.getText();

                if (newValue.length() != 10) {

                    method.show_popup("ENTER 10-DIGIT PHONE NUMBER", c_phone);


                    detailsContainer.setDisable(true);

                    bnCheckOut.setVisible(true);
                    messageL.setVisible(false);
                    c_name.setText("");
                    c_address.setText("");

                }


            }
        });
    }



    public void cancel_Bn(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (stage.isShowing()) {
            stage.close();
        }
    }

    public void submit_bn(ActionEvent event) {

        if (customerId > 0) {

            sellNow(customerId, event);

        } else {

            String fullName = c_name.getText();
            String cPhone = c_phone.getText();
            String address = c_address.getText();

            long phoneNum;

            if (cPhone.isEmpty()) {
                method.show_popup("ENTER CUSTOMER PHONE NUMBER", c_phone);
                return;
            } else if (fullName.isEmpty()) {
                method.show_popup("ENTER CUSTOMER FULL-NAME", c_name);
                return;
            }

            try {

                phoneNum = Long.parseLong(cPhone.replaceAll("[^0-9.]", ""));

            } catch (NumberFormatException e) {
                c_phone.setText("");
                return;
            }

            if (address.isEmpty()) {
                method.show_popup("ENTER CUSTOMER ADDRESS", c_address);
                return;
            }


            addNewCustomer(fullName, phoneNum, address, event);


        }


    }

    private void sellNow(int customerId, ActionEvent event) {

        Customer customer = new Customer(customerId);

        Main.primaryStage.setUserData(customer);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (stage.isShowing()) {
            stage.close();
        }

    }

    private void addNewCustomer(String fullName, long phoneNum, String address, ActionEvent event) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("CustomerDetails : Connection Failed");
                return;
            }

            ps = connection.prepareStatement("INSERT INTO tbl_customer (CUSTOMER_NAME, CUSTOMER_PHONE, CUSTOMER_ADDRESS)\n" +
                    " VALUES (?,?,?)", new String[]{"customer_id"});

            ps.setString(1, fullName);
            ps.setLong(2, phoneNum);
            ps.setString(3, address);

            int res = ps.executeUpdate();


            if (res > 0) {

                rs = ps.getGeneratedKeys();

                if (rs.next()) {

                    int cId = rs.getInt(1);

                    sellNow(cId, event);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    public void bnCheckOut(ActionEvent event) {
        String newValue = c_phone.getText();

        if (newValue.length() == 10) {

            long phoneNum = 0;

            try {
                phoneNum = Long.parseLong(newValue.replaceAll("[^0-9.]", ""));
                checkCustomerExits(phoneNum);

            } catch (NumberFormatException e) {
                c_phone.setText("");
            }

            detailsContainer.setDisable(false);
        } else {
            method.show_popup("ENTER 10-DIGIT PHONE NUMBER", c_phone);
            detailsContainer.setDisable(true);

        }
    }

    private void checkCustomerExits(long phoneNumber) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println("CART : connection failed");
                return;
            }

            ps = connection.prepareStatement("select customer_phone ,customer_name ,customer_address , customer_id  from tbl_customer where customer_phone = ?");
            ps.setLong(1, phoneNumber);

            rs = ps.executeQuery();
            messageL.setVisible(true);

            if (rs.next()) {
                customerId = 0;
                messageL.setText("Customer Already Exists");
                messageL.setStyle("-fx-text-fill: green");
                bnCheckOut.setVisible(false);
                bnCheckOut.managedProperty().bind(bnCheckOut.visibleProperty());

                String name = rs.getString("customer_name");
                String address = rs.getString("customer_address");
                customerId = rs.getInt("customer_id");

                c_name.setText(name);
                c_address.setText(address);

                c_name.setEditable(false);
                c_address.setEditable(false);
                detailsContainer.setDisable(false);

            } else {
                detailsContainer.setDisable(true);
                messageL.setText("Add New Customer");
                messageL.setStyle("-fx-text-fill: blue");
                c_name.setEditable(true);
                c_address.setEditable(true);

                detailsContainer.setVisible(true);
                bnCheckOut.setVisible(false);
                bnCheckOut.managedProperty().bind(bnCheckOut.visibleProperty());

                System.out.println("customer not exist");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

    }

}
