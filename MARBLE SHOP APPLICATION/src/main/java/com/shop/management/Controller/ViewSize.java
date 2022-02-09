package com.shop.management.Controller;

import com.shop.management.Model.Products;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewSize implements Initializable {

    public TableColumn<Products, String> colSize;
    public TableColumn<Products, String> colPurchasePrice;
    public TableColumn<Products, String> colMrp;
    public TableColumn<Products, String> colMinSellPrice;
    public TableColumn<Products, String> colQuantity;
    public TableColumn<Products, String> colQuantityUnit;
    public TableView tableView;
    public TableColumn colAction;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
        colMinSellPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQuantityUnit.setCellValueFactory(new PropertyValueFactory<>("quantityUnit"));  colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
        colMinSellPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        colQuantityUnit.setCellValueFactory(new PropertyValueFactory<>("quantityUnit"));
    }
}
