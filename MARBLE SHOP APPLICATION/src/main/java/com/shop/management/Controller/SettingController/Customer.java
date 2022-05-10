package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.Model.CustomerModel;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Customer implements Initializable {
    private int rowsPerPage = 15;
    public TableView<CustomerModel> tableView;
    public TableColumn<CustomerModel, Integer> colSrNo;
    public TableColumn<CustomerModel, String> colCId;
    public TableColumn<CustomerModel, String> colGstNum;
    public TableColumn<CustomerModel, String> colName;
    public TableColumn<CustomerModel, String> colPhone;
    public TableColumn<CustomerModel, String> colAddress;
    public TableColumn<CustomerModel, String> colDesc;
    public TableColumn<CustomerModel, String> colDate;
    public Pagination pagination;
    public TextField searchTf;
    private Properties propRead;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private FilteredList<CustomerModel> filteredData;


    private ObservableList<CustomerModel> customerList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        PropertiesLoader propLoader = new PropertiesLoader();
        propRead = propLoader.getReadProp();

        getCustomer();

    }

    private void onColumnEdit(TableColumn<CustomerModel, String> col, String updateColumnName) {

        col.setCellFactory(TextFieldTableCell.forTableColumn());

        col.setOnEditCommit(e -> {

            String value = e.getNewValue();
            if (value.isEmpty()) {
                getCustomer();
                customDialog.showAlertBox("Failed", "Empty Value Not Accepted");
                return;
            }
            int customerId = e.getTableView().getItems().get(e.getTablePosition().getRow()).getCustomerId();
            update(e.getNewValue(), updateColumnName, customerId);

        });
    }

    private void update(String newValue, String columnName, int customerId) {

        Connection connection = null;
        PreparedStatement ps = null;
        try {

            connection = dbConnection.getConnection();

            if (null == connection) {
                return;
            }

            String query = "UPDATE TBL_CUSTOMER SET " + columnName + " = ?  where customer_id = ?";

            ps = connection.prepareStatement(query);

            ps.setString(1, newValue);
            ps.setInt(2, customerId);

            int res = ps.executeUpdate();

            if (res > 0) {
                getCustomer();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, null);
        }
    }


    private void getCustomer() {

        if (null != customerList) {
            customerList.clear();
        }
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                return;
            }
            ps = connection.prepareStatement(propRead.getProperty("READ_ALL_CUSTOMER"));

            rs = ps.executeQuery();

            while (rs.next()) {
                int customer_id = rs.getInt("customer_id");
                String customer_name = rs.getString("customer_name");
                long customer_phone = rs.getLong("customer_phone");
                String customer_address = rs.getString("customer_address");
                String description = rs.getString("description");
                String registered_date = rs.getString("registered_date");
                String gstNum = rs.getString("GST_NUMBER");
                customerList.add(new CustomerModel(customer_id, customer_name, customer_phone, customer_address, 0, null, description, registered_date,gstNum));

            }

           if (null != customerList){
               if (customerList.size() > 0) {
                   pagination.setVisible(true);
                   search_Item();
               }
           }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

    }

    private void search_Item() {

        filteredData = new FilteredList<>(customerList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(products -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(products.getCustomerId()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (String.valueOf(products.getPhone()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return products.getName().toLowerCase().contains(lowerCaseFilter);
            });

            changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
        });

        pagination.setCurrentPageIndex(0);
        changeTableView(0, rowsPerPage);
        pagination.currentPageIndexProperty().addListener(
                (observable1, oldValue1, newValue1) -> {
                    changeTableView(newValue1.intValue(), rowsPerPage);
                });

    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));
        colCId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("registered_date"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colGstNum.setCellValueFactory(new PropertyValueFactory<>("gstNum"));

        onColumnEdit(colName, "customer_name");
        onColumnEdit(colAddress, "customer_address");
        onColumnEdit(colDesc, "description");
        onColumnEdit(colGstNum, "GST_NUMBER");

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, customerList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<CustomerModel> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
    }

    public void addCustomer(ActionEvent event) {
        customDialog.showFxmlDialog("setting/addCustomer.fxml","ADD NEW CUSTOMER");
        getCustomer();
    }

    public void gstVerification(ActionEvent event) {
        new CustomDialog().showFxmlFullDialog("sellItems/gstVerification.fxml" , "GST VERIFICATION");

    }
}
