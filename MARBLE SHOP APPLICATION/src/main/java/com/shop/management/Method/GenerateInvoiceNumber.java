package com.shop.management.Method;

import com.shop.management.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenerateInvoiceNumber {

     DBConnection dbConnection;

    public String generate() {
        dbConnection = new DBConnection();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = dbConnection.getConnection();
            ps = connection.prepareStatement("select max(sale_main_id) from tbl_sale_main");
            rs = ps.executeQuery();
            String invoiceNum = null;

            if (rs.next()) {
                long id = rs.getInt(1) + 1;

                invoiceNum = String.format("%07d", id);
            }


            return "SUMA"+invoiceNum;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection(connection , ps , rs);

        }

    }
}
