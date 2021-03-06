package com.shop.management.Controller.ReturnItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Model.ReturnMainModel;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ReturnHistory implements Initializable {

    private int rowsPerPage = 15;

    public TableColumn<ReturnMainModel, Integer> colSrNo;
    public TableColumn<ReturnMainModel, String> colCusName;
    public TableColumn<ReturnMainModel, String> colCusPhone;
    public TableColumn<ReturnMainModel, String> colRefundable;
    public TableColumn<ReturnMainModel, String> colOldVoiceNum;
    public TableColumn<ReturnMainModel, String> colNewInvoice;
    public TableColumn<ReturnMainModel, String> colReturnDate;
    public TableColumn<ReturnMainModel, String> colCheckItem;
    public TableColumn<ReturnMainModel, String> colRemark;
    public TableView<ReturnMainModel> tableView;
    public TextField searchTF;
    public Pagination pagination;

    private ObservableList<ReturnMainModel> returnList = FXCollections.observableArrayList();
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private FilteredList<ReturnMainModel> filteredData;
    private Properties  propRead;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        PropertiesLoader propLoader = new PropertiesLoader();
        propRead = propLoader.getReadProp();

        getReturnItem();

    }

    private void getReturnItem() {

        if (returnList != null) {
            returnList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                return;
            }
            String query = propRead.getProperty("GET_ALL_RETURN_ITEMS");

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {
                int returnMainId = rs.getInt("return_main_id");
                int saleMainId = rs.getInt("sale_main_id");
                int sellerId = rs.getInt("seller_id");

                String returnDate = rs.getString("returnDate");

                double totalRefundAmount = rs.getDouble("total_refund_amount");

                String oldInvoice = rs.getString("old_invoice_number");
                String newInvoice = rs.getString("invoice_number");
                String remark = rs.getString("remark");
                String customer_name = rs.getString("customer_name");
                String customer_phone = rs.getString("customer_phone");

                returnList.add(new ReturnMainModel(returnMainId, saleMainId, sellerId, oldInvoice, newInvoice, remark, returnDate, totalRefundAmount, customer_name, customer_phone));


            }

            if (null != returnList){
                if (returnList.size() > 0) {
                    pagination.setVisible(true);
                    search_Item();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DBConnection.closeConnection(connection, ps, rs);
    }

    private void search_Item() {

        filteredData = new FilteredList<>(returnList, p -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(rList -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(rList.getCustomerName()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (rList.getCustomerPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rList.getOldInvoiceNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return rList.getNewInvoiceNum().toLowerCase().contains(lowerCaseFilter);
            });

            changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
        });

        pagination.setCurrentPageIndex(0);
        changeTableView(0, rowsPerPage);
        pagination.currentPageIndexProperty().addListener(
                (observable1, oldValue1, newValue1) -> changeTableView(newValue1.intValue(), rowsPerPage));

    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));
        colCusName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colCusPhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        colRefundable.setCellValueFactory(new PropertyValueFactory<>("totalRefundableAmount"));
        colOldVoiceNum.setCellValueFactory(new PropertyValueFactory<>("oldInvoiceNum"));
        colNewInvoice.setCellValueFactory(new PropertyValueFactory<>("newInvoiceNum"));
        colReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        colRemark.setCellValueFactory(new PropertyValueFactory<>("remark"));


        customColumn(colRemark);
        customColumn(colCusName);


        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, returnList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<ReturnMainModel> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);
        setOptionalCell();
    }

    private void customColumn(TableColumn<ReturnMainModel, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<ReturnMainModel, String> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-font-size: 14");
            cell.setGraphic(text);
            text.setStyle("-fx-text-alignment: CENTER ;");
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    private void setOptionalCell() {
        Callback<TableColumn<ReturnMainModel, String>, TableCell<ReturnMainModel, String>>
                checkItems = (TableColumn<ReturnMainModel, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {
                    Label bnCheckItem = new Label("Check Items");

                    double height = 36;
                    double width = 150;

                    bnCheckItem.setMinWidth(height);
                    bnCheckItem.setMinWidth(width);

                    bnCheckItem.setStyle("-fx-background-color: #008080; -fx-background-radius: 25 ;" +
                            "-fx-padding: 3 3 3 3 ; -fx-text-fill: white; -fx-alignment: center;-fx-cursor: hand");


                    bnCheckItem.setOnMouseClicked(event -> {
                        ReturnMainModel rmm = tableView.getSelectionModel().getSelectedItem();
                        if (null == tableView) {
                            return;
                        }

                        Main.primaryStage.setUserData(rmm.getReturnMainId());


                        customDialog.showFxmlFullDialog("returnItems/view_Return_Item.fxml", "All Items - " + rmm.getCustomerName() + " / " + rmm.getTotalRefundableAmount() + " / Invoice- " +
                                rmm.getNewInvoiceNum());

                        getReturnItem();
                    });

                    HBox container = new HBox(bnCheckItem);
                    container.setStyle("-fx-alignment:center");
                    setGraphic(container);

                    setText(null);
                }
            }

        };
        colCheckItem.setCellFactory(checkItems);

    }
}
