package com.shop.management.Controller.Update;

import com.shop.management.CustomDialog;
import com.shop.management.Main;
import com.shop.management.Method.Method;
import com.shop.management.Model.CategoryModel;
import com.shop.management.util.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CategoryUpdate implements Initializable {
    public TextField categoryNameTF;
    private  CategoryModel categoryModel;
    private Method method;
    private DBConnection dbConnection;
    private CustomDialog customDialog;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

         categoryModel = (CategoryModel) Main.primaryStage.getUserData();
        method = new Method();
        dbConnection = new DBConnection();
        customDialog = new CustomDialog();

         if (null == categoryModel){
             return;
         }
        setData(categoryModel);
    }

    private void setData(CategoryModel categoryModel) {

        categoryNameTF.setText(categoryModel.getCategoryName());
    }

    public void updateCategory(ActionEvent event) {

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

            ps = connection.prepareStatement("UPDATE tbl_category SET CATEGORY_NAME = ? where category_id = ?");
            ps.setString(1,cName);
            ps.setInt(2,categoryModel.getCategoryId());

            int res = ps.executeUpdate();

            if (res >0){
                categoryNameTF.setText("");

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                if (stage.isShowing()){
                    stage.close();
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }finally {
            DBConnection.closeConnection(connection,ps,null);
        }

    }
}
