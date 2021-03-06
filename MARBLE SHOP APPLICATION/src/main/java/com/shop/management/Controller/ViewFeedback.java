package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.ImageLoader;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.Feedback;
import com.shop.management.PropertiesLoader;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewFeedback implements Initializable {

    public Pagination pagination;
    public TextField searchTf;
    int rowsPerPage = 10;

    public TableColumn<Feedback, String> col_action;
    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;
    public TableColumn<Feedback, String> col_id;
    public TableColumn<Feedback, String> col_name;
    public TableColumn<Feedback, String> col_email;
    public TableColumn<Feedback, String> col_phone;
    public TableColumn<Feedback, String> col_star;
    public TableColumn<Feedback, String> col_message;
    public TableColumn<Feedback, String> col_date;
    public TableView<Feedback> tableView;

    private ObservableList<Feedback> feedList = FXCollections.observableArrayList();
    FilteredList<Feedback> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();
        setData();

    }

    private void setData() {

        if (null != feedList) {

            feedList.clear();
        }

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();

            if (null == connection) {
                return;
            }

            ps = connection.prepareStatement(new PropertiesLoader().getReadProp().getProperty("GET_FEEDBACK"));

            rs = ps.executeQuery();

            while (rs.next()) {

                int feedId = rs.getInt("feedback_id");
                String fullName = rs.getString("full_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String star = rs.getString("star");
                String message = rs.getString("message");
                String date = rs.getString("feedback_date");

                feedList.add(new Feedback(feedId, phone, fullName, email, star, message, date));
            }

            if (feedList.size()>0){
                pagination.setVisible(true);
                search_Item();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            DBConnection.closeConnection(connection, ps, rs);
        }


    }

    private void search_Item() {

        filteredData = new FilteredList<>(feedList, p -> true);

        searchTf.textProperty().addListener((observable, oldValue, newValue) -> {

            filteredData.setPredicate(feed -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (feed.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;

                } else if (feed.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (feed.getFeed_phone().toLowerCase().contains(lowerCaseFilter)) {
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


        col_id.setCellValueFactory(new PropertyValueFactory<>("feed_id"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("feed_phone"));
        col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_star.setCellValueFactory(new PropertyValueFactory<>("star"));
        col_message.setCellValueFactory(new PropertyValueFactory<>("message"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("feedDate"));


        Callback<TableColumn<Feedback, String>, TableCell<Feedback, String>>
                cellFactory = (TableColumn<Feedback, String> param) -> new TableCell<>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);

                } else {

                    String path = "img/icon/";

                    ImageLoader loader = new ImageLoader();
                    ImageView iv_edit = new ImageView(loader.load(path + "edit_ic.png"));
                    iv_edit.setFitHeight(22);
                    iv_edit.setFitHeight(22);
                    iv_edit.setPreserveRatio(true);


                    ImageView iv_delete = new ImageView(loader.load(path + "delete_ic.png"));
                    iv_delete.setFitHeight(17);
                    iv_delete.setFitWidth(17);
                    iv_delete.setPreserveRatio(true);

                    iv_delete.managedProperty().bind(iv_delete.visibleProperty());
                    iv_delete.setVisible(Objects.equals(Login.currentRoleName.toLowerCase(), "admin".toLowerCase()));


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

                        Feedback edit_selection = tableView.
                                getSelectionModel().getSelectedItem();

                        if (null == edit_selection) {
                            method.show_popup("Please Select", tableView);
                            return;
                        }

                        Main.primaryStage.setUserData(edit_selection);

                        customDialog.showFxmlDialog("update/updateFeedback.fxml", "UPDATE");
                        refreshTableData();

                    });
                    iv_delete.setOnMouseClicked((MouseEvent event) -> {


                        Feedback delete_selection = tableView.getSelectionModel().getSelectedItem();

                        if (null == delete_selection) {
                            method.show_popup("Please Select ", tableView);
                            return;
                        }

                        deleteFeedback(delete_selection);

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

        col_action.setCellFactory(cellFactory);
        tableView.setItems(feedList);


        customColumn(col_name);
        customColumn(col_email);
        customColumn(col_message);
        customColumn(col_date);


        int fromIndex = index * limit;
        int toIndex = Math.min(fromIndex + limit, feedList.size());

        int minIndex = Math.min(toIndex, filteredData.size());
        SortedList<Feedback> sortedData = new SortedList<>(
                FXCollections.observableArrayList(filteredData.subList(Math.min(fromIndex, minIndex), minIndex)));
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());

        tableView.setItems(sortedData);

    }
    private void refreshTableData() {

        setData();
        changeTableView(pagination.getCurrentPageIndex(), rowsPerPage);

    }

    private void deleteFeedback(Feedback feed) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning ");
        alert.setHeaderText("Are You Sure You Want to Delete This Feedback");
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

                ps = con.prepareStatement("DELETE FROM TBL_FEEDBACK WHERE feedback_id = ?");
                ps.setInt(1, feed.getFeed_id());

                int res = ps.executeUpdate();

                if (res > 0) {
                    refreshTableData();
                    alert.close();
                }

            } catch (SQLException e) {
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

    private void customColumn(TableColumn<Feedback, String> columnName) {


        columnName.setCellFactory(tc -> {
            TableCell<Feedback, String> cell = new TableCell<>();
            Text text = new Text();
            text.setStyle("-fx-font-size: 14");
            cell.setGraphic(text);
            text.setId("t");
            text.setStyle("-fx-text-alignment: CENTER ;  -fx-padding: 10");
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(columnName.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });
    }

    public void bnRefresh(MouseEvent event) {


    }
}
