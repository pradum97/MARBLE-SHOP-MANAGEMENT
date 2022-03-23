package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.GenerateInvoice;
import com.shop.management.Method.GenerateInvoiceNumber;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.CartModel;
import com.shop.management.Model.Customer;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class Cart implements Initializable {
    public TableView<CartModel> cartTableView;


    public TableColumn<CartModel, String> colProduct_name;
    public TableColumn<CartModel, String> colSize;
    public TableColumn<CartModel, String> colType;
    public TableColumn<CartModel, String> colDiscount;
    public TableColumn<CartModel, String> colGst;
    public TableColumn<CartModel, String> colAction;
    public TableColumn<CartModel, String> colMrp;
    public TableColumn<CartModel, String> colSellPrice;
    public TableColumn<CartModel, String> colQuantity;
    public TableColumn<CartModel, String> colAmount;
    public ComboBox<String> billingTypeC;
    public Label subTotalL;
    public Label discountL;
    public Label taxL, taxTitleL;
    public Label totalPayAbleL;
    public TableColumn<CartModel, Integer> colSrNo;
    public TextField addDiscTF;
    public TextField duesAmountTF;
    public TextField receivedAmountTF;
    public ComboBox<String> paymentModeC;
    public Label taxableValueL;
    private DBConnection dbconnection;
    private Method method;
    private CustomDialog customDialog;
    private Properties properties;

    private static String invoiceNumber;

    private ObservableList<CartModel> cartList = FXCollections.observableArrayList();

    double totalPayableD = 0;
    double subTotAmount = 0;
    double discountPrice = 0;
    double totGstPer = 0;
    double gstPrice = 0;
    double totTaxable = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbconnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = new PropertiesLoader().load("query.properties");
        invoiceNumber = new GenerateInvoiceNumber().generate();

        if (null == invoiceNumber) {
            customDialog.showAlertBox("Failed", "Invoice Number Not Found");
            return;
        }
        setBillType();
        refresh();
        duesAmountTF.setEditable(false);

        billingTypeC.valueProperty().addListener((observableValue, s, t1) -> {
            getCart(t1);
        });
        comboBoxConfig();
        textFieldConfig();


    }

    private void textFieldConfig() {

        receivedAmountTF.textProperty().addListener((observableValue, s, t1) -> {
            double totalPayable = Double.parseDouble(totalPayAbleL.getText());
            double receivedAmount = 0;
            try {
                receivedAmount = Double.parseDouble(t1);
            } catch (NumberFormatException ignored) {
            }

            if (receivedAmount <= totalPayable) {

                double totDues = totalPayable - receivedAmount;

                duesAmountTF.setText(String.valueOf(Math.round(totDues)));
            } else {
                receivedAmountTF.setText("");
                duesAmountTF.setText("");
                method.show_popup("YOUR INVOICE VALUE IS : " + totalPayable, receivedAmountTF);
            }

        });
    }

    private void comboBoxConfig() {

        paymentModeC.setItems(new StaticData().getPaymentMode());
        paymentModeC.getSelectionModel().selectFirst();

        billingTypeC.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });

        billingTypeC.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ListCell<String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            setAlignment(Pos.CENTER);
                            setPadding(new Insets(3, 3, 3, 0));
                        }
                    }
                };
            }
        });

        paymentModeC.setButtonCell(new ListCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setAlignment(Pos.CENTER);
                    Insets old = getPadding();
                    setPadding(new Insets(old.getTop(), 0, old.getBottom(), 0));
                }
            }
        });

        paymentModeC.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                return new ListCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                            setAlignment(Pos.CENTER);
                            setPadding(new Insets(3, 3, 3, 0));
                        }
                    }
                };
            }
        });
    }

    private void setBillType() {

        billingTypeC.setItems(new StaticData().getBillingType());
        billingTypeC.getSelectionModel().selectFirst();
    }

    private void getCart(String billType) {
        if (null != cartList) {
            cartList.clear();
        }

        totTaxable = 0;
        totalPayableD = 0;
        subTotAmount = 0;
        discountPrice = 0;
        gstPrice = 0;
        receivedAmountTF.setText("");
        duesAmountTF.setText("");
        addDiscTF.setText(String.valueOf(0));

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = dbconnection.getConnection();

            if (null == connection) {

                System.out.println("connection Failed");
                return;
            }

            String query = "SELECT  tps.purchase_price ,tps.product_mrp , tps.min_sellingprice ,\n" +
                    "        tc.sellprice , tps.height , tps.width,tps.size_unit,\n" +
                    "        tc.quantity_unit, tc.quantity,tp.product_id , tp.product_color , tc.cart_id , tc.seller_id ,\n" +
                    "        tp.product_name,tp.product_type,tcategory.category_name , tcategory.category_id,\n" +
                    "        tp.discount_id ,tp.tax_id , tps.stock_id ,\n" +
                    "        td.discount_id , td.discount_name,td.discount,tpt.hsn_sac  ,\n" +
                    "        tpt.tax_id ,tpt.sgst,tpt.cgst,tpt.igst,tpt.gstName,(tc.sellprice * tc.quantity)  amount_asPer_mrp,\n" +
                    "        ((tc.sellprice * tc.quantity)*td.discount/100)as discountAmount,\n" +
                    "       ((((tc.sellprice * tc.quantity)-((tc.sellprice * tc.quantity)*td.discount/100))*100)/(100+(tpt.sgst+tpt.cgst+tpt.igst))) as taxable,\n" +
                    "       (((((tc.sellprice * tc.quantity)-((tc.sellprice * tc.quantity)*td.discount/100))*100)/(100+(tpt.sgst+tpt.cgst+tpt.igst))*(tpt.sgst+tpt.cgst+tpt.igst))/100) as gstAmount\n" +
                    "FROM   tbl_cart as tc\n" +
                    "           LEFT JOIN tbl_products as tp ON (tc.product_id = tp.product_id)\n" +
                    "           LEFT JOIN tbl_product_stock as tps ON tc.stock_id = tps.stock_id\n" +
                    "           Left JOIN tbl_discount as td  ON ( tp.discount_id = td.discount_id )\n" +
                    "           Left Join tbl_product_tax as tpt  on ( tp.tax_id = tpt.tax_id )\n" +
                    "           LEFT JOIN tbl_category as tcategory ON (tp.category_id = tcategory.category_id) order by cart_id ASC";


            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                int cartId = rs.getInt("cart_id");
                int productId = rs.getInt("product_id");
                int discountId = rs.getInt("discount_id");
                int stockID = rs.getInt("stock_id");
                int taxId = rs.getInt("tax_id");
                int sellerId = rs.getInt("seller_id");

                String productName = rs.getString("product_name");
                String product_type = rs.getString("product_type");
                String productCategory = rs.getString("category_name");
                String productColor = rs.getString("product_color");

                double purchasePrice = rs.getDouble("purchase_price");
                double productMRP = rs.getDouble("product_mrp");
                double minSellPrice = rs.getDouble("min_sellingprice");
                double sellingPrice = rs.getDouble("sellprice");
                double height = rs.getDouble("height");
                double width = rs.getDouble("width");

                String sizeUnit = rs.getString("size_unit");
                String quantityUnit = rs.getString("quantity_unit");

                int quantity = rs.getInt("quantity");

                double sgst = rs.getInt("sgst");
                double cgst = rs.getInt("cgst");
                double igst = rs.getInt("igst");
                long hsn_sac = rs.getLong("hsn_sac");

                double totalGstPer = sgst + cgst + igst;

                double discountPer = rs.getInt("discount");
                String discount_name = rs.getString("discount_name");

                double amountAsPerMrp = Math.round(rs.getDouble("amount_asPer_mrp"));
                double disAmount = Math.round(rs.getDouble("discountAmount"));

                double taxable = Math.round(rs.getDouble("taxable"));
                double gstAmount = Math.round(rs.getDouble("gstAmount"));

                String totalDisStr = disAmount + "( " + discountPer + "%)";
                String taxStr = gstAmount + "( " + totalGstPer + "%)";

                double netAmount = (amountAsPerMrp - disAmount);

                cartList.add(new CartModel(cartId, productId, stockID, discountId, taxId, sellerId, productName, product_type,
                        productCategory, purchasePrice, productMRP, minSellPrice, sellingPrice, height, width,
                        sizeUnit, quantityUnit, totalDisStr, totalGstPer, quantity, productColor, discount_name,
                        disAmount, hsn_sac, Math.round(netAmount), gstAmount, sgst, cgst, igst, taxStr, discountPer));

                subTotAmount += amountAsPerMrp;
                discountPrice += disAmount;
                gstPrice += gstAmount;
                totalPayableD += netAmount;
                totTaxable += taxable;

            }
            subTotalL.setText(String.valueOf(Math.round(subTotAmount)));
            discountL.setText(String.valueOf(Math.round(discountPrice)));
            taxL.setText(String.valueOf(Math.round(gstPrice)));
            totalPayAbleL.setText(String.valueOf(Math.ceil(totalPayableD)));
            taxableValueL.setText(String.valueOf(totTaxable));


            addDiscTF.textProperty().addListener((observableValue, s, t1) -> {

                receivedAmountTF.setText("");
                duesAmountTF.setText("");
                double inputDiscount = 0;
                try {
                    inputDiscount = Double.parseDouble(t1);
                } catch (NumberFormatException ignored) {
                }

                if (inputDiscount < totalPayableD) {
                    double totPayable = totalPayableD - inputDiscount;
                    totalPayAbleL.setText(String.valueOf((Math.ceil(totPayable))));
                } else {
                    inputDiscount = 0;
                    totalPayAbleL.setText(String.valueOf(Math.ceil(totalPayableD)));
                    addDiscTF.setText(String.valueOf(0));
                    method.show_popup("Discount Amount Not Greater Then " + totalPayableD, addDiscTF);
                }
            });
            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    cartTableView.getItems().indexOf(cellData.getValue()) + 1));
            colAmount.setCellValueFactory(new PropertyValueFactory<>("netAmount"));
            colProduct_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colSize.setCellValueFactory(new PropertyValueFactory<>("fullSize"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colDiscount.setCellValueFactory(new PropertyValueFactory<>("totalDiscountStr"));
            colGst.setCellValueFactory(new PropertyValueFactory<>("totalTaxStr"));
            colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
            colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("fullQuantity"));

            cartTableView.setItems(cartList);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

        Callback<TableColumn<CartModel, String>, TableCell<CartModel, String>>
                cellFactory = (TableColumn<CartModel, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {

                    ImageView ivDelete = new ImageView();
                    ivDelete.setFitHeight(18);
                    ivDelete.setFitWidth(18);
                    ivDelete.setPreserveRatio(true);
                    ivDelete.setSmooth(true);

                    ImageView ivUpdate = new ImageView();
                    ivUpdate.setFitHeight(20);
                    ivUpdate.setFitWidth(20);
                    ivUpdate.setPreserveRatio(true);
                    ivUpdate.setSmooth(true);

                    ivDelete.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                            CartModel cartModel = cartTableView.getSelectionModel().getSelectedItem();

                            if (null == cartModel) {
                                return;
                            }

                            deleteCartItem(cartModel);
                        }
                    });

                    ivUpdate.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {

                            CartModel cartModel = cartTableView.getSelectionModel().getSelectedItem();

                            if (null == cartModel) {
                                System.out.println("not found");
                                return;
                            }

                            Main.primaryStage.setUserData(cartModel);
                            customDialog.showFxmlDialog("sellitems/cartQuantityUpdate.fxml", "");
                            refresh();
                            cartTableView.refresh();
                        }
                    });

                    ImageLoader loader = new ImageLoader();

                    ivDelete.setImage(loader.load("img/icon/delete_ic.png"));
                    ivUpdate.setImage(loader.load("img/icon/edit_ic.png"));

                    HBox managebtn = new HBox(ivUpdate, ivDelete);
                    managebtn.setStyle("-fx-alignment:center; -fx-cursor: hand");
                    HBox.setMargin(ivUpdate, new Insets(3, 10, 3, 5));
                    HBox.setMargin(ivDelete, new Insets(3, 5, 3, 20));
                    setGraphic(managebtn);
                    setText(null);
                }
            }

        };

        colAction.setCellFactory(cellFactory);
        customColumn(colProduct_name);

    }

    private void deleteCartItem(CartModel cartModel) {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Item ( " + cartModel.getFullSize() + " )");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            Connection con = null;
            PreparedStatement ps = null;

            try {

                con = dbconnection.getConnection();

                if (null == con) {
                    return;
                }

                ps = con.prepareStatement("DELETE FROM tbl_cart WHERE  cart_id =  ?");
                ps.setInt(1, cartModel.getCartId());

                int res = ps.executeUpdate();

                if (res > 0) {
                    refresh();
                }
            } catch (SQLException e) {
                customDialog.showAlertBox("ERROR", "You cannot remove this Discount because this Discount has been used in your products.");
                e.printStackTrace();
            } finally {

                DBConnection.closeConnection(con, ps, null);
            }

        } else {
            alert.close();
        }
    }

    private void customColumn(TableColumn<CartModel, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<CartModel, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            cell.setGraphic(text);
            return cell;
        });
    }

    public void clear(ActionEvent event) {

        if (cartList.size() < 1) {
            customDialog.showAlertBox("NOT AVAILABLE", "ITEM NOT AVAILABLE PLEASE ADD AT LEAST ONE ITEM");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Remove All Item From Cart");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {

            deleteAll(event);

        } else {
            alert.close();
        }

    }

    private void deleteAll(ActionEvent event) {

        Connection con = null;
        Statement ps = null;


        String deleteQuery = "DELETE FROM tbl_cart";
        String sequenceOrder = "ALTER SEQUENCE tbl_cart_cart_id_seq RESTART WITH 1;";

        try {

            con = dbconnection.getConnection();

            if (null == con) {
                return;
            }
            ps = con.createStatement();
            ps.addBatch(deleteQuery);
            ps.addBatch(sequenceOrder);

            int[] res = ps.executeBatch();
            if (res[0] > 0) {

                if (res[1] > 0) {

                    if (null != cartList) {
                        cartList.clear();
                    }
                    refresh();
                }

                if (null != cartList) {
                    cartList.clear();
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                if (stage.isShowing()) {
                    stage.close();
                }
                refresh();
            }

        } catch (SQLException e) {
            customDialog.showAlertBox("ERROR", "Failed to Clear Cart !");
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(con, null, null);
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            refresh();
        }
    }

    private void refresh() {
        String billingType = billingTypeC.getSelectionModel().getSelectedItem();
        getCart(billingType);

    }

    public void sellNow_bn(ActionEvent event) {

        if (cartList.size() < 1) {
            customDialog.showAlertBox("NOT AVAILABLE", "ITEM NOT AVAILABLE PLEASE ADD AT LEAST ONE ITEM");
            return;
        }
        String receivedAmountS = receivedAmountTF.getText();

        if (receivedAmountS.isEmpty()) {
            method.show_popup("Please Enter Received Amount", receivedAmountTF);
            return;
        }
        double receivedAmountD = 0;
        try {
            receivedAmountD = Double.parseDouble(receivedAmountS);
        } catch (NumberFormatException e) {
            method.show_popup("Enter Valid Amount", receivedAmountTF);
            return;
        }

        if (receivedAmountD < 0) {
            method.show_popup("Enter more than 0", receivedAmountTF);
            return;
        } else if (addDiscTF.getText().isEmpty()) {
            addDiscTF.setText(String.valueOf(0));
        }

        customDialog.showFxmlDialog2("sellItems/customerDetails.fxml", "ENTER CUSTOMER DETAILS");
        Customer customer = null;

        try {
            customer = (Customer) Main.primaryStage.getUserData();
        } catch (ClassCastException ignored) {
        }

        if (null == customer) {
            System.out.println("customer not found");
            return;
        }
        // "REGULAR", "GST", "PROPOSAL" billType

        int customerId = customer.getCustomerId();
        String billingType = billingTypeC.getSelectionModel().getSelectedItem();
        switch (billingType) {
            case "PROPOSAL" -> {

            }
            case "GST", "REGULAR" -> {

                try {
                    addSaleItem(customer, billingType, receivedAmountD, event);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void addSaleItem(Customer customer, String billingType,
                             double receivedAmountD, ActionEvent event) throws SQLException {

        double additionalDisc = 0;
        try {
            additionalDisc = Double.parseDouble(addDiscTF.getText());
        } catch (NumberFormatException ignored) {
        }

        String paytmModeS = paymentModeC.getSelectionModel().getSelectedItem();
        double duesAmtD = Double.parseDouble(duesAmountTF.getText());
        double totalDisAmtD = discountPrice + additionalDisc;
        double totalTaxAmtD = gstPrice;
        double invoiceValue = Double.parseDouble(totalPayAbleL.getText());
        long timestamp = System.currentTimeMillis();

        ObservableList<CartModel> items = cartTableView.getItems();
        Connection connection = null;
        PreparedStatement ps = null, ps1 = null, ps3 = null, psDues = null;
        ResultSet rsMain = null;

        int res = 0;

        connection = dbconnection.getConnection();
        connection.setAutoCommit(false);

        double addiDisc = 0;
        try {
            addiDisc = Double.parseDouble(addDiscTF.getText());
        } catch (NumberFormatException ignored) {
        }


        try {
            String saleMainQuery = "INSERT INTO tbl_sale_main(CUSTOMER_ID, SELLER_ID, ADDITIONAL_DISCOUNT, RECEIVED_AMOUNT," +
                    " PAYMENT_MODE, TOT_DISC_AMOUNT,\n" +
                    "                          TOT_TAX_AMOUNT, NET_AMOUNT, INVOICE_NUMBER, BILL_TYPE)\n" +
                    "                           VALUES (?,?,?,?,?,?,?,?,?,?)";

            ps3 = connection.prepareStatement(saleMainQuery, new String[]{"sale_main_id"});
            ps3.setInt(1, customer.getCustomerId());
            ps3.setInt(2, Login.currentlyLogin_Id);
            ps3.setDouble(3, addiDisc);
            ps3.setDouble(4, receivedAmountD);
            ps3.setString(5, paytmModeS);
            ps3.setDouble(6, totalDisAmtD);
            ps3.setDouble(7, totalTaxAmtD);
            ps3.setDouble(8, invoiceValue);
            ps3.setString(9, invoiceNumber);
            ps3.setString(10, billingType);

            int resMain = ps3.executeUpdate();
            if (resMain > 0) {

                rsMain = ps3.getGeneratedKeys();

                if (rsMain.next()) {

                    int sale_main_id = rsMain.getInt(1);

                    if (duesAmtD > 0) {
                        String duesQuery = "INSERT INTO tbl_dues (CUSTOMER_ID, SALE_MAIN_ID, DUES_AMOUNT," +
                                " DUES_NOTES, PAYMENT_MODE, LAST_PAYMENT,INVOICE_NUMBER)\n" +
                                "VALUES (?,?,?,?,?,?,?)";
                        psDues = connection.prepareStatement(duesQuery);
                        psDues.setInt(1, customer.getCustomerId());
                        psDues.setInt(2, sale_main_id);
                        psDues.setDouble(3, duesAmtD);
                        psDues.setNull(4, Types.NULL);
                        psDues.setString(5, paytmModeS);
                        psDues.setTimestamp(6, new Timestamp(timestamp));
                        psDues.setString(7, invoiceNumber);
                        psDues.executeUpdate();
                    }

                    for (CartModel model : items) {

                        res = 0;
                        String query = "INSERT INTO tbl_saleItems (sale_main_id, product_id, stock_id, product_name, product_color, \n" +
                                "                           product_category, product_type, product_size, purchase_price, product_mrp, sell_price,\n" +
                                "                           product_quantity, discount_name, discount_amount, hsn_sac, igst, sgst, cgst," +
                                " net_amount, tax_amount,SALE_DATE,discountPer) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                        ps = connection.prepareStatement(query);

                        ps.setInt(1, sale_main_id);
                        ps.setInt(2, model.getProductId());
                        ps.setInt(3, model.getProductStockID());
                        ps.setString(4, model.getProductName());
                        ps.setString(5, model.getProductColor());
                        ps.setString(6, model.getCategory());
                        ps.setString(7, model.getType());
                        ps.setString(8, model.getFullSize());
                        ps.setDouble(9, model.getPurchasePrice());
                        ps.setDouble(10, model.getProductMRP());
                        ps.setDouble(11, model.getSellingPrice());
                        ps.setString(12, model.getFullQuantity());

                        if (null != model.getDiscountName()) {
                            ps.setString(13, model.getDiscountName());
                        } else {
                            ps.setNull(13, Types.NULL);
                        }
                        ps.setDouble(14, model.getDiscountAmount());
                        ps.setLong(15, model.getHsn());
                        ps.setDouble(16, model.getIgst());
                        ps.setDouble(17, model.getSgst());
                        ps.setDouble(18, model.getCsgt());
                        ps.setDouble(19, model.getNetAmount());
                        ps.setDouble(20, model.getGstAmount());
                        ps.setTimestamp(21, new Timestamp(timestamp));
                        ps.setDouble(22, model.getDiscountPercentage());

                        res = ps.executeUpdate();
                        if (res > 0) {
                            ps1 = connection.prepareStatement("update tbl_product_stock set quantity = quantity-? where stock_id = ?");
                            ps1.setInt(1, model.getQuantity());
                            ps1.setInt(2, model.getProductStockID());
                            res = ps1.executeUpdate();
                        }

                    }
                    if (res > 0) {

                        connection.commit();
                        addDiscTF.setText(String.valueOf(0));
                        deleteAll(event);

                        switch (billingType){

                            case "REGULAR" ->  new GenerateInvoice().regularInvoice(sale_main_id,false , null);

                            case "GST" -> new GenerateInvoice().gstInvoice(sale_main_id,false , null);
                        }
                    }

                }
            }
        } catch (SQLException e) {
            customDialog.showAlertBox("ERROR", "Failed to Sale !");
            connection.rollback();
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, null);

            if (null != rsMain) {
                rsMain.close();
            }
            if (ps1 != null) {
                ps1.close();
            }
            if (ps3 != null) {
                ps3.close();
            }
            if (psDues != null) {
                psDues.close();
            }
        }


    }
}
