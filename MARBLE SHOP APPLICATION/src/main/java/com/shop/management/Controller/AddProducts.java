package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.ProductSize;
import com.shop.management.Model.ProductSize;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class AddProducts implements Initializable {
    public TableView<ProductSize> sizeTableView;
    public TextField productName;
    public TextField productPurchasePrice;
    public TextField productMrp;
    public TextField productMinSell;
    public ComboBox<String> productDiscount;
    public ComboBox<String> productColor;
    public ComboBox<String> productType;
    public ComboBox<String> productCategory;
    public TextField productHeight;
    public TextField productWidth;
    public ComboBox<String> productSizeUnit;
    public TextField productQuantity;
    public ComboBox<String> productQuantityUnit;
    public Button bnAddSize;
    public TableColumn<ProductSize, String> col_Height;
    public TableColumn<ProductSize, String> col_Width;
    public TableColumn<ProductSize, String> col_Quantity;
    public TableColumn<ProductSize, String> col_action;
    public TableColumn<ProductSize, String> col_SizeUit;
    public TableColumn<ProductSize, String> col_QuantityUnit;

    public TextField productDescription;
    public ImageView productIMG;
    public Button bn_UploadImage;
    public Button bnSubmit;

    double tableViewSize = 70;


    private Method method;
    private CustomDialog customDialog;

    ObservableList<ProductSize> sizeList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeTableView.setPlaceholder(new Label("Size Not Available"));
        method = new Method();
        customDialog = new CustomDialog();

        setComboBoxData();
    }

    private void setComboBoxData() {
        productColor.setItems(method.getProductColor());
        productType.setItems(method.getProductType());
        productCategory.setItems(method.getProductCategory());
        productSizeUnit.setItems(method.getSizeUnit());
        productQuantityUnit.setItems(method.getSizeQuantityUnit());

        productDiscount.setEditable(true);

        productDiscount.setItems(method.getProductColor());

        productDiscount.valueProperty().addListener((v, oldValue, newValue) -> {
            System.out.println(newValue);
        });

    }

    public void uploadImage_bn(ActionEvent event) {



    }

    public void submit_bn(ActionEvent event) {

        // textField
        String prodName =productName.getText();
        String purchasePrice = productPurchasePrice.getText();
        String prodMrp = productMrp.getText();
        String minSellPrice = productMinSell.getText();
        String prodDescription = productDescription.getText();

        if (prodName.isEmpty()){
            method.show_popup("ENTER PRODUCT NAME ",productName);
            return;
        }else if (purchasePrice.isEmpty()){
            method.show_popup("ENTER PURCHASE PRICE ",productPurchasePrice);
            return;
        }else if (prodMrp.isEmpty()){
            method.show_popup("ENTER PRODUCT MRP ",productMrp);
            return;
        }else if (minSellPrice.isEmpty()){
            method.show_popup("ENTER MIN SELLING PRICE ",productMinSell);
            return;
        }else if (null == productDiscount.getValue()){
            method.show_popup("CHOOSE PRODUCT DISCOUNT ",productDiscount);
            return;
        }else if (null == productColor.getValue()){
            method.show_popup("CHOOSE PRODUCT COLOR ",productColor);
            return;
        }else if (null == productType.getValue()){
            method.show_popup("CHOOSE PRODUCT TYPE ",productType);
            return;
        }else if (null == productCategory.getValue()){
            method.show_popup("CHOOSE PRODUCT CATEGORY",productCategory);
            return;
        }else if (null == sizeTableView){
            method.show_popup("ADD AT LEAST 1 SIZE",bnAddSize);
            return;
        }

        customDialog.showAlertBox("","all right");

        // combobox
        String discount = productDiscount.getValue(); // get discount id
        String prodColor = productColor.getValue();
        String prodType = productType.getValue();
        String prodCategory = productCategory.getValue();

        ObservableList<ProductSize> p = sizeTableView.getItems();

       for(ProductSize ps : p){ System.out.println( ps.getHeight()); }
    }

    public void bnAddSize(ActionEvent event) {

        String heightS = productHeight.getText();
        String widthS = productWidth.getText();
        String quantityS = productQuantity.getText();

        if (heightS.isEmpty()) {
            method.show_popup("ENTER PRODUCT HEIGHT", productHeight);
            return;
        } else if (widthS.isEmpty()) {
            method.show_popup("ENTER PRODUCT WIDTH", productWidth);
            return;
        } else if (null == productSizeUnit.getValue()) {
            method.show_popup("CHOOSE SIZE UNIT", productSizeUnit);
            return;

        } else if (quantityS.isEmpty()) {

            method.show_popup("ENTER PRODUCT QUANTITY", productQuantity);
            return;
        } else if (null == productQuantityUnit.getValue()) {

            method.show_popup("CHOOSE QUANTITY UNIT", productQuantityUnit);
            return;
        }

        double height = 0;
        double width = 0;
        long quantity = 0;

        try {
            height = Double.parseDouble(heightS.replaceAll("[^0-9.]", ""));
            width = Double.parseDouble(widthS.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID PRODUCT SIZE", "ENTER VALID HEIGHT AND WIDTH ");
            e.printStackTrace();
        }

        try {
            quantity = Long.parseLong(quantityS.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            customDialog.showAlertBox("INVALID QUANTITY", "ENTER VALID QUANTITY");
            e.printStackTrace();
        }

        String sizeUnit = productSizeUnit.getValue();
        String quantityUnit = productQuantityUnit.getValue();


        ProductSize productSize = new ProductSize(width, height, quantity, sizeUnit, quantityUnit);
        sizeList.add(productSize);

        sizeTableView.setItems(sizeList);

        tableViewSize = tableViewSize+20;
        sizeTableView.setMinHeight(tableViewSize);

        col_Height.setCellValueFactory(new PropertyValueFactory<>("height"));
        col_Width.setCellValueFactory(new PropertyValueFactory<>("width"));
        col_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_SizeUit.setCellValueFactory(new PropertyValueFactory<>("sizeUnit"));
        col_QuantityUnit.setCellValueFactory(new PropertyValueFactory<>("quantityUnit"));


        Callback<TableColumn<ProductSize, String>, TableCell<ProductSize, String>>
                cellFactory = (TableColumn<ProductSize, String> param) -> {

            final TableCell<ProductSize, String> cell = new TableCell<ProductSize, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        FileInputStream input_delete;
                        File  delete_file;
                        ImageView  iv_delete;
                        Image  image_delete = null;

                        String path = "src/main/resources/com/shop/management/img/icon/";

                        try {
                            delete_file = new File(path + "delete_ic.png");

                            input_delete = new FileInputStream(delete_file.getPath());

                            image_delete = new Image(input_delete);


                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                        iv_delete = new ImageView(image_delete);
                        iv_delete.setFitHeight(17);
                        iv_delete.setFitWidth(17);
                        iv_delete.setPreserveRatio(true);


                        iv_delete.setStyle(
                                " -fx-cursor: hand ;"
                                        + "-glyph-size:28px;"
                                        + "-fx-fill:#ff0000;"
                        );

                        iv_delete.setOnMouseClicked((MouseEvent event) -> {


                            ProductSize ps1 = sizeTableView.getSelectionModel().getSelectedItem();

                            sizeTableView.getItems().remove(ps1);

                            tableViewSize = tableViewSize-20;


                            sizeTableView.setMinHeight(tableViewSize);

                        });

                        HBox managebtn = new HBox (iv_delete);

                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(iv_delete, new Insets(2, 3, 0, 20));

                        setGraphic(managebtn);

                        setText(null);

                    }
                }

            };

            return cell;
        };

        col_action.setCellFactory(cellFactory);
    }
}
