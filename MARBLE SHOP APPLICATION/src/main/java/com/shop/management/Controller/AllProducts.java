package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Products;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class AllProducts implements Initializable {

    int rowsPerPage = 15;

    public TableColumn<Products, String> colColor;
    public TableColumn<Products , Integer> colSrNo;
    public TableColumn<Products, String> colProductName;
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


        listener();
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
                } else if (products.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (products.getProductColor().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }else if (products.getAdded_date().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }else if (products.getTotalDiscount().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }else if (products.getTotalTax().toLowerCase().contains(lowerCaseFilter)) {

                    return true;
                }
                else if (String.valueOf(products.getHsn_sac()).toLowerCase().contains(lowerCaseFilter)) {

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
        colType.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("totalDiscount"));
        colTax.setCellValueFactory(new PropertyValueFactory<>("totalTax"));
        colHsnSan.setCellValueFactory(new PropertyValueFactory<>("hsn_sac"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("sizeUnit"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("added_date"));


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

            String query = "SELECT tp.product_id,tp.added_date , tp.product_name , tp.product_description\n" +
                    "       ,tp.product_color,tp.product_type,tp.category,\n" +
                    "       tp.discount_id ,tp.tax_id ,\n" +
                    "       td.discount_id ,td.discount,td.description,tpt.tax_id , tpt.hsn_sac ,\n" +
                    "       tpt.tax_id ,tpt.sgst,tpt.cgst,tpt.igst,tpt.description,tpt.\"gstName\",\n" +
                    "       (select string_agg(concat(tps.height , 'x' , tps.width ,' ', tps.size_unit ),', ' ) as height_width\n" +
                    "       from tbl_product_stock as tps where  tps.product_id = tp.product_id group by tp.product_id )\n" +
                    "\n" +
                    "FROM   tbl_products as tp\n" +
                    "         Left JOIN tbl_discount as td  ON ( tp.discount_id = td.discount_id )\n" +
                    "         Left Join tbl_product_tax as tpt  on ( tp.tax_id = tpt.tax_id ) ORDER BY tp.product_id DESC";

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
                int productDiscountID = rs.getInt("discount_id");
                String addedDate = " " + rs.getString("added_date");
                int productTaxID = rs.getInt("tax_id");

                // discount
                int discountID = rs.getInt("discount_id");
                int totalDiscount = rs.getInt("discount");

                // tax
                int hsnSac = rs.getInt("hsn_sac");
                int taxId = rs.getInt("tax_id");
                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

                String size = " " + rs.getString("height_width");

                int totalTax = sgst + cgst + igst;

                String[] str = addedDate.split("\\.");


                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
              //  Date date = new Date();

              // String date = formatter.format(new Date(addedDate));

                productsList.add(new Products(0, productID, 0, 0,
                        0, 0, 0, size,
                        null, 0, productID, productName, productDescription, productColor,
                        productType, productCategory, discountID, taxId, null, str[0],
                        String.valueOf(totalDiscount), String.valueOf(totalTax), hsnSac));

            }

            if (productsList.size()>0){
                pagination.setVisible(true);
                search_Item();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(connection, ps, rs);

        }

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


                        deleteProduct(products);

                    });

                    HBox managebtn = new HBox(iv_edit, iv_delete);

                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(iv_edit, new Insets(0, 2, 0, 0));
                    HBox.setMargin(iv_delete, new Insets(0, 3, 0, 30));

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
        tableView.setItems(productsList);

        customColumn(colProductName);
        customColumn(colDescription);
        customColumn(colSize);
        customColumn(colDate);
        tableView.getSelectionModel().selectFirst();

    }

    private void deleteProduct(Products products) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Product ( " + products.getProductName() + " )");
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

                ps = con.prepareStatement("DELETE FROM tbl_product_stock WHERE product_id = ?");
                ps.setInt(1, products.getProductID());

                int res = ps.executeUpdate();

                if (res > 0) {

                    ps = null;
                    ps = con.prepareStatement("select  * from tbl_product_img where product_id = ?");
                    ps.setInt(1, products.getProductID());

                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        String path = rs.getString("img_path");

                        File file = new File("src/main/resources/com/shop/management/img/Product_Image/" + path);
                        if (file.exists()) {
                            FileUtils.forceDelete(file);
                        }
                    }
                    ps = null;

                    if (null != rs) {
                        rs.close();
                    }
                    ps = con.prepareStatement("DELETE FROM tbl_product_img WHERE product_id = ?");
                    ps.setInt(1, products.getProductID());
                    ps.executeUpdate();

                    ps = null;

                    ps = con.prepareStatement("DELETE FROM tbl_products WHERE product_id = ?");
                    ps.setInt(1, products.getProductID());

                    int i = ps.executeUpdate();

                    if (i > 0) {
                        bnRefresh(null);
                        customDialog.showAlertBox("", "Successfully Deleted");
                        alert.close();
                    }

                }
            } catch (SQLException | IOException e) {
                customDialog.showAlertBox("ERROR", e.getMessage());
                e.printStackTrace();
            } finally {

                DBConnection.closeConnection(con, ps, null);
            }

        } else {
            alert.close();
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
