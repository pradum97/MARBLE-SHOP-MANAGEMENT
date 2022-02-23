package com.shop.management.Method;

import com.shop.management.Model.UserDetails;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class GetUserProfile {
    Method method;
    DBConnection dbConnection;
    Properties properties;

    public UserDetails getUser(int userId){

        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");

        Connection connection = null;
        PreparedStatement userPs = null;
        ResultSet userRs = null;
        UserDetails userDetails = null;

        try{

            connection = dbConnection.getConnection();

            if (null == connection){
                System.out.println("GetUserProfile : connection Failed");
                return null;
            }

            userPs = connection.prepareStatement(properties.getProperty("USERS"));
            userPs.setInt(1,userId);
            userRs = userPs.executeQuery();

            if (userRs.next()){

                int id = userRs.getInt("user_id");
                long phone = userRs.getLong("phone");
                String user_image = userRs.getString("USER_IMG_PATH");
                String password = userRs.getString("password");
                String first_name = userRs.getString("first_name");
                String last_name = userRs.getString("last_name");
                String gender = userRs.getString("gender");
                String email = userRs.getString("email");
                String role = userRs.getString("role");
                String username = userRs.getString("username");
                String address = userRs.getString("full_address");
                String create_time = userRs.getString("created_time");
                int accountStatus = userRs.getInt("account_status");
                String acStatus = switch (accountStatus) {

                    case 0 -> "Inactive";
                    default -> "Active";
                };

                userDetails = new UserDetails(id,acStatus,phone,first_name,last_name,gender,role,
                        email,username,password,address,user_image,create_time);

            }else {
                System.out.println("User Not Found..");
            }

            return userDetails;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {

            CloseConnection.closeConnection(connection, userPs, userRs);
        }
    }

    public ObservableList<UserDetails> getAllUser(){

        method = new Method();
        dbConnection = new DBConnection();
        properties = method.getProperties("query.properties");
        ObservableList<UserDetails> usersList = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement userPs = null;
        ResultSet userRs = null;

        try{

            connection = dbConnection.getConnection();

            if (null == connection){
                System.out.println("GetUserProfile : connection Failed");
                return null;
            }

            userPs = connection.prepareStatement(properties.getProperty("ALL_USERS"));
            userRs = userPs.executeQuery();

            while (userRs.next()){

                int id = userRs.getInt("user_id");
                long phone = userRs.getLong("phone");
                String user_image = userRs.getString("USER_IMG_PATH");
                String password = userRs.getString("password");
                String first_name = userRs.getString("first_name");
                String last_name = userRs.getString("last_name");
                String gender = userRs.getString("gender");
                String email = userRs.getString("email");
                String role = userRs.getString("role");
                String username = userRs.getString("username");
                String address = userRs.getString("full_address");
                String create_time = userRs.getString("created_time");
                int accountStatus = userRs.getInt("account_status");

                String acStatus = switch (accountStatus) {
                    case 0 -> "Inactive";
                    default -> "Active";
                };


                usersList.add(new UserDetails(id,acStatus,phone,first_name,last_name,gender,role,
                        email,username,password,address,user_image,create_time));

            }
            return usersList;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }finally {

            CloseConnection.closeConnection(connection, userPs, userRs);
        }
    }
}
