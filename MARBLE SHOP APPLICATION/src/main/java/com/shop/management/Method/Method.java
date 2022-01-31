package com.shop.management.Method;

import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Method {

   public String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

    public ObservableList<String> getGender() {

        return FXCollections.observableArrayList("Male", "Female", "Other");
    }

    public ObservableList<String> getAccountStatus() {

        return FXCollections.observableArrayList("Inactive","Active" );
    }

    public ObservableList<String> getRole(){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<String> role = FXCollections.observableArrayList();



        try {
            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println(" Signup ( 65 ) : Connection Failed");
                return null;
            }
            ps = connection.prepareStatement(getProperties("query.properties").getProperty("ROLE"));
            rs = ps.executeQuery();

            while (rs.next()) {
                int role_ID = rs.getInt("ROLE_ID");
                String roleName = rs.getString("ROLE");

              role.add(roleName);

            }

            return  role;


        } catch (SQLException e) {
            e.printStackTrace();
            return  null;
        } finally {

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


    }

    public ContextMenu show_popup(String message, Object textField){

       ContextMenu form_Validator = new ContextMenu();
        form_Validator.setAutoHide(true);
        form_Validator.getItems().add(new MenuItem(message));
        form_Validator.show((Node) textField, Side.RIGHT, 10, 0);

        return form_Validator;

    }

    public Properties getProperties(String filename){

        try {
            File file = new File("src/main/java/com/shop/management/util/" + filename);
            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            Properties prop = new Properties();
            prop.load(fileInputStream);
            return prop;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String get_mac_address() {

        InetAddress ip;
        StringBuilder sb;
        try {

            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();

            sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    public Image getImage(String path){

        InputStream is = null;

        try {

            File file = new File(path);
             is = new FileInputStream(file.getAbsolutePath());

            return new Image(is);

        } catch (FileNotFoundException e) {
            try {
                is = new FileInputStream("src/main/resources/com/shop/management/img/icon/person_ic.png");
                return new Image(is);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;

            }


        }finally {

            try {

                if (null != is ){
                    is.close();
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }


    }
}
