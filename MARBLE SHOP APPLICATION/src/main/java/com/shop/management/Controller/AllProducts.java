package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Products;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class AllProducts implements Initializable {

    public Button bnSelectAll;
    int rowsPerPage = 15;

    public TableColumn<Products, String> colColor;
    public TableColumn<Products, Integer> colSrNo;
    public TableColumn<Products, String> colProductName;
    public TableColumn<Products, String> colProductCode;
    public TableColumn<Products, String> colType;
    public TableColumn<Products, String> colCategory;
    public TableColumn<Products, String> colDiscount;
    public TableColumn<Products, String> colTax;
    public TableColumn<Products, String> colDescription;
    public TableColumn<Products, String> colAction;
    public TableColumn<Products, String> colPrice;
    public TableColumn<Products, String> colDate;
    public TableColumn<Products, String> colHsnSan;
    public TableView<Products> tableView;
    public TableColumn<Products, String> colSize;
    public HBox refresh_bn;
    public ImageView refresh_img;
    public TextField searchTf;
    public Pagination pagination;
    public Button bnDeleteAll;


    private DBConnection dbconnection;
    private Method method;
    private CustomDialog customDialog;
    private Properties properties;

    FilteredList<Products> filteredData;

    ObservableList<Products> productsList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbconnection = new DBConnection();
        customDialog = new CustomDialog();
        properties = method.getProperties("query.properties");
        setCustomImage();

        getProduct();

        tableViewMultipleSelection();


        listener();
    }

    private void tableViewMultipleSelection() {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        invisibleItem(bnSelectAll, bnDeleteAll);

        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, products, t1) -> {

            onSelectMultiple();
        });

        tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                onSelectMultiple();
            }
        });
    }

    private void onSelectMultiple() {

        ObservableList<Products> selectedItems = tableView.getSelectionModel().getSelectedItems();

        if (null == tableView.getSelectionModel().getSelectedItem() || selectedItems.size() < 2) {
            invisibleItem(bnDeleteAll, bnSelectAll);
        } else {
            if (!bnDeleteAll.isVisible()) {
                bnDeleteAll.setVisible(true);
            }
            if (!bnSelectAll.isVisible()) {
                bnSelectAll.setVisible(true);
            }
        }

        if (selectedItems.size() > 1) {
            if (productsList.size() == selectedItems.size()) {
                bnSelectAll.setText("Deselect All");
            } else {
                bnSelectAll.setText("Select All");
            }
        } else {
            invisibleItem(bnDeleteAll, bnSelectAll);
        }

    }

    private void invisibleItem(Button... bn) {

        for (Button button : bn) {
            button.setVisible(false);
            button.managedProperty().bind(button.visibleProperty());
        }
    }

    public void selectAllProduct(ActionEvent event) {

        String txt = bnSelectAll.getText();

        if (txt.equals("Select All")) {
            tableView.getSelectionModel().selectAll();
            bnSelectAll.setText("Deselect All");
        } else {

            tableView.getSelectionModel().clearSelection();
            bnSelectAll.setText("Select All");
            invisibleItem(bnSelectAll);
        }


    }

    public void deleteMultipleProduct(ActionEvent event) {
       int res  = 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("ARE YOU SURE YOU WANT TO DELETE SELECTED PRODUCT ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {

            ObservableList<Products> selectedItems = tableView.getSelectionModel().getSelectedItems();
            for (Products products : selectedItems) {
             int[]  i = deleteProduct(products.getProductID());

             res = i.length;
            }

            if (res > 0) {
                bnRefresh(null);
                customDialog.showAlertBox("Success","Successfully Deleted");
            }

        } else {
            alert.close();
        }
    }
    private void search_Item() {

        filteredData = new FilteredList<>(productsList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(products -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (products.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (products.getProductType().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getProductCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getProductColor().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                } else if (products.getAdded_date().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }else if (String.valueOf(products.getHsn_sac()).toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }
                return false;
            });

            changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
        });

        pagination.setCurrentPageIndex(0);
        changeTableView(0, rowsPerPage);
        pagination.currentPageIndexProperty().addListener(
                (observable1, oldValue1, newValue1) -> changeTableView(newValue1.intValue(), rowsPerPage));

    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(filteredData.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));
        colColor.setCellValueFactory(new PropertyValueFactory<>("productColor"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colProductCode.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        colType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("totalTax"));
        colHsnSan.setCellValueFactory(new PropertyValueFactory<>("hsn_sac"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("sizeUnit"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("added_date"));

        Callback<TableColumn<Products, String>, TableCell<Products, String>>
                cellFactory = (TableColumn<Products, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {

                    FileInputStream input_edit, input_delete;
                    File edit_file, delete_file;
                    ImageView iv_edit, iv_delete;
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
                                    + "-fx-fill:#c506fa;"
                    );

                    iv_delete.setStyle(
                            " -fx-cursor: hand ;"

                                    + "-fx-fill:#ff0000;"
                    );
                    iv_edit.setOnMouseClicked((MouseEvent event) -> {

                        Products edit_selection = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == edit_selection) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }

                        Main.primaryStage.setUserData(edit_selection);

                        customDialog.showFxmlDialog("update/productUpdate.fxml", "UPDATE PRODUCT");
                        bnRefresh(null);

                    });
                    iv_delete.setOnMouseClicked((MouseEvent event) -> {


                        Products products = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == products) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Warning ");
                        alert.setHeaderText("ARE YOU SURE YOU WANT TO DELETE THIS PRODUCT ( " + products.getProductName() + " )");
                        alert.initModality(Modality.APPLICATION_MODAL);
                        alert.initOwner(Main.primaryStage);
                        Optional<ButtonType> result = alert.showAndWait();
                        ButtonType button = result.orElse(ButtonType.CANCEL);
                        if (button == ButtonType.OK) {

                            int[] res = deleteProduct(products.getProductID());
                            if (res.length > 0) {
                                bnRefresh(null);
                                customDialog.showAlertBox("Success","Successfully Deleted");
                            }

                        } else {
                            alert.close();
                        }

                    });

                    HBox managebtn = new HBox(iv_edit, iv_delete);

                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(iv_edit, new Insets(0, 0, 0, 0));
                    HBox.setMargin(iv_delete, new Insets(0, 3, 0, 20));

                    setGraphic(managebtn);

                    setText(null);

                }
            }

        };

        Callback<TableColumn<Products, String>, TableCell<Products, String>>
                cellSize = (TableColumn<Products, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {
                    Label bnCheckPrice = new Label("CHECK PRICE");

                    bnCheckPrice.setMinWidth(100);

                    bnCheckPrice.setStyle("-fx-background-color: #008080; -fx-background-radius: 3 ; " +
                            "-fx-padding: 5 8 5 8 ; -fx-text-fill: white; -fx-alignment: center;-fx-cursor: hand");


                    bnCheckPrice.setOnMouseClicked(event -> {
                        Products products = tableView.getSelectionModel().getSelectedItem();

                        if (null == tableView) {
                            {
                                return;
                            }
                        }

                        Main.primaryStage.setUserData(products);

                        customDialog.showFxmlDialog("ViewSizeAndPrice.fxml", "SIZE AND PRICE CHART");
                        bnRefresh(null);
                    });

                    HBox container = new HBox(bnCheckPrice);
                    container.setStyle("-fx-alignment:center");
                    HBox.setMargin(bnCheckPrice, new Insets(2, 20, 2, 20));
                    setGraphic(container);

                    setText(null);
                }
            }

        };
        colAction.setCellFactory(cellFactory);
        colPrice.setCellFactory(cellSize);

        customColumn(colProductName);
        customColumn(colDescription);
        customColumn(colSize);
        customColumn(colDate);
        customColumn(colProductCode);


        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, productsList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Products> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

    private void listener() {


        refresh_bn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                bnRefresh(null);

            }
        });
    }

    private void setCustomImage() {

        // refreshImage.setImage(method.getImage("src/main/resources/com/shop/management/img/icon/refresh_ic.png"));
    }

    private void getProduct() {

        if (null != productsList) {
            productsList.clear();

        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            String query = "SELECT tp.product_id, (TO_CHAR(tp.added_date, 'DD-MM-YYYY HH12:MI:SS AM')) as added_date , tp.product_code , tp.product_name , tp.product_description\n" +
                    "        ,tp.product_color,tp.product_type,tc.category_id, tc.category_name,\n" +
                    "       tp.discount_id ,tp.tax_id ,\n" +
                    "       td.discount_id ,td.discount,td.description,tpt.tax_id , tpt.hsn_sac ,\n" +
                    "       tpt.tax_id ,tpt.sgst,tpt.cgst,tpt.igst,tpt.description,tpt.gstName,\n" +
                    "       (select string_agg(concat(tps.height , 'x' , tps.width ,' ', tps.size_unit ),', ' ) as height_width\n" +
                    "        from tbl_product_stock as tps where  tps.product_id = tp.product_id group by tp.product_id )\n" +
                    "\n" +
                    "FROM   tbl_products as tp\n" +
                    "           LEFT JOIN tbl_category as tc ON tp.category_id = tc.category_id         Left JOIN tbl_discount as td  ON ( tp.discount_id = td.discount_id )\n" +
                    "           Left Join tbl_product_tax as tpt  on ( tp.tax_id = tpt.tax_id ) ORDER BY tp.product_id DESC";

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
                String productCategory = rs.getString("category_name");
                int productDiscountID = rs.getInt("discount_id");
                String addedDate = " " + rs.getString("added_date");
                String productCode = " " + rs.getString("product_code");
                int productTaxID = rs.getInt("tax_id");

                // discount
                int discountID = rs.getInt("discount_id");
                double discountPer = rs.getInt("discount");

                // tax
                int hsnSac = rs.getInt("hsn_sac");
                int taxId = rs.getInt("tax_id");
                double sgst = rs.getInt("sgst");
                double cgst = rs.getInt("cgst");
                double igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

                String size =rs.getString("height_width");

               if (null == size){
                   size = "-";
               }
                double totalTaxPer = sgst + cgst + igst;

                productsList.add(new Products(0, productID, 0, 0,
                        0, 0, 0, size,
                        null, 0, productID, productName, productDescription, productColor,
                        productType, productCategory, discountID, taxId, null, addedDate,
                        discountPer, totalTaxPer, hsnSac, productCode));

            }

            if (productsList.size() > 0) {
                pagination.setVisible(true);
                search_Item();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(connection, ps, rs);

        }

    }

    private int[] deleteProduct(int productId) {

        Connection con = null;
        Statement ps = null;

        try {

            con = dbconnection.getConnection();

            String stockQuery = "DELETE FROM tbl_product_stock WHERE product_id =" + productId;
            String productQuery = "DELETE FROM tbl_products WHERE product_id =" + productId;

            ps = con.createStatement();
            ps.addBatch(stockQuery);
            ps.addBatch(productQuery);

            return ps.executeBatch();


        } catch (SQLException e) {
            customDialog.showAlertBox("ERROR", e.getMessage());
            e.printStackTrace();
            return new int[]{0};
        } finally {

            DBConnection.closeConnection(con, null, null);

            try {
                if (null != ps) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


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

    public void bnRefresh(MouseEvent event) {
        getProduct();
        tableView.refresh();
        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
    }
}
