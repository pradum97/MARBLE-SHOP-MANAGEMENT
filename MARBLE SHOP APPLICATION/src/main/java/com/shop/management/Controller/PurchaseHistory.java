package com.shop.management.Controller;

import com.shop.management.Model.PurchaseHistoryModel;
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

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class PurchaseHistory implements Initializable {
    private int rowsPerPage = 15;
    public TableView<PurchaseHistoryModel> tableView;
    public TableColumn<PurchaseHistoryModel, Integer> colSrNo;
    public TableColumn<PurchaseHistoryModel, String> colSupplierName;
    public TableColumn<PurchaseHistoryModel, String> colInvoiceNum;
    public TableColumn<PurchaseHistoryModel, String> colProductCode;
    public TableColumn<PurchaseHistoryModel, String> colSize;
    public TableColumn<PurchaseHistoryModel, String> colQuantity;
    public TableColumn<PurchaseHistoryModel, String> colDate;
    public TableColumn <PurchaseHistoryModel, String>  colPurPrice;
    public TableColumn <PurchaseHistoryModel, String>  colMrp;
    public TableColumn <PurchaseHistoryModel, String>  colMinSale;

    public TextField searchTf;
    public Pagination pagination;

    private DBConnection dbConnection;
    private FilteredList<PurchaseHistoryModel> filteredData;

    private ObservableList<PurchaseHistoryModel> purchaseList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
        getPurchaseHistory();
    }

    private void getPurchaseHistory() {

        if (null !=  purchaseList){
            purchaseList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
             connection = dbConnection.getConnection();
             if (null == connection){
                 return;
             }

             String query = "SELECT s.supplier_name , s.supplier_id ,ph.purchase_price , ph.mrp , ph.min_sell, ph.purchase_id , ph.product_id , ph.stock_id , ph.seller_id\n" +
                     "       , ph.invoice_num , tp.product_code , (concat(tps.height,'x' , tps.width , ' ',tps.size_unit)) as size,\n" +
                     "       ph.quantity , ph.activity_type ,(TO_CHAR(ph.purchase_date, 'DD-MM-YYYY')) as purchase_date\n" +
                     "FROM purchase_history ph\n" +
                     "         left join supplier s on ph.supplier_id = s.supplier_id\n" +
                     "         left join tbl_products tp on ph.product_id = tp.product_id\n" +
                     "         left join tbl_product_stock tps on ph.stock_id = tps.stock_id";

             ps = connection.prepareStatement(query);

             rs = ps.executeQuery();

             while (rs.next()){
                 int supplierId = rs.getInt("supplier_id");
                 int productId = rs.getInt("product_id");
                 int purchaseId = rs.getInt("purchase_id");
                 int stockId = rs.getInt("stock_id");
                 int sellerId = rs.getInt("seller_id");

                 String supplierName = rs.getString("supplier_name");
                 String invoiceNum = rs.getString("invoice_num");
                 String productCode = rs.getString("product_code");
                 String size = rs.getString("size");
                 String quantity = rs.getString("quantity");
                 String activity = rs.getString("activity_type");
                 String purchaseDate = rs.getString("purchase_date");

                 double purchasePrice = rs.getDouble("purchase_price");
                 double mrp = rs.getDouble("mrp");
                 double minSell = rs.getDouble("min_sell");

                 String invoice = Objects.requireNonNullElse(invoiceNum, "-");

                purchaseList.add(new PurchaseHistoryModel(purchaseId , supplierId , productId , stockId , sellerId , supplierName ,
                        invoice , productCode , size , quantity , activity , purchaseDate , purchasePrice , mrp , minSell));
             }

            if (purchaseList.size() > 0) {
                pagination.setVisible(true);
                search_Item();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnection.closeConnection(connection , ps, rs);
        }

    }

    private void search_Item() {

        filteredData = new FilteredList<>(purchaseList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(products -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (products.getProductCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (products.getInvoiceNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getSupplierName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else return String.valueOf(products.getQuantity()).toLowerCase().contains(lowerCaseFilter);
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
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colInvoiceNum.setCellValueFactory(new PropertyValueFactory<>("invoiceNum"));
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPurPrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
        colMinSale.setCellValueFactory(new PropertyValueFactory<>("minSell"));


        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, purchaseList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<PurchaseHistoryModel> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);


    }

    public void bnRefresh(ActionEvent event) {

        getPurchaseHistory();
    }
}
