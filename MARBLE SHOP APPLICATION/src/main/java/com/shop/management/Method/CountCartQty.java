package com.shop.management.Method;

import com.shop.management.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountCartQty {

    public int countQty( int stockId , String qUnit , int pcsPerPkt){

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        int qty = 0;
        String unit = null;

        if (qUnit.equals("PCS")){
            unit = "PKT";
        }if (qUnit.equals("PKT")){
            unit = "PCS";
        }


        try {
            connection = new DBConnection() .getConnection();

            String query = "select * from tbl_cart where stock_id = ? and quantity_unit = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, stockId);
            ps.setString(2 , unit);

            rs = ps.executeQuery();
            while (rs.next()){

                int quantity = rs.getInt("quantity");
                String qtyUnit = rs.getString("quantity_unit");

                if (qtyUnit.equals("PKT")){
                    qty += quantity*pcsPerPkt;
                }else {
                    qty += quantity;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection, ps, rs);
        }

        return qty;
    }
}
