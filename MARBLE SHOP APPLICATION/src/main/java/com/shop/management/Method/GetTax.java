package com.shop.management.Method;

import com.shop.management.Model.TAX;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTax {


    public ObservableList<TAX> getGst() {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return null;
            }

            ps = connection.prepareStatement(new Method().getProperties("query.properties").getProperty("GET_TAX"));
            rs = ps.executeQuery();

            ObservableList<TAX> tax = FXCollections.observableArrayList();

            while (rs.next()) {

                // tax

                int taxID = rs.getInt("tax_id");
                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

              tax.add(new TAX(taxID, sgst, cgst, igst, gstName, tax_description));
            }

           return tax;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            closeConnection(connection, ps, rs);
        }

    }

    public TAX getGst(int taxId) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return null;
            }

            ps = connection.prepareStatement(new Method().getProperties("query.properties").getProperty("GET_TAX_FROM_ID"));
           ps.setInt(1,taxId);
            rs = ps.executeQuery();

            TAX tax  = null;

            while (rs.next()) {

                // tax

                int taxID = rs.getInt("tax_id");
                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

              tax = new TAX(taxID, sgst, cgst, igst, gstName, tax_description);
            }

           return tax;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            closeConnection(connection, ps, rs);
        }

    }


    public static void closeConnection(Connection connection, PreparedStatement ps, ResultSet rs) {
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
