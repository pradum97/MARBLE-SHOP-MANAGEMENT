package com.shop.management.Controller.ReturnItems;

import com.shop.management.Controller.Login;
import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.ReturnProductModel;
import com.shop.management.util.DBConnection;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class ReturnProduct implements Initializable {

    public TextField invoiceNumTF;
    public Label cusNameL;
    public Label cusPhoneL;
    public Label cusAddressL;
    public Label purchaseDateL;
    public Label netAmountL;
    public Label billTypeL;
    public Label duesL;
    public TableView<ReturnProductModel> tableView;
    public TableColumn<ReturnProductModel, Integer> colSrNo;
    public TableColumn<ReturnProductModel, String> colProductName;
    public TableColumn<ReturnProductModel, String> colProductSIze;
    public TableColumn<ReturnProductModel, String> colQuantity;
    public TableColumn<ReturnProductModel, String> colRate;
    public TableColumn<ReturnProductModel, String> colAction;
    public Button searchBn;
    public TextField invoicePrefetchTF;
    public TableColumn<ReturnProductModel, CheckBox> colCheckBox;
    public Label refundL;
    public TextArea remarkTF;
    public Button bnSubmit;
    public TextField discountTF;
    private DBConnection dbConnection;
    private Method method;
    private CustomDialog customDialog;
    double netReturnAmtTot = 0;
    private double totalReturnDisctAmount = 0;
    private int saleMainID;
    private double discountPer;
    private String oldInvoiceNumber;
    private double totalDiscount , netAmount;
    private ObservableList<ReturnProductModel> itemList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();

        textChangeListener();
    }

    private void textChangeListener() {

        discountTF.textProperty().addListener((observableValue, s, t1) -> {

            double currentDiscount = 0;
            try {
                currentDiscount = Double.parseDouble(t1.replaceAll("[^0-9.]", ""));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (netReturnAmtTot < currentDiscount){
                method.show_popup("Discount Amount Should Not Be Exceed Price !",discountTF);
                return;

            }
            refundL.setText(String.valueOf(netReturnAmtTot - currentDiscount));

        });
    }

    public void bnSearch(ActionEvent event) {

        String str = searchBn.getText();
        if ("RESET".equals(str)) {
            resetValue();
            return;
        }

        String invPrefix = invoicePrefetchTF.getText();
        String invoiceNumber = invoiceNumTF.getText();

        if (invoiceNumber.isEmpty()) {

            ContextMenu menu = new ContextMenu();
            menu.setAutoHide(true);
            menu.getItems().add(new MenuItem("Enter Invoice Number "));
            menu.show(invoiceNumTF, Side.BOTTOM, 10, 0);

            return;
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection) {
                System.out.println("connection Failed");
                return;
            }

            String query = "select tsm.sale_main_id,tsm.tot_disc_amount  ,tsi.product_id,tsi.stock_id,tsi.discountPer , tsi.sale_item_id ,tsm.invoice_number , tc.customer_name , tc.customer_phone, tc.customer_address,\n" +
                    "       (TO_CHAR( tsm.sale_date, 'DD-MM-YYYY HH12:MI AM')) as sale_date , tsm.net_amount , tsm.bill_type ,td.dues_amount,\n" +
                    "       tsi.product_name , tsi.product_size , tsi.product_quantity , tsi.sell_price\n" +
                    "from tbl_sale_main tsm\n" +
                    "         LEFT JOIN tbl_saleitems tsi on tsm.sale_main_id = tsi.sale_main_id\n" +
                    "         LEFT JOIN tbl_customer tc on tsm.customer_id = tc.customer_id\n" +
                    "         LEFT JOIN tbl_dues td on tsm.sale_main_id = td.sale_main_id where tsm.invoice_number = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, invPrefix + invoiceNumber);

            rs = ps.executeQuery();

            int res = 0;
            while (rs.next()) {

                int saleMainId = rs.getInt("sale_main_id");
                int saleItemId = rs.getInt("sale_item_id");
                int productId = rs.getInt("product_id");
                int stockId = rs.getInt("stock_id");

                String invoiceNum = rs.getString("invoice_number");

                String customerName = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerAddress = rs.getString("customer_address");
                String saleDate = rs.getString("sale_date");
                String billType = rs.getString("bill_type");
                String productName = rs.getString("product_name");
                String ReturnProductModelize = rs.getString("product_size");
                String productQuantity = rs.getString("product_quantity");

                double net_Amount = rs.getDouble("net_amount");
                double duesAmount = rs.getDouble("dues_amount");
                double rate = rs.getDouble("sell_price");
                double discountPer = rs.getDouble("discountPer");


                if (rs.isLast()) {
                    res++;
                    totalDiscount = rs.getDouble("tot_disc_amount");
                    netAmount = net_Amount;
                    cusNameL.setText(customerName.toUpperCase());
                    cusPhoneL.setText(customerPhone);
                    cusAddressL.setText(customerAddress);
                    purchaseDateL.setText(saleDate);
                    netAmountL.setText(String.valueOf(netAmount));
                    billTypeL.setText(billType);
                    duesL.setText(String.valueOf(duesAmount));
                    searchBn.setText("RESET");
                    invoiceNumTF.setEditable(false);
                    saleMainID = saleMainId;
                    oldInvoiceNumber = invoiceNum;
                }
                itemList.add(new ReturnProductModel(false, productId, stockId, saleMainId, saleItemId, productName, ReturnProductModelize, productQuantity, rate, discountPer, "0"));

            }
            if (res < 1) {
                customDialog.showAlertBox("Failed", "Invoice Not Found");
            }

            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    tableView.getItems().indexOf(cellData.getValue()) + 1));
            colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colProductSIze.setCellValueFactory(new PropertyValueFactory<>("productSize"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            colRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
            colAction.setCellValueFactory(new PropertyValueFactory<>("returnQuantity"));
            colCheckBox.setCellValueFactory(new PropertyValueFactory<>("isReturn"));

            setCellF();

            colAction.setCellFactory(TextFieldTableCell.forTableColumn());
            colAction.setOnEditCommit(e -> {
                e.getTableView().getItems().get(e.getTablePosition().getRow()).setReturnQuantity(e.getNewValue());

            });


            tableView.setItems(itemList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    private void setCellF() {

        colCheckBox.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ReturnProductModel, CheckBox>, ObservableValue<CheckBox>>() {

            @Override
            public ObservableValue<CheckBox> call(
                    TableColumn.CellDataFeatures<ReturnProductModel, CheckBox> arg0) {
                ReturnProductModel rpm = arg0.getValue();

                CheckBox checkBox = new CheckBox();

                checkBox.selectedProperty().setValue(rpm.isReturn());

                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                    public void changed(ObservableValue<? extends Boolean> ov,
                                        Boolean old_val, Boolean new_val) {

                        int returnQuantity = 0;
                        int quantity = 0;
                        try {
                            quantity = Integer.parseInt(rpm.getQuantity().split(" -")[0].replaceAll("[^0-9.]", ""));
                            returnQuantity = Integer.parseInt(rpm.getReturnQuantity().replaceAll("[^0-9.]", ""));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }


                        if (returnQuantity < 1) {
                            method.show_popup("Please Enter Return Quantity ", checkBox);
                            checkBox.setSelected(false);
                            return;
                        }
                        if (returnQuantity > quantity) {

                            method.show_popup("ENTER VALID RETURN QUANTITY " +
                                    "Available Quality : " + rpm.getQuantity() + " You Enter :" + returnQuantity, checkBox);

                            checkBox.setSelected(false);
                            return;
                        }
                        rpm.setReturn(new_val);
                        calculate();
                    }
                });
                return new SimpleObjectProperty<CheckBox>(checkBox);
            }
        });
    }

    private void resetValue() {
        cusNameL.setText("");
        cusPhoneL.setText("");
        cusAddressL.setText("");
        purchaseDateL.setText("");
        netAmountL.setText("");
        billTypeL.setText("");
        duesL.setText("");
        invoiceNumTF.setText("");
        searchBn.setText("SEARCH");
        invoiceNumTF.setEditable(true);
        if (!itemList.isEmpty()) {
            itemList.clear();
        }

        calculate();

        tableView.refresh();

    }

    public void test(MouseEvent mouseEvent) {

        for (ReturnProductModel rtm : tableView.getItems()) {

            System.out.println(rtm.getReturnQuantity());
        }
    }

    public void bnSubmit(ActionEvent event) {
        ImageView image = new ImageView(new ImageLoader().load("img/icon/warning_ic.png"));
        image.setFitWidth(45);
        image.setFitHeight(45);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setGraphic(image);
        alert.setHeaderText("Are you sure you want to Return This Item");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            try {
                finalReturn();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            alert.close();
        }
    }

    private void finalReturn() throws SQLException {

        String remark = remarkTF.getText();


        double refundAmount = 0;
        try {
            refundAmount = Double.parseDouble(refundL.getText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        PreparedStatement psReturnMain = null, psReturnItem = null, psUpdateStock = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            connection.setAutoCommit(false);

            String returnMainQuery = "INSERT INTO tbl_return_main (SALE_MAIN_ID, seller_id, TOTAL_REFUND_AMOUNT,invoice_number , REMARK ," +
                    " old_invoice_number)VALUES (?,?, ?, ?, ?,?)";

            psReturnMain = connection.prepareStatement(returnMainQuery, new String[]{"return_main_id"});
            psReturnMain.setInt(1, saleMainID);
            psReturnMain.setInt(2, Login.currentlyLogin_Id);
            psReturnMain.setDouble(3, refundAmount);
            psReturnMain.setString(4, getInvoiceNumber()); // invoice Num
            if (remark.isEmpty()) {
                psReturnMain.setNull(5, Types.NULL);
            } else {
                psReturnMain.setString(5, remark);
            }
            psReturnMain.setString(6, oldInvoiceNumber);


            int res = psReturnMain.executeUpdate();
            if (res > 0) {
                int return_main_id = 0;
                rs = psReturnMain.getGeneratedKeys();
                if (rs.next()) {
                    return_main_id = rs.getInt(1);
                }
                for (ReturnProductModel rp : tableView.getItems()) {
                    String quantityUnit = rp.getQuantity().split(" -")[1];
                    res = 0;
                    int returnQuantity = 0;
                    try {
                        returnQuantity = Integer.parseInt(rp.getReturnQuantity().replaceAll("[^0-9.]", ""));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    String returnItemsQuery = "INSERT INTO tbl_return_items(return_main_id, product_id, return_quantity, discount_per, rate , quantity_Unit , sale_item_id ) VALUES (?,?,?,?,?,?,?)";
                    psReturnItem = connection.prepareStatement(returnItemsQuery);
                    psReturnItem.setInt(1, return_main_id);
                    psReturnItem.setInt(2, rp.getProductID());
                    psReturnItem.setInt(3, returnQuantity);
                    psReturnItem.setDouble(4, discountPer);
                    psReturnItem.setDouble(5, rp.getRate());
                    psReturnItem.setString(6, quantityUnit);
                    psReturnItem.setInt(7, rp.getSaleItemId());


                    res = psReturnItem.executeUpdate();


                    if (res > 0) {
                        res = 0;
                        String updateQuery = "UPDATE tbl_product_stock SET quantity = quantity + ? where stock_id = ?  ";
                        PreparedStatement updatePstmt = connection.prepareStatement(updateQuery);
                        updatePstmt.setInt(1, returnQuantity);
                        updatePstmt.setInt(2, rp.getStockId());
                        res = updatePstmt.executeUpdate();

                    }
                }
            }
            if (res > 0) {
                connection.commit();
                customDialog.showAlertBox("Success", "Successfully Returned");
                resetValue();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            DBConnection.closeConnection(connection, psReturnMain, rs);
            if (null != psReturnItem) {
                psReturnItem.close();
            }
            if (null != psUpdateStock) {
                psUpdateStock.close();
            }
        }
    }


    private void calculate() {
        netReturnAmtTot = 0;
        totalReturnDisctAmount = 0;
        discountPer = 0;

        bnSubmit.setDisable(true);
        for (ReturnProductModel rp : tableView.getItems()) {
            if (rp.isReturn()) {
                bnSubmit.setDisable(false);
                int returnQuantity = 0;
                try {
                    returnQuantity = Integer.parseInt(rp.getReturnQuantity().replaceAll("[^0-9.]", ""));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                int productId = rp.getProductID();
                int stockId = rp.getStockId();
                double rate = rp.getRate();

                discountPer = (totalDiscount*100)/(netAmount+totalDiscount) ;

                System.out.println(discountPer);

                double returnAmount = rate * returnQuantity;
                double discountAmount = (returnAmount * discountPer) / 100;
                netReturnAmtTot = netReturnAmtTot + returnAmount;
                totalReturnDisctAmount = totalReturnDisctAmount + discountAmount;
            }
        }
        refundL.setText(String.valueOf(Math.round(netReturnAmtTot)));
        discountTF.setText(String.valueOf(Math.round(totalReturnDisctAmount)));
    }

    public void bnReturnHistory(ActionEvent event) {

        customDialog.showFxmlFullDialog("returnItems/ returnMain.fxml", "RETURN HISTORY");
    }

    public String getInvoiceNumber() {
        dbConnection = new DBConnection();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            ps = connection.prepareStatement("select max(RETURN_MAIN_ID) from TBL_RETURN_MAIN");
            rs = ps.executeQuery();
            String invoiceNum = null;

            if (rs.next()) {
                long id = rs.getInt(1) + 1;

                invoiceNum = String.format("%07d", id);
            }


            return "SUMA" + invoiceNum + "R";
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection(connection, ps, rs);

        }

    }
}




