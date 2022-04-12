package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.CategoryModel;
import com.shop.management.Model.CategoryModel;
import com.shop.management.util.DBConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Category implements Initializable
{

    private int rowsPerPage = 8;
    public Pagination pagination;
    public TextField categoryNameTF;
    public TableView<CategoryModel> tableView;
    public TableColumn<CategoryModel , String> colCName;
    public TableColumn<CategoryModel , String> colAction;
    public TableColumn<CategoryModel,Integer> colSrNo;
    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;

    private ObservableList<CategoryModel> categoryList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();

        getCategory();

        categoryNameTF.setFocusTraversable(false);
    }

    private void getCategory() {

        if (null != categoryList){
            categoryList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            if (null == connection){
                return;
            }

            ps = connection.prepareStatement("SELECT * FROM tbl_category order by category_id asc");
            rs = ps.executeQuery();

            while (rs.next()){
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");

                categoryList.add(new CategoryModel(categoryId , categoryName));
            }

            if (categoryList.size()>0){
                pagination.setVisible(true);
                pagination.setCurrentPageIndex(0);
                changeTableView(0, rowsPerPage);
                changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnection.closeConnection(connection,ps,rs);
        }
    }

    public void addCategory(ActionEvent event) {

        String cName = categoryNameTF.getText();

        if (cName.isEmpty()){
            method.show_popup("ENTER CATEGORY NAME",categoryNameTF);
            return;
        }

        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dbConnection.getConnection();;
            if (null == dbConnection){
                return;
            }

            ps = connection.prepareStatement("INSERT INTO tbl_category(CATEGORY_NAME) VALUES (?)");
            ps.setString(1,cName);

            int res = ps.executeUpdate();

            if (res >0){
                getCategory();
                categoryNameTF.setText("");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            customDialog.showAlertBox("Failed...","DUPLICATE VALUE NOT ALLOW");
        }finally {
            DBConnection.closeConnection(connection,ps,null);
        }
    }

    private void changeTableView(int index, int limit) {

        int totalPage = (int) (Math.ceil(categoryList.size() * 1.0 / rowsPerPage));
        pagination.setPageCount(totalPage);

        colSrNo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(
                tableView.getItems().indexOf(cellData.getValue()) + 1));
        colCName.setCellValueFactory(new PropertyValueFactory<>("categoryName"));

        Callback<TableColumn<CategoryModel, String>, TableCell<CategoryModel, String>>
                cellFactory = (TableColumn<CategoryModel, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {

                    ImageView  iv_delete , ivEdit;

                    String path = "img/icon/";

                    ImageLoader loader = new ImageLoader();

                    iv_delete = new ImageView(loader.load(path+"delete_ic.png"));
                    iv_delete.setFitHeight(17);
                    iv_delete.setFitWidth(17);
                    iv_delete.setPreserveRatio(true);

                    ivEdit = new ImageView(loader.load(path+"edit_ic.png"));
                    ivEdit.setFitHeight(21);
                    ivEdit.setFitWidth(21);
                    ivEdit.setPreserveRatio(true);

                    iv_delete.managedProperty().bind(iv_delete.visibleProperty());
                    iv_delete.setVisible(Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase()));

                    iv_delete.setStyle(
                            " -fx-cursor: hand ;"

                                    + "-fx-fill:#ff0000;"
                    );
                    ivEdit.setStyle(
                            " -fx-cursor: hand ;"

                                    + "-fx-fill:#ff0000;"
                    );
                    iv_delete.setOnMouseClicked((MouseEvent event) -> {


                        CategoryModel categoryModel = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == categoryModel) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }


                        deleteProduct(categoryModel);

                    });
                    ivEdit.setOnMouseClicked((MouseEvent event) -> {


                        CategoryModel categoryModel = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == categoryModel) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }

                        Main.primaryStage.setUserData(categoryModel);
                        customDialog.showFxmlDialog("update/categoryUpdate.fxml","UPDATE CATEGORY");
                        getCategory();

                    });

                    HBox managebtn = new HBox(ivEdit , iv_delete);

                    managebtn.setStyle("-fx-alignment:center");
                    HBox.setMargin(ivEdit, new Insets(0, 3, 0, 20));
                    HBox.setMargin(iv_delete, new Insets(0, 3, 0, 20));

                    setGraphic(managebtn);

                    setText(null);

                }
            }

        };


        colAction.setCellFactory(cellFactory);

        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, categoryList.size());

        int minIndex = Math.min(toIndex, categoryList.size());
        SortedList<CategoryModel> sortedData = new SortedList<>(
                FXCollections.observableArrayList(categoryList.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }

    private void deleteProduct(CategoryModel categoryModel) {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Category ( " + categoryModel.getCategoryName() + " )");
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

                ps = con.prepareStatement("DELETE FROM tbl_category WHERE category_id = ?");
                ps.setInt(1, categoryModel.getCategoryId());

                int res = ps.executeUpdate();

                if (res > 0) {
                    getCategory();
                    changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);
                    customDialog.showAlertBox("", "Successfully Deleted");
                    alert.close();

                }
            } catch (SQLException e) {
                customDialog.showAlertBox("ERROR", e.getMessage());
                e.printStackTrace();
            } finally {

                DBConnection.closeConnection(con, ps, null);
            }

        } else {
            alert.close();
        }
    }


    public void enterPress(KeyEvent event) {

        if (event.getCode() == KeyCode.ENTER){

            addCategory(null);
        }
    }
}
