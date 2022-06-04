package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.StaticData;
import com.shop.management.Model.SupplierModel;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllSupplier implements Initializable {
    public TableView<SupplierModel> tableView;
    public TableColumn<SupplierModel, Integer> colSrNo;
    public TableColumn<SupplierModel, String> colName;
    public TableColumn<SupplierModel, String> colPhone;
    public TableColumn<SupplierModel, String> colEmail;
    public TableColumn<SupplierModel, String> colGstNum;
    public TableColumn<SupplierModel, String> colAddress;
    public TableColumn<SupplierModel, String> colState;
    public TableColumn<SupplierModel, String> colDate;
    public TableColumn<SupplierModel, String> colAction;
    private DBConnection dbConnection;
    private CustomDialog customDialog;

    private ObservableList<SupplierModel> supplierList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        getSupplier();
    }

    private void getSupplier() {

        if (null != supplierList) {
            supplierList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = dbConnection.getConnection();
            if (null == connection) {
                return;
            }

            String query = "SELECT * ,(TO_CHAR(ADDED_DATE, 'DD-MM-YYYY')) as date FROM Supplier order by supplier_id asc";
            ps = connection.prepareStatement(query);

            rs = ps.executeQuery();

            while (rs.next()) {

                int supplierId = rs.getInt("supplier_id");
                String supplierName = rs.getString("supplier_name");
                String supplierPhone = rs.getString("supplier_phone");
                String supplierEmail = rs.getString("supplier_email");
                String supplierGstNum = rs.getString("supplier_gstNo");
                String supplierAddress = rs.getString("ADDRESS");
                String supplierState = rs.getString("STATE");
                String date = rs.getString("date");

                supplierList.add(new SupplierModel(supplierId, supplierName, supplierPhone, supplierEmail, supplierGstNum, supplierAddress, supplierState, date));
            }


            colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                    tableView.getItems().indexOf(cellData.getValue()) + 1));
            colName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("supplierPhone"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("supplierEmail"));
            colGstNum.setCellValueFactory(new PropertyValueFactory<>("supplierGstNum"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("supplierAddress"));
            colState.setCellValueFactory(new PropertyValueFactory<>("supplierState"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("addedSate"));

            onColumnEdit(colName, "supplier_name");
            onColumnEdit(colPhone, "supplier_phone");
            onColumnEdit(colEmail, "supplier_email");
            onColumnEdit(colGstNum, "supplier_gstNo");
            onColumnEdit(colAddress, "ADDRESS");
            onColumnEdit(colState, "STATE");

            setOptionalCell();
            tableView.setItems(supplierList);


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    private void onColumnEdit(TableColumn<SupplierModel, String> col, String updateColumnName) {

        col.setCellFactory(TextFieldTableCell.forTableColumn());

        col.setOnEditCommit(e -> {

            if (col.equals(colEmail)) {
                String email = e.getNewValue();
                Pattern pattern = Pattern.compile(new StaticData().emailRegex);
                Matcher matcher = pattern.matcher(email);
                if (!email.isEmpty()) {
                    if (!matcher.matches()) {
                        getSupplier();
                        customDialog.showAlertBox("Failed", "Enter Valid Email Address");
                        return;
                    }
                }
            } else {
                String value = e.getNewValue();

                if (value.isEmpty()) {
                    getSupplier();
                    customDialog.showAlertBox("Failed", "Empty Value Not Accepted");
                    return;
                }
            }
            int supplierId = e.getTableView().getItems().get(e.getTablePosition().getRow()).getSupplierId();

            update(e.getNewValue(), updateColumnName, supplierId);
        });
    }

    private void update(String newValue, String columnName, int supplierId) {

        Connection connection = null;
        PreparedStatement ps = null;

        try {

            connection = dbConnection.getConnection();

            if (null == connection) {
                return;
            }

            String query = "UPDATE Supplier SET " + columnName + " = ?  where supplier_id = ?";

            ps = connection.prepareStatement(query);
            ps.setString(1, newValue);
            ps.setInt(2, supplierId);

            int res = ps.executeUpdate();

            if (res > 0) {
                getSupplier();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, null);
        }
    }

    private void deleteSupplier(SupplierModel sm) {

        int sullpierId = sm.getSupplierId();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Supplier ( " + sm.getSupplierName() + " )");
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

                ps = con.prepareStatement("DELETE FROM Supplier WHERE supplier_id = ?");
                ps.setInt(1, sullpierId);

                int res = ps.executeUpdate();

                if (res > 0) {

                    customDialog.showAlertBox("", "Successfully Deleted");
                    alert.close();
                    getSupplier();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {

                DBConnection.closeConnection(con, ps, null);
            }

        } else {
            alert.close();
        }
    }

    private void setOptionalCell() {


        Callback<TableColumn<SupplierModel, String>, TableCell<SupplierModel, String>>
                cellFactory = (TableColumn<SupplierModel, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {

                    ImageView ivDelete = new ImageView();

                    ivDelete.setFitWidth(20);
                    ivDelete.setFitHeight(20);

                    ivDelete.setImage(new ImageLoader().load("img/icon/delete_ic.png"));

                    ivDelete.managedProperty().bind(ivDelete.visibleProperty());
                    ivDelete.setVisible(Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase()));

                    HBox managebtn = new HBox(ivDelete);

                    ivDelete.setOnMouseClicked(mouseEvent -> {

                        SupplierModel sm = tableView.getSelectionModel().getSelectedItem();

                        deleteSupplier(sm);
                    });

                    ivDelete.setStyle("-fx-cursor: hand");
                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(ivDelete, new Insets(2, 10, 2, 5));

                    setGraphic(managebtn);
                    setText(null);
                }
            }

        };

        colAction.setCellFactory(cellFactory);

    }

    public void addSupplier(ActionEvent event) {
        customDialog.showFxmlDialog2("stock/addSupplier.fxml", "ADD SUPPLIER");
        getSupplier();
    }
}
