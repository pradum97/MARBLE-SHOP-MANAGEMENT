package com.shop.management.Controller.SettingController;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.GetTax;
import com.shop.management.Method.Method;
import com.shop.management.Model.TAX;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class GstConfig implements Initializable {

    int rowsPerPage = 10;
    public TableColumn<TAX, String> colSGST;
    public TableColumn<TAX, String> colCGST;
    public TableColumn<TAX, String> colIGST;
    public TableColumn<TAX, String> colGstName;
    public TableColumn<TAX, String> colAction;
    public TableColumn<TAX, String> colDes;
    public TableView<TAX> tableViewGst;
    public TableColumn<TAX, String> colHsn_Sac;
    public Pagination pagination;
    private ObservableList<TAX> taxList;

    private Method method;
    private DBConnection dbConnection;
    private Properties properties;
    private CustomDialog customDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        properties = new PropertiesLoader().load("query.properties");
        customDialog = new CustomDialog();

        setGstTableData();
    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(taxList.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        colHsn_Sac.setCellValueFactory(new PropertyValueFactory<>("hsn_sac"));
        colSGST.setCellValueFactory(new PropertyValueFactory<>("sgst"));
        colCGST.setCellValueFactory(new PropertyValueFactory<>("cgst"));
        colIGST.setCellValueFactory(new PropertyValueFactory<>("igst"));
        colGstName.setCellValueFactory(new PropertyValueFactory<>("gstName"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("taxDescription"));


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

                        ImageLoader imageLoader = new ImageLoader();

                      ImageView  iv_edit = new ImageView(imageLoader.load("img/icon/edit_ic.png"));
                        iv_edit.setFitHeight(22);
                        iv_edit.setFitHeight(22);
                        iv_edit.setPreserveRatio(true);

                      ImageView  iv_delete = new ImageView(imageLoader.load("img/icon/delete_ic.png"));
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

                            TAX edit_selection = tableViewGst.
                                    getSelectionModel().getSelectedItem();

                            if (null == edit_selection) {
                                method.show_popup("Please Select", tableViewGst);
                                return;
                            }

                            Main.primaryStage.setUserData(edit_selection);

                            customDialog.showFxmlDialog("setting/update/gstUpdate.fxml", "GST UPDATE");
                            setGstTableData();

                        });

                        iv_delete.setOnMouseClicked((MouseEvent event) -> {


                            TAX delete_selection = tableViewGst.
                                    getSelectionModel().getSelectedItem();

                            if (null == delete_selection) {
                                method.show_popup("Please Select ", tableViewGst);
                                return;
                            }

                            deleteGst(delete_selection);

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


        colAction.setCellFactory(cellFactory);
        tableViewGst.setItems(taxList);
        customColumn(colGstName);

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, taxList.size());

        int minIndex = Math.min(toIndex, taxList.size());
        SortedList<TAX> sortedData = new SortedList<>(
                FXCollections.observableArrayList(taxList.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableViewGst.comparatorProperty());

        tableViewGst.setItems(sortedData);

    }


    private void deleteGst(TAX tax) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This GST ( " + tax.getGstName() + " )");
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

                ps = con.prepareStatement("DELETE FROM TBL_PRODUCT_TAX WHERE TAX_ID = ?");
                ps.setInt(1, tax.getTaxID());

                int res = ps.executeUpdate();

                if (res > 0) {
                    setGstTableData();
                    alert.close();
                }

            } catch (SQLException e) {
                customDialog.showAlertBox("ERROR", "You cannot remove this GST because this GST has been used in your products.");
                e.printStackTrace();
            } finally {

                try {

                    if (null != con) {
                        con.close();
                    }
                    if (null != ps) {
                        ps.close();
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else {
            alert.close();
        }


    }

    public void addGST(ActionEvent event) {
        customDialog.showFxmlDialog("setting/addGst.fxml", "ADD NEW GST");
        setGstTableData();
    }

    private void setGstTableData() {
        if (null != taxList) {
            taxList.clear();
        }
        taxList = new GetTax().getGst();

        if (null == taxList) {
            return;
        }
        double tableMinHeight = 70;

        for (int i = 0; i < taxList.size(); i++) {

            tableMinHeight = tableMinHeight + 20 + i;

            tableViewGst.setMinHeight(tableMinHeight);

        }

        if (taxList.size()>0){
            pagination.setVisible(true);
        }

        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);

        pagination.setCurrentPageIndex(0);
        changeTableView(0, rowsPerPage);
        pagination.currentPageIndexProperty().addListener(
                (observable1, oldValue1, newValue1) -> changeTableView(newValue1.intValue(), rowsPerPage));

    }

    private void customColumn(TableColumn<TAX, String> columnName) {

        columnName.setCellFactory(tc -> {
            TableCell<TAX, String> cell = new TableCell<>();
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
