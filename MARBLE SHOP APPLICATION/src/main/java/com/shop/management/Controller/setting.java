package com.shop.management.Controller;

import com.shop.management.CustomDialog;
import com.shop.management.Method.Method;
import com.shop.management.util.DBConnection;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class setting implements Initializable {
    public ComboBox theme;
    public ComboBox fontStyle;
    public ComboBox billing;
    public ComboBox igstTax;
    public ComboBox cgstTax;
    public ComboBox sgstTax;
    public Button saveBtn;
    Connection conn=null;
    DBConnection dbConnection=new DBConnection();
    Method method=new Method();
    Properties prop;
    ObservableList<String>themeList;
    ObservableList<String>fontStyleList;
    ObservableList<Double>igstTaxList;
    ObservableList<Double>cgstTaxList;
    ObservableList<Double>sgstTaxList;
    ObservableList<String>billingTypeList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setThemeAndFontList();
        setIgstCgstSgst();
        setBillingType();
        setSettingValue();
        double ram= (double) igstTax.getValue();
        System.out.println(ram+1);

    }
//    set item add in theme and fontstle DropDwon
    private void setThemeAndFontList(){
        themeList=theme.getItems();
        fontStyleList=fontStyle.getItems();
        theme.setEditable(true);
        fontStyle.setEditable(true);
        try {
            conn= dbConnection.getConnection();
            prop=method.getProperties("query.properties");
            PreparedStatement stmt=conn.prepareStatement(prop.getProperty("SET_THEME_FONT_LIST"));
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                themeList.add(rs.getString("theme_name"));
                fontStyleList.add(rs.getString("font_name"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // set item value add in tax dropdoewn

    private  void  setIgstCgstSgst(){
        igstTaxList=igstTax.getItems();
        cgstTaxList=cgstTax.getItems();
        sgstTaxList=sgstTax.getItems();
        igstTax.setEditable(true);
        cgstTax.setEditable(true);
        sgstTax.setEditable(true);
        try {
            conn=dbConnection.getConnection();
            prop=method.getProperties("query.properties");
            PreparedStatement stmt=conn.prepareStatement(prop.getProperty("SET_IGST_CGST_SGST_List"));
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                igstTaxList.add(rs.getDouble("igst"));
                cgstTaxList.add(rs.getDouble("cgst"));
                sgstTaxList.add(rs.getDouble("sgst"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //set item bill type
    private  void setBillingType(){
        billingTypeList=billing.getItems();
        billing.setEditable(true);
        billingTypeList.addAll("Non Gst Bill","Gst Bill","Stander Bill","On Diamond Bil");
    }

    private  void setSettingValue(){
        try {
            conn=dbConnection.getConnection();
            prop=method.getProperties("query.properties");
            PreparedStatement stmt=conn.prepareStatement(prop.getProperty("SET_VALUE"));
            ResultSet rs=stmt.executeQuery();
            while (rs.next()){
                theme.setValue(rs.getString("theme"));
                fontStyle.setValue(rs.getString("font_style"));
                billing.setValue(rs.getString("billing_type"));
                igstTax.setValue(rs.getDouble("igst_tax"));
                sgstTax.setValue(rs.getDouble("sgst_tax"));
                cgstTax.setValue(rs.getDouble("cgst_tax"));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

   public void updateSetting()  {
       CustomDialog customDialog=new CustomDialog();
        try {
            conn=dbConnection.getConnection();
            prop=method.getProperties("query.properties");
            PreparedStatement stmt=conn.prepareStatement(prop.getProperty("UpdateSetting"));
            stmt.setString(1,theme.getValue().toString());
            stmt.setString(2,fontStyle.getValue().toString());
            stmt.setString(3,billing.getValue().toString());
            stmt.setString(4, igstTax.getValue().toString());
           stmt.setString(5,  cgstTax.getValue().toString());
           stmt.setString(6,  sgstTax.getValue().toString());
            int rs=stmt.executeUpdate();
            if(rs==1){
                customDialog.showAlertBox("Message","Successful Setting Update ");
            }else{
                System.out.println("update not successful ");

            }

        } catch (SQLException throwables) {
            throwables.getMessage();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
   }
}

