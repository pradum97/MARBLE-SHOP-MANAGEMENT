package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.CloseConnection;
import com.shop.management.Method.GetStockData;
import com.shop.management.Method.Method;
import com.shop.management.Model.Products;
import com.shop.management.Model.Stock;
import com.shop.management.util.DBConnection;
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
import javafx.stage.Modality;
import javafx.util.Callback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class ViewSizeAndPrice implements Initializable {

    public TableColumn<Stock, String> colSize;
    public TableColumn<Stock, String> colPurchasePrice;
    public TableColumn<Stock, String> colMrp;
    public TableColumn<Stock, String> colMinSellPrice;
    public TableColumn<Stock, String> colQuantity;
    public TableColumn<Stock, String> colAction;
    public TableView<Stock> tableView;
    private Method method;
    private CustomDialog customDialog;
    private DBConnection dbConnection;
    private Properties properties;
    private Products products;

    private ObservableList<Stock> stockList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        customDialog = new CustomDialog();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");

        products = (Products) Main.primaryStage.getUserData();

        if (null != products) {
            setTableData(products.getProductID());
        }
    }

    private void setTableData(int productID) {

        stockList = new GetStockData().getStockList(productID);

        if (null == stockList) {

            customDialog.showAlertBox("Failed", "Data Not Found");
            return;
        }

        tableView.setItems(stockList);

        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colMrp.setCellValueFactory(new PropertyValueFactory<>("productMRP"));
        colMinSellPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("fullQuantity"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("fullSize"));

        Callback<TableColumn<Stock, String>, TableCell<Stock, String>>
                cellFactory = (TableColumn<Stock, String> param) -> new TableCell<>() {
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
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#c506fa;"
                    );

                    iv_delete.setStyle(
                            " -fx-cursor: hand ;"
                                    + "-glyph-size:28px;"
                                    + "-fx-fill:#ff0000;"
                    );
                    iv_edit.setOnMouseClicked((MouseEvent event) -> {

                        Stock stock = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == stock) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }

                        Main.primaryStage.setUserData(stock);

                        customDialog.showFxmlDialog2("update/updateSize.fxml", "UPDATE SIZE");
                        setTableData(productID);

                    });

                    iv_delete.setOnMouseClicked((MouseEvent event) -> {


                        Stock stock = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == stock) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }


                        deleteSize(stock);

                    });

                    HBox managebtn = new HBox(iv_edit, iv_delete);

                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(iv_edit, new Insets(2, 2, 0, 0));
                    HBox.setMargin(iv_delete, new Insets(2, 3, 0, 30));

                    setGraphic(managebtn);

                    setText(null);

                }
            }

        };

        colAction.setCellFactory(cellFactory);

    }

    private void deleteSize(Stock stock) {

        int stockId = stock.getStockID();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Size ( " + stock.getFullSize() + " )");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(Main.primaryStage);
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);
        if (button == ButtonType.OK) {
            Connection con = null;
            PreparedStatement ps = null;

            try {

                con = dbConnection.getConnection();

                if (null == con) {
                    return;
                }

                ps = con.prepareStatement("DELETE FROM tbl_product_stock WHERE stock_id = ?");
                ps.setInt(1, stockId);

                int res = ps.executeUpdate();

                if (res > 0) {

                    customDialog.showAlertBox("", "Successfully Deleted");
                    alert.close();
                    setTableData(products.getProductID());

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

                CloseConnection.closeConnection(con, ps, null);
            }

        } else {
            alert.close();
        }
    }

    public void bnAddNewSize(ActionEvent event) {

        Main.primaryStage.setUserData(products);
        customDialog.showFxmlDialog2("addSize.fxml", "ADD NEW SIZE");
        setTableData(products.getProductID());
    }
}
