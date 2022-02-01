package com.shop.management.Controller;

import com.shop.management.Method.Method;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProducts implements Initializable {
    public TableView  sizeTableView;
    public TextField productName;
    public TextField productPurchasePrice;
    public TextField productMrp;
    public TextField productMinSell;
    public ComboBox <String> productDiscount;
    public ComboBox <String> productColor;
    public ComboBox <String> productType;
    public ComboBox <String> productCategory;
    public TextField  productHeight;
    public TextField productWidth;
    public ComboBox <String> productSizeUnit;
    public TextField productQuantity;
    public ComboBox <String> productQuantityUnit;
    public Button bnAddSize;
    public TableColumn col_Height;
    public TableColumn col_Width;
    public TableColumn col_Quantity;
    public TableColumn col_action;
    public TextField productDescription;
    public ImageView productIMG;
    public Button bn_UploadImage;
    public Button bnSubmit;

    private Method method;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeTableView.setPlaceholder(new Label("Size Not Available"));
        method = new Method();

        setComboBoxData();
    }

    private void setComboBoxData() {
        productColor.setItems(method.getProductColor());
        productType.setItems(method.getProductType());
        productCategory.setItems(method.getProductCategory());
        productSizeUnit.setItems(method.getSizeUnit());
        productQuantityUnit.setItems(method.getSizeQuantityUnit());


    }

    public void uploadImage_bn(ActionEvent event) {


    }

    public void submit_bn(ActionEvent event) {
    }


}
