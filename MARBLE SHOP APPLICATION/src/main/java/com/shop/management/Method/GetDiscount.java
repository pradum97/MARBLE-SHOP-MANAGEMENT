package com.shop.management.Method;

import com.shop.management.Model.Discount;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetDiscount {


    public ObservableList<Discount> get (){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Discount> discountList = FXCollections.observableArrayList();

        try {

            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return null;
            }

            ps = connection.prepareStatement(new Method().getProperties("query.properties").getProperty("GET_DISCOUNT"));
            rs = ps.executeQuery();

            while (rs.next()) {

                // discount
                int discountID = rs.getInt("discount_id");
                int discount = rs.getInt("discount");
                String description = rs.getString("description");
                String discountName = rs.getString("discount_name");


                discountList.addAll(new Discount(discountID,discountName, discount, description));

            }

           return discountList;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            DBConnection.closeConnection(connection, ps, rs);
        }
    }
}
