package com.shop.management.Controller.ReturnItems;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.ReturnMainModel;
import com.shop.management.Model.Return_ItemsModel;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewReturnItem implements Initializable {

    public TableView<Return_ItemsModel> tableView;
    public TableColumn<Return_ItemsModel , Integer> colSrNo;
    public TableColumn<Return_ItemsModel , String> colProductName;
    public TableColumn<Return_ItemsModel , String> colProductSize;
    public TableColumn<Return_ItemsModel , String> colReturnQuantity;
    public TableColumn<Return_ItemsModel , String> rate;
    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection ;
    private ObservableList<Return_ItemsModel> itemList = FXCollections.observableArrayList();
    int returnMainId;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        returnMainId = (int) Main.primaryStage.getUserData();
        if (returnMainId <1){
            customDialog.showAlertBox("Failed","Item Not Found");
            return;
        }

       getItem();

    }

    private void getItem() {

        if (null != itemList){
            itemList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null ==  connection){
                System.out.println("connection Failed");
                return;
            }

            String query = "select tri.quantity_unit , tri.return_quantity ,ts.product_name , ts.product_size , tri.rate,\n" +
                    "tri.return_items_id from tbl_return_items tri\n" +
                    "LEFT JOIN tbl_return_main trm on tri.return_main_id = trm.return_main_id\n" +
                    "LEFT JOIN tbl_saleitems ts on tri.sale_item_id = ts.sale_item_id";


            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()){
                int returnItemsId = rs.getInt("return_items_id");

                String productName = rs.getString("product_name");
                String productSize = rs.getString("product_Size");
                String returnQuantity = rs.getString("return_quantity")+" -"+rs.getString("quantity_unit");
                double rate = rs.getDouble("rate");

                itemList.add(new Return_ItemsModel(returnItemsId,productName,productSize,returnQuantity , rate));

            }

            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    tableView.getItems().indexOf(cellData.getValue()) + 1));
            colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colProductSize.setCellValueFactory(new PropertyValueFactory<>("productSize"));
            colReturnQuantity.setCellValueFactory(new PropertyValueFactory<>("returnQuantity"));
            rate.setCellValueFactory(new PropertyValueFactory<>("rate"));

            tableView.setItems(itemList);

        } catch (SQLException e) {
            e.printStackTrace();
        }DBConnection.closeConnection(connection , ps,rs);
    }
}
