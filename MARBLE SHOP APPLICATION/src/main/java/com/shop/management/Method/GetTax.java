package com.shop.management.Method;

import com.shop.management.Model.TAX;
import com.shop.management.PropertiesLoader;
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
                return null;
            }

            ps = connection.prepareStatement(new PropertiesLoader().getReadProp().getProperty("GET_TAX"));
            rs = ps.executeQuery();

            ObservableList<TAX> tax = FXCollections.observableArrayList();

            while (rs.next()) {

                // tax

                int taxID = rs.getInt("tax_id");
                int hsn_sac = rs.getInt("hsn_sac");
                int sgst = rs.getInt("sgst");
                int cgst = rs.getInt("cgst");
                int igst = rs.getInt("igst");
                String tax_description = rs.getString("description");
                String gstName = rs.getString("gstName");

              tax.add(new TAX(taxID,hsn_sac , sgst, cgst, igst, gstName, tax_description));
            }

           return tax;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            DBConnection.closeConnection(connection, ps, rs);
        }

    }

}
