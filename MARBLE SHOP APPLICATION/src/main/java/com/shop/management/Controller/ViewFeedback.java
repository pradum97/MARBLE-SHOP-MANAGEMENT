package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.Model.Feedback;
import com.shop.management.Model.UserDetails;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ViewFeedback implements Initializable {

    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private Properties properties;

    public TableColumn<Feedback, String> col_id;
    public TableColumn<Feedback, String> col_name;
    public TableColumn<Feedback, String> col_email;
    public TableColumn<Feedback, String> col_phone;
    public TableColumn<Feedback, String> col_star;
    public TableColumn<Feedback, String> col_message;
    public TableColumn<Feedback, String> col_date;
    public TableView<Feedback> tableView ;

    ObservableList<Feedback> feedList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");

        setData();
    }

    private void setData() {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = dbConnection.getConnection();

            if (null == connection){
                return;
            }

            ps = connection.prepareStatement(properties.getProperty("GET_FEEDBACK"));

            rs = ps.executeQuery();

            while (rs.next()){

                int feedId = rs.getInt("feedback_id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                long phone = rs.getLong("phone");
                String star = rs.getString("star");
                String message = rs.getString("message");
                String date = rs.getString("feedback_date");

                feedList.add(new Feedback(feedId,phone,fullName,email,star,message,date));
            }

            tableView.setItems(feedList);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        col_id.setCellValueFactory(new PropertyValueFactory<>("feed_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("feed_phone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_star.setCellValueFactory(new PropertyValueFactory<>("star"));
        col_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("feedDate"));


        customColumn(col_name);
        customColumn(col_email);
        customColumn(col_message);
        customColumn(col_date);
    }

    private void customColumn(TableColumn<Feedback, String> columnName) {


        columnName.setCellFactory(tc -> {
            TableCell<Feedback, String> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-font-size: 14");
            cell.setGraphic(text);
            text.setId("t");
            text.setStyle("-fx-text-alignment: CENTER ;  -fx-padding: 10");
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }
}
