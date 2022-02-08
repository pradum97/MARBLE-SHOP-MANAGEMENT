package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.*;
import com.shop.management.Model.*;
import com.shop.management.util.DBConnection;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class AllProducts implements Initializable {

    public TableColumn<Products, String> colColor;
    public TableColumn<Products, String> col_IMG;
    public TableColumn<Products, String> colProductName;
    public TableColumn<Products, String> colType;
    public TableColumn<Products, String> colCategory;
    public TableColumn<Products, String> colDiscount;
    public TableColumn<Products, String> colTax;
    public TableColumn<Products, String> colSize;
    public TableColumn<Products, String> colPurchasePrice;
    public TableColumn<Products, String> colMrp;
    public TableColumn<Products, String> colMinSellPrice;
    public TableColumn<Products, String> colQuantity;
    public TableColumn<Products, String> colQuantityUnit;
    public TableColumn<Products, String> colDescription;
    public TableColumn<Products, String> colProduct;
    public TableView<Products> tableView;

    DBConnection dbconnection;
    Method method;
    CustomDialog customDialog;
    Properties properties;

    ObservableList<Products> productsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbconnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");
        getProduct();
    }

    private void getProduct() {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String query = "SELECT * FROM TBL_PRODUCTS";

            connection = dbconnection.getConnection();

            if (null == connection) {
                System.out.println("MyProduct : Connection Failed");
                return;
            }
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {

                int productID = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                String productDescription = rs.getString("product_description");
                String productColor = rs.getString("product_color");
                String productType = rs.getString("product_type");
                String productCategory = rs.getString("category");
                int discountID = rs.getInt("discount_id");
                int taxID = rs.getInt("tax_id");

                Discount discounts = new GetDiscount().get(discountID);
                TAX gst = new GetTax().getGst(taxID);
                Stock stock = new GetStockData().getStock(productID);

                int discount = 0 , tax = 0;
                if (null != discounts){
                    discount = discounts.getDiscount();
                }

                if ( null != gst){
                    tax  = gst.getSgst()+ gst.getCgst()+gst.getIgst();
                }

                if (null != stock){

                    productsList.add(new Products(stock.getStockID(),productID, stock.getPurchasePrice(), stock.getProductMRP(),
                            stock.getMinSellingPrice(),stock.getHeight(),stock.getWidth(),stock.getSizeUnit(),stock.getQuantityUnit(),stock.getQuantity(),productID,productName,productDescription,productColor,productType,productCategory,discount,tax,null));
                }

            }

            tableView.setItems(productsList);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            CloseConnection.closeConnection(connection,ps,rs);

        }

        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("tax"));

        colSize.setCellValueFactory(new PropertyValueFactory<>("height"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
        colMinSellPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQuantityUnit.setCellValueFactory(new PropertyValueFactory<>("quantityUnit"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));

        Callback<TableColumn<TAX, String>, TableCell<TAX, String>>
                cellFactory = (TableColumn<TAX, String> param) -> {

            final TableCell<TAX, String> cell = new TableCell<TAX, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FileInputStream input_edit, input_delete;
                        File edit_file, delete_file;
                        ImageView iv_edit , iv_delete;
                        Image image_edit = null, image_delete = null;

                        String path = "src/main/resources/com/shop/management/img/icon/";

                        try {
                            edit_file = new File(path + "edit_ic.png");
                            delete_file = new File(path + "delete_ic.png");

                            input_edit = new FileInputStream(edit_file.getPath());
                            input_delete = new FileInputStream(delete_file.getPath());

                            image_edit = new Image(input_edit);
                            image_delete = new Image(input_delete);


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        iv_edit = new ImageView(image_edit);
                        iv_edit.setFitHeight(22);
                        iv_edit.setFitHeight(22);
                        iv_edit.setPreserveRatio(true);

                        iv_delete = new ImageView(image_delete);
                        iv_delete.setFitHeight(17);
                        iv_delete.setFitWidth(17);
                        iv_delete.setPreserveRatio(true);

                        iv_edit.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#c506fa;"
                        );

                        iv_delete.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff0000;"
                        );
                        iv_edit.setOnMouseClicked((MouseEvent event) -> {

                            Products edit_selection = tableView.
                                    getSelectionModel().getSelectedItem();

                            if (null == edit_selection) {
                                method.show_popup("Please Select", tableView);
                                return;
                            }

                          /*  Main.primaryStage.setUserData(edit_selection);

                            customDialog.showFxmlDialog("setting/update/gstUpdate.fxml", "GST UPDATE");
                            setGstTableData();*/

                        });

                        iv_delete.setOnMouseClicked((MouseEvent event) -> {


                            Products products = tableView.
                                    getSelectionModel().getSelectedItem();

                            if (null == products) {
                                method.show_popup("Please Select", tableView);
                                return;
                            }


                            deleteProduct(products);

                        });

                        HBox managebtn = new HBox(iv_edit, iv_delete);

                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(iv_edit, new Insets(2, 2, 0, 3));
                        HBox.setMargin(iv_delete, new Insets(2, 3, 0, 20));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };

        customColumn(colProductName);
        customColumn(colDescription);
    }

    private void deleteProduct(Products products) {

       /* Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This DISCOUNT ( "+discount.getDiscount()+" )");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            Connection con = null;
            PreparedStatement ps = null;

            try{

                con =  dbConnection.getConnection();

                if (null == con){
                    return;
                }

                ps = con.prepareStatement("DELETE FROM TBL_DISCOUNT WHERE DISCOUNT_ID = ?");
                ps.setInt(1,discount.getDiscount_id());

                int res = ps.executeUpdate();

                if (res > 0){
                    setDiscountData();
                    alert.close();
                }

            } catch (SQLException e) {
                customDialog.showAlertBox("ERROR","You cannot remove this Discount because this Discount has been used in your products.");
                e.printStackTrace();
            }finally {

                try{

                    if (null != con){
                        con.close();
                    }
                    if (null != ps){
                        ps.close();
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            alert.close();
        }*/
    }

    private void customColumn(TableColumn<Products, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<Products, String> cell = new TableCell<>();
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



}
