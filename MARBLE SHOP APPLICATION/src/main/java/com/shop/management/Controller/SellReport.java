package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Dues;
import com.shop.management.Model.Sale_Main;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SellReport implements Initializable {
    int rowsPerPage = 15;

    public TableColumn<Sale_Main, Integer> col_sno;
    public TableColumn<Sale_Main, String> colCheck;
    public TableColumn<Sale_Main, String> c_name;
    public TableColumn<Sale_Main, String> c_phone;
    public TableColumn<Sale_Main, String> c_address;
    public TableColumn<Sale_Main, String> colNetAmount;
    public TableColumn<Sale_Main, String> colSellerName;
    public TableColumn<Sale_Main, String> colBillType;
    public TableColumn<Sale_Main, String> colDate;
    public TableColumn<Sale_Main, String> colAddiDisc;
    public TableColumn<Sale_Main, String> colReceivedAmt;
    public TableColumn<Sale_Main, String> colPayDues;
    public TableColumn<Sale_Main, String> colDuesAmt;
    public TableColumn<Sale_Main, String> colTotDisc;
    public TableColumn<Sale_Main, String> colTotTax;
    public TableColumn<Sale_Main, String> colPaymentMode;
    public TableColumn<Sale_Main, String> colTotItem;
    public TextField searchTf;
    public TableView<Sale_Main> tableView;
    public TableColumn<Sale_Main, String> colInvoiceNumber;
    public Pagination pagination;
    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;


    ObservableList<Sale_Main> reportList = FXCollections.observableArrayList();

    private FilteredList<Sale_Main> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        getSaleItems();

        String query = "select  * from tbl_saleitems where TO_CHAR(sale_date, 'YYYY-MM-DD') between TO_CHAR(?, 'YYYY-MM-DD') and TO_CHAR(?, 'YYYY-MM-DD')\n";

    }

    private void getSaleItems() {
        if (null != reportList) {
            reportList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("home : Connection failed");
                return;
            }

            String query ="select (select count(sale_item_id) from tbl_saleitems tsi where (tsi.sale_main_id = tsm.sale_main_id) )\n" +
                    "           as totalItem ,tsm.sale_main_id, td.dues_id ,tc.customer_id,tsm.seller_id,tsm.additional_discount,\n" +
                    "       tsm.received_amount,tsm.tot_disc_amount,tsm.tot_tax_amount,tsm.net_amount,tsm.payment_mode,tsm.invoice_number,\n" +
                    "       tsm.bill_type,tsm.date,tc.customer_name,tc.customer_phone,tc.customer_address , tu.first_name,tu.last_name,td.dues_amount\n" +
                    "       from tbl_sale_main tsm\n" +
                    "                                     LEFT JOIN tbl_customer tc on (tsm.customer_id = tc.customer_id)\n" +
                    "                                     LEFT JOIN tbl_users tu on (tsm.seller_id = tu.user_id)\n" +
                    "                                     LEFT JOIN tbl_dues td on tsm.sale_main_id = td.sale_main_id";

            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                int saleMainId = rs.getInt("sale_main_id");
                int customerId = rs.getInt("customer_id");
                int sellerId = rs.getInt("seller_id");
                double additionalDisc = rs.getDouble("additional_discount");
                double receivedAmount = rs.getDouble("received_amount");
                double totDiscAmt = rs.getDouble("tot_disc_amount");
                double totTaxAmt = rs.getDouble("tot_tax_amount");
                double netAmount = rs.getDouble("net_amount");
                String paymentMode = rs.getString("payment_mode");
                String invoiceNumber = rs.getString("invoice_number");
                String billType = rs.getString("bill_type");

                String[] date = rs.getString("date").split("\\.");
                String sellingDate = date[0];

                String customerName = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerAddress = rs.getString("customer_address");
                String sellerName = rs.getString("first_name")+" "+ rs.getString("last_name");

                // DUES
                double duesAmount = rs.getDouble("dues_amount");
                int duesId = rs.getInt("dues_id");

                int totalItems = rs.getInt("totalItem");

                reportList.add(new Sale_Main(saleMainId,duesId,customerId , sellerId , customerName , customerPhone , customerAddress , additionalDisc , receivedAmount , totDiscAmt,
                       totTaxAmt , netAmount ,  paymentMode , billType , invoiceNumber , sellerName , sellingDate,totalItems,duesAmount));

            }

            if (reportList.size() > 0) {

                pagination.setVisible(true);
                search_Item();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    private void customColumn(TableColumn<Sale_Main, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<Sale_Main, String> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-font-size: 14");
            cell.setGraphic(text);
            text.setStyle("-fx-text-alignment: CENTER ; -fx-padding: 10");
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    public void bnRefresh(MouseEvent event) {

        getSaleItems();
        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
        setOptional();

    }

    private void search_Item() {

        filteredData = new FilteredList<>(reportList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(saleMain -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (saleMain.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (String.valueOf(saleMain.getCustomerPhone()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (saleMain.getInvoiceNumber().toLowerCase().contains(lowerCaseFilter)){
                    return true;
                }
                return false;
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

        col_sno.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));
        c_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        c_phone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        c_address.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        colTotItem.setCellValueFactory(new PropertyValueFactory<>("totalItems"));
        colAddiDisc.setCellValueFactory(new PropertyValueFactory<>("additionalDisc"));
        colReceivedAmt.setCellValueFactory(new PropertyValueFactory<>("receivedAmount"));
        colDuesAmt.setCellValueFactory(new PropertyValueFactory<>("duesAmount"));
        colTotDisc.setCellValueFactory(new PropertyValueFactory<>("totalDiscountAmount"));
        colTotTax.setCellValueFactory(new PropertyValueFactory<>("totalTaxAmount"));
        colNetAmount.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
        colInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));
        colPaymentMode.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
        colBillType.setCellValueFactory(new PropertyValueFactory<>("billType"));
        colSellerName.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("sellingDate"));

        setOptional();

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, reportList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Sale_Main> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

    private void setOptional() {
        Callback<TableColumn<Sale_Main, String>, TableCell<Sale_Main, String>>
                checkItems = (TableColumn<Sale_Main, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {
                    Label bnCheckPrice = new Label("CHECK ITEMS");

                    bnCheckPrice.setStyle("-fx-background-color: #ff0000; -fx-background-radius: 25 ; -fx-font-family: 'Bookman Old Style'; " +
                            "-fx-padding: 5 8 5 8 ; -fx-text-fill: white; -fx-alignment: center;-fx-cursor: hand");


                    bnCheckPrice.setOnMouseClicked(event -> {
                        Sale_Main saleMain = tableView.getSelectionModel().getSelectedItem();
                        if (null == tableView) {
                            System.out.println("Items Not Found");
                            return;
                        }

                        Main.primaryStage.setUserData(saleMain.getSale_main_id());


                        customDialog.showFxmlFullDialog("sellItems/viewSellItems.fxml", "All Items - "+saleMain.getCustomerName()+" / "+saleMain.getSellingDate()+" / Invoice- "+
                                saleMain.getInvoiceNumber() );
                        bnRefresh(null);
                    });

                    HBox container = new HBox(bnCheckPrice);
                    container.setStyle("-fx-alignment:center");
                    HBox.setMargin(bnCheckPrice, new Insets(2, 20, 2, 20));
                    setGraphic(container);

                    setText(null);
                }
            }

        };


        Callback<TableColumn<Sale_Main, String>, TableCell<Sale_Main, String>>
                payDues = (TableColumn<Sale_Main, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {
                    double duesAmt = filteredData.get(getIndex()).getDuesAmount();
                    int duesId = filteredData.get(getIndex()).getDuesId();
                    Label bnPayDues = new Label("PAY DUES");
                    Label bnDuesHistory = new Label("DUES HISTORY");
                    bnPayDues.setVisible(false);
                    bnPayDues.managedProperty().bind(bnPayDues.visibleProperty());
                   /* bnDuesHistory.setVisible(false);
                    bnDuesHistory.managedProperty().bind(bnPayDues.visibleProperty());*/
                    if (duesAmt > 0){
                        bnPayDues.setVisible(true);
                    }

                    bnPayDues.setStyle("-fx-background-color: #0881ea; -fx-background-radius: 25 ; -fx-font-family: 'Bookman Old Style'; " +
                            "-fx-padding: 4 11 4 11 ; -fx-text-fill: white; -fx-alignment: center;-fx-cursor: hand");

                    bnDuesHistory.setStyle("-fx-background-color: #a7a4a8; -fx-background-radius: 25 ; -fx-font-family: 'Bookman Old Style'; " +
                            "-fx-padding: 4 11 4 11 ; -fx-text-fill: white; -fx-alignment: center;-fx-cursor: hand");


                    bnPayDues.setOnMouseClicked(event -> {
                        Sale_Main saleMain = tableView.getSelectionModel().getSelectedItem();
                        if (null == tableView) {
                            System.out.println("Items Not Found");
                            return;
                        }

                        Main.primaryStage.setUserData(saleMain);


                        customDialog.showFxmlFullDialog("sellItems/payDues.fxml", "PAY DUES - "+saleMain.getCustomerName()+" / "+saleMain.getSellingDate()+" / Invoice- "+
                                saleMain.getInvoiceNumber()+"DUES-ID- "+saleMain.getDuesId() );
                        bnRefresh(null);
                    });

                    bnDuesHistory.setOnMouseClicked(event -> {
                        Sale_Main saleMain = tableView.getSelectionModel().getSelectedItem();
                        if (null == tableView) {
                            System.out.println("Items Not Found");
                            return;
                        }

                        Main.primaryStage.setUserData(saleMain);


                        customDialog.showFxmlDialog("sellItems/duesHistory.fxml", "DUES HISTORY " );
                        bnRefresh(null);
                    });

                    HBox container = new HBox(bnPayDues,bnDuesHistory);
                    container.setStyle("-fx-alignment:center");
                    if (bnPayDues.isVisible()){
                        HBox.setMargin(bnPayDues, new Insets(2, 10, 2, 0));
                    }
                    HBox.setMargin(bnDuesHistory, new Insets(2, 0, 2, 0));
                    setGraphic(container);

                    setText(null);
                }
            }
        };

        colCheck.setCellFactory(checkItems);
        colPayDues.setCellFactory(payDues);

        customColumn(c_name);
        customColumn(c_address);
        customColumn(colDate);
    }

}
