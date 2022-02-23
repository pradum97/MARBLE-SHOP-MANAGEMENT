package com.shop.management.Controller;

import com.shop.management.Method.CloseConnection;
import com.shop.management.Method.Method;
import com.shop.management.Model.SaleItems;
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
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SellReport implements Initializable {

    int rowsPerPage = 15;

    public TableColumn<SaleItems, Integer> col_sno;
    public TableColumn<SaleItems, String> c_name;
    public TableColumn<SaleItems, String> c_phone;
    public TableColumn<SaleItems, String> c_address;
    public TableColumn<SaleItems, String> colColor;
    public TableColumn<SaleItems, String> colName;
    public TableColumn<SaleItems, String> colSize;
    public TableColumn<SaleItems, String> colProductType;
    public TableColumn<SaleItems, String> colCategory;
    public TableColumn<SaleItems, String> colPurchasePrice;
    public TableColumn<SaleItems, String> colMrp;
    public TableColumn<SaleItems, String> colRate;
    public TableColumn<SaleItems, String> colQuantity;
    public TableColumn<SaleItems, String> colHsnSac;
    public TableColumn<SaleItems, String> colTax;
    public TableColumn<SaleItems, String> colDiscount;
    public TableColumn<SaleItems, String> colNetAmount;
    public TableColumn<SaleItems, String> colSellerName;
    public TableColumn<SaleItems, String> colBillType;
    public TableColumn<SaleItems, String> colDate;
    public TextField searchTf;
    public TableView<SaleItems> tableView;
    public TableColumn<SaleItems, String> colDiscName;
    public TableColumn<SaleItems, String> colInvoiceNumber;
    public Pagination pagination;
    private Method method;
    private DBConnection dbConnection;
    private double totNetAmt;


    ObservableList<SaleItems> reportList = FXCollections.observableArrayList();

    private FilteredList<SaleItems> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
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

            String query = "select  tsi.sales_id, tsi.customer_id, tsi.product_id, tsi.seller_id,tsi.stock_id ,tsi.invoice_number,\n" +
                    "        tsi.product_name ,tsi.product_color , tsi.product_category , tsi.product_type ,\n" +
                    "        tsi.product_size,tsi.purchase_price  , tsi.product_mrp ,tsi.sell_price ,tsi.product_quantity,tsi.discount_name ,tsi.discount_amount,\n" +
                    "        tsi.bill_type ,tsi.hsn_sac,tsi.product_tax ,tsi.tax_amount , tsi.net_amount , tsi.sale_date ,\n" +
                    "        tsi.igst, tsi.cgst,tsi. sgst , tc.customer_name, tc.customer_phone,tu.first_name , tu.last_name ,  tc.customer_address\n" +
                    "\n" +
                    "\n" +
                    "from tbl_saleItems as tsi\n" +
                    "                   LEFT JOIN tbl_customer as tc on tsi.customer_id = tc.customer_id\n" +
                    "                   LEFT JOIN tbl_users as tu on tsi.seller_id = tu.user_id ORDER BY tsi.sales_id DESC";

            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            double profit = 0, totNetAmount = 0;

            while (rs.next()) {

                int saleId = rs.getInt("sales_id");
                int customerId = rs.getInt("customer_id");
                int productId = rs.getInt("product_id");
                int stockId = rs.getInt("stock_id");
                int sellerId = rs.getInt("seller_id");
                String invoiceNumber = rs.getString("invoice_number");
                String pName = rs.getString("product_name");
                String pColor = rs.getString("product_color");
                String pCategory = rs.getString("product_category");
                String pType = rs.getString("product_type");
                String pSize = rs.getString("product_size");
                double purPrice = rs.getDouble("purchase_price");
                double pMrp = rs.getDouble("product_mrp");
                double sellPrice = rs.getDouble("sell_price");
                String pQuantity = rs.getString("product_quantity");
                String disName = rs.getString("discount_name");
                double disAmount = rs.getDouble("discount_amount");
                String bill_type = rs.getString("bill_type");
                int hsn = rs.getInt("hsn_sac");
                int totalTax = rs.getInt("product_tax");
                double taxAmount = rs.getDouble("tax_amount");
                double netAmount = rs.getDouble("net_amount");
                String date = rs.getString("sale_date");
                int igst = rs.getInt("igst");
                int cgst = rs.getInt("cgst");
                int sgst = rs.getInt("sgst");

                String customerName = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerAddress = rs.getString("customer_address");

                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                String[] strDate = date.split("\\.");

                reportList.add(new SaleItems(customerName, customerPhone, customerAddress, saleId, customerId, productId, stockId, sellerId, invoiceNumber, pName, pColor,
                        pSize, pType, pCategory, purPrice, pMrp, sellPrice, disAmount, disName, pQuantity, bill_type, hsn,
                        totalTax, netAmount, strDate[0], igst, cgst, sgst, fullName));

                totNetAmount = totNetAmount + netAmount;
                String[] str = pQuantity.split(" ");
                int quantity = Integer.parseInt(str[0]);
                profit = profit + (netAmount - (purPrice * quantity));

            }

            if (reportList.size() > 0){

                pagination.setVisible(true);
                search_Item();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseConnection.closeConnection(connection, ps, rs);
        }
        customColumn(colName);
        customColumn(c_name);
        customColumn(c_address);
        customColumn(c_phone);
        customColumn(colDiscName);
        customColumn(colDate);

        customColumn(colSellerName);
        customColumn(colInvoiceNumber);
        customColumn(colInvoiceNumber);
    }

    public void cal() {
        totNetAmt = 0.0;
        for (SaleItems fd : filteredData) {
            totNetAmt += fd.getNetAmount();

        }
    }

    private void customColumn(TableColumn<SaleItems, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<SaleItems, String> cell = new TableCell<>();
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
        cal();
        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);

    }

    private void search_Item() {

        filteredData = new FilteredList<>(reportList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(products -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (products.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (products.getProductType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getProductCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getProductColor().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (products.getDate().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (products.getCustomerName().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (String.valueOf(products.getTax()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (String.valueOf(products.getHsnSac()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }
                return false;
            });
            changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
            cal();
        });

        pagination.setCurrentPageIndex(0);
        changeTableView(0, rowsPerPage);
        pagination.currentPageIndexProperty().addListener(
                (observable1, oldValue1, newValue1) -> changeTableView(newValue1.intValue(), rowsPerPage));

    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        c_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        c_phone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        c_address.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));

        col_sno.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));

        colName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("productSize"));
        colProductType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        colRate.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colBillType.setCellValueFactory(new PropertyValueFactory<>("billType"));
        colHsnSac.setCellValueFactory(new PropertyValueFactory<>("hsnSac"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("tax"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discountAmount"));
        colNetAmount.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
        colSellerName.setCellValueFactory(new PropertyValueFactory<>("sellerName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDiscName.setCellValueFactory(new PropertyValueFactory<>("discountName"));
        colInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("invoiceNumber"));

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, reportList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<SaleItems> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

}
