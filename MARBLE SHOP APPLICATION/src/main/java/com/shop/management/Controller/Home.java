package com.shop.management.Controller;

import com.shop.management.Method.Method;
import com.shop.management.Model.DailySaleReport;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Home implements Initializable {

    int rowsPerPage = 15;
    public BorderPane mainContainer;
    public Label totalProfitL;
    public Label totalSaleAmountL;
    public Label totalSaleItemL;
    public TableView<DailySaleReport> tableViewHome;
    public TableColumn<DailySaleReport, Integer> col_sno;
    public TableColumn<DailySaleReport, String> colProductName;
    public TableColumn<DailySaleReport, String> colProductType;
    public TableColumn<DailySaleReport, String> colProductSize;
    public TableColumn<DailySaleReport, String> colPurchasePrice;
    public TableColumn<DailySaleReport, String> colMrp;
    public TableColumn<DailySaleReport, String> colProductQuantity;
    public TableColumn<DailySaleReport, String> colSellPrice;
    public TableColumn<DailySaleReport, String> colProductDiscount;
    public TableColumn<DailySaleReport, String> colGst;
    public TableColumn<DailySaleReport, String> colNetAmount;
    public TableColumn<DailySaleReport, String> colBilType;
    public TextField searchTf;
    public HBox refresh_bn;
    public Pagination pagination;

    private Method method;
    private DBConnection dbConnection;

    ObservableList<DailySaleReport> reportList = FXCollections.observableArrayList();

    private FilteredList<DailySaleReport> filteredData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();

        getSaleItems();

    }

    private void calculate(){

        double profit = 0, totNetAmount = 0;

        for(DailySaleReport items : filteredData){

            String[] str = items.getQuantity().split(" ");
           int quantity = Integer.parseInt(str[0]) ;

           totNetAmount = totNetAmount+items.getNet_amount();

           profit = profit+(items.getNet_amount() - (items.getPurchase_price()*quantity));

        }


        totalSaleAmountL.setText("₹  "+totNetAmount);
        totalProfitL.setText("₹  "+profit);
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

            ps = connection.prepareStatement("select  sales_id , product_name , product_type ,product_size\n" +
                    "                    ,purchase_price, product_mrp ,sell_price , discount_amount\n" +
                    "                    , tax_amount , product_tax ,product_quantity ,net_amount,bill_type from tbl_saleItems " +
                    "where TO_CHAR(sale_date, 'yyyy-MM-dd' ) = TO_CHAR(CURRENT_DATE, 'yyyy-MM-dd') ORDER BY sales_id DESC ");

            rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {
                ++count;
                int saleId = rs.getInt("sales_id");
                String pName = rs.getString("product_name");
                String pType = rs.getString("product_type");
                String pSize = rs.getString("product_size");
                String pQuantity = rs.getString("product_quantity");
                String bill_type = rs.getString("bill_type");

                double purPrice = rs.getDouble("purchase_price");
                double pMrp = rs.getDouble("product_mrp");
                double sellPrice = rs.getDouble("sell_price");

                double disAmount = rs.getDouble("discount_amount");
                double taxAmount = rs.getDouble("tax_amount");
                double netAmount = rs.getDouble("net_amount");

                int tax = rs.getInt("product_tax");
                reportList.add(new DailySaleReport( saleId, pName, pType, pSize, pQuantity, purPrice, pMrp, sellPrice,
                        disAmount, netAmount, taxAmount + " ( " + tax + "% )", tax, bill_type));

            }

            totalSaleItemL.setText(String.valueOf(count));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

        if (reportList.size() > 0){
            pagination.setVisible(true);
            search_Item();
        }

        customColumn(colProductName);
    }

    private void customColumn(TableColumn<DailySaleReport, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<DailySaleReport, String> cell = new TableCell<>();
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
        calculate();
        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
    }

    private void search_Item() {

       filteredData = new FilteredList<>(reportList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(dailySaleReport -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (dailySaleReport.getProduct_name().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if
                (dailySaleReport.getProduct_type().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(dailySaleReport.getPurchase_price()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if
                (String.valueOf(dailySaleReport.getProduct_mrp()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (String.valueOf(dailySaleReport.getSell_price()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if
                (String.valueOf(dailySaleReport.getNet_amount()).toLowerCase().contains(lowerCaseFilter)) {

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


        calculate();

    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);


        col_sno.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableViewHome.getItems().indexOf(cellData.getValue()) + 1));
        colBilType.setCellValueFactory(new PropertyValueFactory<>("billType"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
        colProductType.setCellValueFactory(new PropertyValueFactory<>("product_type"));
        colProductSize.setCellValueFactory(new PropertyValueFactory<>("product_size"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchase_price"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("product_mrp"));
        colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sell_price"));
        colProductQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProductDiscount.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colProductDiscount.setCellValueFactory(new PropertyValueFactory<>("discount_amount"));
        colNetAmount.setCellValueFactory(new PropertyValueFactory<>("net_amount"));
        colGst.setCellValueFactory(new PropertyValueFactory<>("tax_amount"));


        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, reportList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<DailySaleReport> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableViewHome.comparatorProperty());

        tableViewHome.setItems(sortedData);

    }
}
