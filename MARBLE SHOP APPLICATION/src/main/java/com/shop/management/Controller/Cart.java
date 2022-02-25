package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.CartModel;
import com.shop.management.Model.Customer;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
    public TableColumn<CartModel, String> colPurchasePrice;
    public TableColumn<CartModel, String> colMrp;
    public TableColumn<CartModel, String> colSellPrice;
    public TableColumn<CartModel, String> colQuantity;
    public ComboBox<String> billingTypeC;
    public Label priceL;
    public Label discountL;
    public Label taxL;
    public Label totalPayAbleL;
    public TableColumn<CartModel ,Integer> colSrNo;

    private DBConnection dbconnection;
    private Method method;
    private CustomDialog customDialog;
    private Properties properties;

    private ObservableList<CartModel> cartList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbconnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");
        setBillType();


        refresh();

        cartTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, cartModel, t1) -> {


        });

        billingTypeC.valueProperty().addListener((observableValue, s, t1) -> {
            getCart(t1);

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
        double totalPayableD = 0, amount = 0, discountPrice = 0, gstPrice = 0;

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
                    "        tp.product_name,tp.product_type,tp.category,\n" +
                    "        tp.discount_id ,tp.tax_id , tps.stock_id ,\n" +
                    "        td.discount_id , td.discount_name,td.discount,tpt.hsn_sac  ,\n" +
                    "        tpt.tax_id ,tpt.sgst,tpt.cgst,tpt.igst,tpt.\"gstName\"\n" +
                    "FROM   tbl_cart as tc\n" +
                    "           LEFT JOIN tbl_products as tp ON tc.product_id = tp.product_id\n" +
                    "           LEFT JOIN tbl_product_stock as tps ON tc.stock_id = tps.stock_id\n" +
                    "           Left JOIN tbl_discount as td  ON ( tp.discount_id = td.discount_id )\n" +
                    "           Left Join tbl_product_tax as tpt  on ( tp.tax_id = tpt.tax_id ) order by cart_id desc ";


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
                String productCategory = rs.getString("category");
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

                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                long hsn_sac = rs.getLong("hsn_sac");

                int totalGst = sgst + cgst + igst;

                int totalDiscount = rs.getInt("discount");
                String discount_name = rs.getString("discount_name");

                double price = sellingPrice * quantity;
                double disAmount = price * totalDiscount / 100;
                double gstAmount = price * totalGst / 100;
                double netAmount = 0;

                String totalDis = String.valueOf(totalDiscount);


                switch (billType) {

                    case "REGULAR" -> {

                        gstAmount = 0;
                        netAmount = (price - disAmount) + gstAmount;


                        cartList.add(new CartModel(cartId, productId, stockID, discountId, taxId, sellerId, productName, product_type,
                                productCategory, purchasePrice, productMRP, minSellPrice, sellingPrice, height, width,
                                sizeUnit, quantityUnit, totalDis, 0, quantity, productColor, discount_name,
                                disAmount, 0, netAmount, 0, sgst, cgst, igst));
                    }

                    case "GST" -> {
                        gstAmount = price * totalGst / 100;
                        netAmount = (price - disAmount) + gstAmount;

                        cartList.add(new CartModel(cartId, productId, stockID, discountId, taxId, sellerId, productName, product_type,
                                productCategory, purchasePrice, productMRP, minSellPrice, sellingPrice, height, width,
                                sizeUnit, quantityUnit, totalDis, totalGst, quantity, productColor, discount_name,
                                disAmount, hsn_sac, netAmount, gstAmount, sgst, cgst, igst));
                    }
                }


                amount = amount + price;
                discountPrice = discountPrice + disAmount;
                gstPrice = gstPrice + gstAmount;

                totalPayableD = totalPayableD + netAmount;


            }

            cartTableView.setItems(cartList);
            discountL.setText(String.valueOf(discountPrice));
            taxL.setText(String.valueOf(gstPrice));
            priceL.setText(String.valueOf(amount));
            totalPayAbleL.setText(String.valueOf(totalPayableD));

            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    cartTableView.getItems().indexOf(cellData.getValue()) + 1));
            colProduct_name.setCellValueFactory(new PropertyValueFactory<>("productName"));
            colSize.setCellValueFactory(new PropertyValueFactory<>("fullSize"));
            colType.setCellValueFactory(new PropertyValueFactory<>("type"));
            colDiscount.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
            colGst.setCellValueFactory(new PropertyValueFactory<>("totalTax"));
            colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
            colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
            colSellPrice.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("fullQuantity"));


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

                    ivDelete.setImage(method.getImage("src/main/resources/com/shop/management/img/icon/delete_ic.png"));
                    ivUpdate.setImage(method.getImage("src/main/resources/com/shop/management/img/icon/edit_ic.png"));

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
        customColumn(colSize);
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
            text.setStyle("-fx-font-size: 14");
            cell.setGraphic(text);
            text.setStyle("-fx-text-alignment: CENTER ;");
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    public void clear(ActionEvent event) {

        if (cartList.size() < 1){
            customDialog.showAlertBox("Failed...","Item Not Available Please add At least 1 Item");
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

            deleteAll();

        } else {
            alert.close();
        }

    }

    private void deleteAll() {

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
            if (res[0] > 0){

                if (res[1] > 0){

                    if (null != cartList){
                        cartList.clear();
                    }

                    refresh();
                }

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

        if (cartList.size() < 1){
            customDialog.showAlertBox("Failed...","Item Not Available Please add At least 1 Item");
            return;
        }

        customDialog.showFxmlDialog("sellItems/customerDetails.fxml", "ENTER CUSTOMER DETAILS");
        Customer customer = null;

        try {
            customer = (Customer) Main.primaryStage.getUserData();
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
        }

        if (null == customer) {

            System.out.println("customer not found");
            return;
        }

        // "REGULAR", "GST", "PROPOSAL" billType

        int customerId = customer.getCustomerId();
        String billingType = billingTypeC.getSelectionModel().getSelectedItem();

        long invoiceNumber = 1233;


        switch (billingType) {

            case "PROPOSAL" -> {

            }
            case "GST", "REGULAR" -> {

                try {
                    addSaleItem(customer, invoiceNumber, billingType);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void addSaleItem(Customer customer, long invoiceNumber, String billingType) throws SQLException {

        ObservableList<CartModel> items = cartTableView.getItems();
        Connection connection = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        int res = 0;

        connection = dbconnection.getConnection();
        connection.setAutoCommit(false);

        if (null == connection) {
            System.out.println("Cart : Connection Failed");
            return;
        }

        try {
            for (CartModel model : items) {

                res = 0;
                String query = "INSERT INTO tbl_saleItems (CUSTOMER_ID, PRODUCT_ID, STOCK_ID, SELLER_ID,\n" +
                        "                             INVOICE_NUMBER, PRODUCT_NAME, PRODUCT_COLOR, PRODUCT_CATEGORY,\n" +
                        "                             PRODUCT_TYPE, PRODUCT_SIZE, PURCHASE_PRICE, PRODUCT_MRP, SELL_PRICE,\n" +
                        "                             PRODUCT_QUANTITY, DISCOUNT_NAME, DISCOUNT_AMOUNT, BILL_TYPE, HSN_SAC,\n" +
                        "                             PRODUCT_TAX,TAX_AMOUNT ,NET_AMOUNT , igst , sgst ,cgst)\n" +
                        "\n" +
                        " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                ps = connection.prepareStatement(query);
                ps.setInt(1, customer.getCustomerId());
                ps.setInt(2, model.getProductId());
                ps.setInt(3, model.getProductStockID());
                ps.setInt(4, model.getSellerId());
                ps.setLong(5, invoiceNumber);
                ps.setString(6, model.getProductName());
                ps.setString(7, model.getProductColor());// color
                ps.setString(8, model.getCategory());
                ps.setString(9, model.getType());
                ps.setString(10, model.getFullSize());
                ps.setDouble(11, model.getPurchasePrice());
                ps.setDouble(12, model.getProductMRP());
                ps.setDouble(13, model.getSellingPrice());
                ps.setString(14, model.getFullQuantity());

                // ps.setDate(1,new java.sql.Date(new java.util.Date().getTime()));

                if (null != model.getDiscountName()) {
                    ps.setString(15, model.getDiscountName());
                } else {
                    ps.setNull(15, Types.NULL);
                }

                ps.setDouble(16, model.getDiscountAmount());
                ps.setString(17, billingType);


                ps.setLong(18, model.getHsn_sac());
                ps.setDouble(19, model.getTotalTax());
                ps.setDouble(20, model.getGstAmount());

                ps.setDouble(21, model.getNetAmount());
                ps.setInt(22, model.getIgst());
                ps.setInt(23, model.getSgst());
                ps.setInt(24, model.getCsgt());


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
                customDialog.showAlertBox("Seccess", "Successfully to Sale !");
            } else {
            }
        } catch (SQLException e) {
            customDialog.showAlertBox("ERROR", "Failed to Sale !");
            connection.rollback();
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(connection, ps, null);
        }
        //}

        deleteAll();
        Stage stage = CustomDialog.stage;

        if (stage.isShowing()) {
            stage.close();
        }

    }
}
