package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.StockMainModel;
import com.shop.management.Model.SupplierModel;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ReStock implements Initializable {
    public ComboBox<SupplierModel> supplierCom;
    public TextField sNameTf;
    public TextField sPhoneTf;
    public TextField sEmailTf;
    public TextField sInvoiceNumberTf;
    public TextField quantityTf;
    public ComboBox<String> unitCom;

    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    private StockMainModel stockMainModel;

    private ObservableList<SupplierModel> supplierList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        stockMainModel =(StockMainModel) Main.primaryStage.getUserData();

       if (null == stockMainModel){
           return;
       }
        getSupplier();
        comboBox();

    }

    private void comboBox() {

       unitCom.getItems().add(stockMainModel.getProductFullQuantity().split(" -")[1]);
       unitCom.getSelectionModel().selectFirst();
        supplierCom.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(SupplierModel item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item.getSupplierName());
                    setAlignment(Pos.CENTER);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });

        supplierCom.setCellFactory(new Callback<>() {
            @Override
            public ListCell<SupplierModel> call(ListView<SupplierModel> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(SupplierModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item.getSupplierName());
                            setAlignment(Pos.CENTER);
                            setPadding(new Insets(3, 3, 3, 0));
                        }
                    }
                };
            }
        });

        supplierCom.valueProperty().addListener((observableValue, supplierModel, t1) ->{

            Connection connection = null;
            PreparedStatement ps = null;
            ResultSet rs = null;

            try {

                connection = dbConnection.getConnection();
                if (null == connection) {
                    System.out.println("connection failed");
                    return;
                }

                String query = "SELECT supplier_name,supplier_phone ,supplier_email  FROM Supplier where supplier_id = ?";
                ps = connection.prepareStatement(query);
                ps.setInt(1,t1.getSupplierId());

                rs = ps.executeQuery();

                while (rs.next()) {

                    String supplierName = rs.getString("supplier_name");
                    String supplierPhone = rs.getString("supplier_phone");
                    String supplierEmail = rs.getString("supplier_email");


                    sNameTf.setDisable(false);
                    sPhoneTf.setDisable(false);
                    sEmailTf.setDisable(false);

                    sNameTf.setText(supplierName);
                    sPhoneTf.setText(supplierPhone);
                    sEmailTf.setText(supplierEmail);

                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnection.closeConnection(connection, ps, rs);
            }
        });
    }

    private void getSupplier() {

        if (null != supplierList){
            supplierList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("connection failed");
                return;
            }

            String query = "SELECT * FROM Supplier order by supplier_id asc";
            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                int supplierId = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String supplierPhone = rs.getString("supplier_phone");
                String supplierEmail = rs.getString("supplier_email");
                String supplierGstNum = rs.getString("supplier_gstNo");
                String supplierAddress = rs.getString("ADDRESS");
                String supplierState = rs.getString("STATE");
                String date = rs.getString("added_date");

                supplierList.add(new SupplierModel(supplierId, supplierName, supplierPhone, supplierEmail, supplierGstNum, supplierAddress, supplierState, date));
            }
            supplierCom.setItems(supplierList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    public void addStock(ActionEvent event) {


        if (null == supplierCom.getSelectionModel().getSelectedItem()){
            method.show_popup("Select Supplier",supplierCom);
            return;
        }else if (quantityTf.getText().isEmpty()){
            method.show_popup("Please Enter Quantity",quantityTf);
            return;
        }else if (null == unitCom.getSelectionModel().getSelectedItem()){
            method.show_popup("Select Unit",unitCom);
            return;
        }

        int supplierId  = supplierCom.getSelectionModel().getSelectedItem().getSupplierId();
        int quantity = 0;
        try {
            quantity = Integer.parseInt(quantityTf.getText());
        } catch (NumberFormatException e) {
            method.show_popup("Please Enter Valid Quantity",quantityTf);
            return;
        }
        String quantityUnit = unitCom.getSelectionModel().getSelectedItem();
        if (quantity < 1){
            method.show_popup("Please Enter More Then 0",quantityTf);

            return;
        }

        Connection connection = null;
        PreparedStatement ps = null , psStock = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection){
                System.out.println("connection failed");
                return;
            }


            String stockUpdateQuery = "UPDATE tbl_product_stock SET quantity =  quantity + ? , quantity_unit = ? where stock_id = ?";

            psStock = connection.prepareStatement(stockUpdateQuery);
            psStock.setInt(1,quantity);
            psStock.setString(2,quantityUnit);
            psStock.setInt(3,stockMainModel.getStockId());

            int res = psStock.executeUpdate();

            if (res > 0 ){
                res = 0;

                String query = "INSERT INTO  purchase_history( SUPPLIER_ID, PRODUCT_ID, STOCK_ID, SELLER_ID, INVOICE_NUM, ACTIVITY_TYPE, QUANTITY) VALUES (?,?,?,?,?,?,?)";

                ps = connection.prepareStatement(query);
                ps.setInt(1,supplierId);
                ps.setInt(2,stockMainModel.getProductId());
                ps.setInt(3,stockMainModel.getStockId());
                ps.setInt(4,Login.currentlyLogin_Id);

                if (sInvoiceNumberTf.getText().isEmpty()){
                    ps.setNull(5 , Types.NULL);
                }else {
                    ps.setString(5 , sInvoiceNumberTf.getText());
                }

                ps.setString(6,"RE-STOCK");

                ps.setString(7,quantity+" -"+quantityUnit);

                res = ps.executeUpdate();

                if (res > 0){

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    if (stage.isShowing()){
                        stage.close();
                    }

                    customDialog.showAlertBox("SUCCESS","Stock SuccessFully Added");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {

            DBConnection.closeConnection(connection , ps , null);

            if (null != psStock){
                try {
                    psStock.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addSupplier(ActionEvent event) {

        customDialog.showFxmlDialog("stock/addSupplier.fxml", "ADD SUPPLIER");
        getSupplier();
    }
}
