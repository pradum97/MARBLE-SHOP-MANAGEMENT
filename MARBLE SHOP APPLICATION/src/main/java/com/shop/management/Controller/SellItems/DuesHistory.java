package com.shop.management.Controller.SellItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Model.DuesHistoryModel;
import com.shop.management.Model.Sale_Main;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class DuesHistory implements Initializable {

    public TableColumn<DuesHistoryModel, String> colDate;
    public TableColumn<DuesHistoryModel, String> colPaymentMode;
    public TableColumn<DuesHistoryModel, String> colPaid;
    public TableColumn<DuesHistoryModel, Integer> colSrNo;
    public TableColumn<DuesHistoryModel, String> colCurrentDues;
    public TableColumn<DuesHistoryModel, String> colPreDues;
    public TableView<DuesHistoryModel> tableView;
    public Label cusNameL;
    public Label cusPhoneL;
    public Label cusAddressL;
    public Label invoiceNumL;
    public BorderPane main;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private final ObservableList<DuesHistoryModel> historyList = FXCollections.observableArrayList();
    private int saleMainId;
    private Sale_Main saleMain;
    private Properties propRead;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        PropertiesLoader propLoader = new PropertiesLoader();
        propRead = propLoader.getReadProp();

        saleMain = (Sale_Main) Main.primaryStage.getUserData();
        if (null == saleMain) {
            customDialog.showAlertBox("", "History Not Available");
            return;
        }
        saleMainId = saleMain.getSale_main_id();
        setData();
        getDuesHistory();
    }

    private void setData() {
        cusNameL.setText(saleMain.getCustomerName());
        cusPhoneL.setText(saleMain.getCustomerPhone());
        cusAddressL.setText(saleMain.getCustomerAddress());
        invoiceNumL.setText(saleMain.getInvoiceNumber());
    }

    private void getDuesHistory() {
        if (null != historyList) {
            historyList.clear();
        }
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                return;
            }
            ps = connection.prepareStatement(propRead.getProperty("READ_DUES_HISTORY"));
            ps.setInt(1, saleMainId);
            rs = ps.executeQuery();

            while (rs.next()) {
                int duesHistoryId = rs.getInt("dues_history_id");
                int dues_id = rs.getInt("dues_id");
                int customerId = rs.getInt("customer_id");
                int saleMainId = rs.getInt("sale_main_id");

                double previousDues = rs.getDouble("previous_dues");
                double paidAmount = rs.getDouble("paid_amount");

                double current_dues = rs.getDouble("current_dues");

                String paymentMode = rs.getString("payment_mode");

                String paymentDate = rs.getString("payment_date");


                historyList.add(new DuesHistoryModel(duesHistoryId, dues_id, customerId, saleMainId, previousDues,
                        paidAmount, current_dues, paymentMode, paymentDate));
            }

            tableView.setItems(historyList);


            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    tableView.getItems().indexOf(cellData.getValue()) + 1));
            colPreDues.setCellValueFactory(new PropertyValueFactory<>("previousDues"));
            colPaid.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
            colCurrentDues.setCellValueFactory(new PropertyValueFactory<>("currentDues"));
            colPaymentMode.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
        tableView.setPlaceholder(new Label("History Not Available!"));
    }

}
