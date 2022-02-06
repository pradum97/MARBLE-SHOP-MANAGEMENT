package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Discount;
import com.shop.management.Model.ProductSize;
import com.shop.management.Model.TAX;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class AddProducts implements Initializable {
    public TableView<ProductSize> sizeTableView;
    public TextField productName;
    public TextField productPurchasePrice;
    public TextField productMrp;
    public TextField productMinSell;
    public ComboBox<Discount> productDiscount;
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
    public Button bn_UploadImage;
    public Button bnSubmit;
    public ComboBox<TAX> productTax;
    public GridPane gridImage;

    double tableViewSize = 70;
    List<String> imagePath;

    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private Properties properties;

    ObservableList<ProductSize> sizeList = FXCollections.observableArrayList();
    ObservableList<Discount> discountList = FXCollections.observableArrayList();
    ObservableList<TAX> taxList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sizeTableView.setPlaceholder(new Label("Size Not Available"));
        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        imagePath = new ArrayList<>();

        setComboBoxData();
        setDiscount();
        setTax();
    }


    private void setComboBoxData() {

        productColor.setItems(method.getProductColor());
        productType.setItems(method.getProductType());
        productCategory.setItems(method.getProductCategory());
        productSizeUnit.setItems(method.getSizeUnit());
        productQuantityUnit.setItems(method.getSizeQuantityUnit());

    }

    public void submit_bn(ActionEvent event) {

        // textField
        String prodName = productName.getText();
        String purchasePrice = productPurchasePrice.getText();
        String prodMrp = productMrp.getText();
        String minSellPrice = productMinSell.getText();
        String prodDescription = productDescription.getText();

        Discount discount = productDiscount.getSelectionModel().getSelectedItem();
        ObservableList<ProductSize> tableData = sizeTableView.getItems();

        if (prodName.isEmpty()) {
            method.show_popup("ENTER PRODUCT NAME ", productName);
            return;
        } else if (purchasePrice.isEmpty()) {
            method.show_popup("ENTER PURCHASE PRICE ", productPurchasePrice);
            return;
        } else if (prodMrp.isEmpty()) {
            method.show_popup("ENTER PRODUCT MRP ", productMrp);
            return;
        } else if (minSellPrice.isEmpty()) {
            method.show_popup("ENTER MIN SELLING PRICE ", productMinSell);
            return;
        } else if (null == productColor.getValue()) {
            method.show_popup("CHOOSE PRODUCT COLOR ", productColor);
            return;
        } else if (null == productType.getValue()) {
            method.show_popup("CHOOSE PRODUCT TYPE ", productType);
            return;
        } else if (null == productCategory.getValue()) {
            method.show_popup("CHOOSE PRODUCT CATEGORY", productCategory);
            return;
        } else if (tableData.isEmpty()) {
            method.show_popup("ADD AT LEAST 1 SIZE", bnAddSize);
            return;
        } else if (null == imagePath) {
            method.show_popup("CHOOSE PRODUCT IMAGE", bn_UploadImage);
            return;
        }

        String prodColor = productColor.getValue();
        String prodType = productType.getValue();
        String prodCategory = productCategory.getValue();

        int prodDiscount;
        int prodTax;

        prodDiscount = productDiscount.getValue().getDiscount_id();
        prodTax = productTax.getValue().getTaxID();

        System.out.println(prodDiscount);
        System.out.println(prodTax);

        String propQuery = "INSERT INTO TBL_PRODUCT_PROP (PRODUCT_COLOR_CODE, PRODUCT_COLOR_NAME, PRODUCT_TYPE, DESCRIPTION)VALUES (?,?,?,?)";
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
            return;
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

        tableViewSize = tableViewSize + 20;
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
                        File delete_file;
                        ImageView iv_delete;
                        Image image_delete = null;

                        String path = "src/main/resources/com/shop/management/img/img/";

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

                            tableViewSize = tableViewSize - 20;


                            sizeTableView.setMinHeight(tableViewSize);

                        });

                        HBox managebtn = new HBox(iv_delete);

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

    private void setDiscount() {

        if (null != discountList) {
            discountList.clear();
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

            ps = connection.prepareStatement(properties.getProperty("GET_DISCOUNT"));
            rs = ps.executeQuery();

            while (rs.next()) {

                // discount
                int discountID = rs.getInt("discount_id");
                double discount = rs.getDouble("discount");

                String discountType = rs.getString("discount_type");
                String description = rs.getString("description");
                discountList.addAll(new Discount(discountID, discount, discountType, description));

            }

            productDiscount.setItems(discountList);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            closeConnection(connection, ps, rs);
        }
    }

    private void setTax() {

        if (null != taxList) {
            taxList.clear();
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

            ps = connection.prepareStatement(properties.getProperty("GET_TAX"));
            rs = ps.executeQuery();

            while (rs.next()) {

                // tax

                int taxID = rs.getInt("tax_id");
                double sgst = rs.getDouble("sgst");
                double cgst = rs.getDouble("cgst");
                double igst = rs.getDouble("igst");
                String tax_description = rs.getString("sgst");

                taxList.add(new TAX(taxID, sgst, cgst, igst, tax_description));
            }

            productTax.setItems(taxList);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            closeConnection(connection, ps, rs);
        }
    }

    public static void closeConnection(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            if (null != connection) {
                connection.close();
            }
            if (null != ps) {
                ps.close();
            }
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage_bn(ActionEvent event) {

        switch (bn_UploadImage.getText()) {
            case "CLEAR IMAGE" -> {
                gridImage.getChildren().clear();
                bn_UploadImage.setText("UPLOAD IMAGE");
            }
            case "UPLOAD IMAGE" -> {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Image");
                fileChooser.getExtensionFilters().addAll(new FileChooser
                        .ExtensionFilter("JPG , PNG , JPEG", "*.JPG", "*.PNG", "*.JPEG"));

                List<File> f = fileChooser.showOpenMultipleDialog(Main.primaryStage);

                if (null != f) {
                    int cols = 3, colCnt = 0, rowCnt = 0;
                    gridImage.getChildren().clear();

                    for (File file : f) {

                        imagePath.add(file.getAbsolutePath());

                        ImageView img = new ImageView();
                        img.setFitWidth(80);
                        img.setFitHeight(80);
                        img.setSmooth(true);
                        img.setCache(true);
                        img.setPreserveRatio(true);

                        img.setOnMouseClicked(event1 -> {

                            Main.primaryStage.setUserData((String) file.getAbsolutePath());

                            customDialog.showFxmlDialog2("full_Image.fxml", file.getName());

                        });


                        img.setImage(method.getImage(file.getAbsolutePath()));

                        gridImage.add(img, colCnt, rowCnt);
                        colCnt++;

                        if (colCnt > cols) {
                            rowCnt++;
                            colCnt = 0;
                        }
                    }

                    bn_UploadImage.setText("CLEAR IMAGE");
                }
            }
        }


    }
}
