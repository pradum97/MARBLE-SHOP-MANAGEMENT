package com.shop.management.Method;

import com.shop.management.Model.Stock;
import com.shop.management.util.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetStockData {

    public Stock getStock(int productID) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Stock stock = null;

        try {

            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return null;
            }

            ps = connection.prepareStatement(new Method().getProperties("query.properties")
                    .getProperty("GET_STOCK"));
            ps.setInt(1, productID);


            rs = ps.executeQuery();

            if (rs.next()) {

                int stockID = rs.getInt("stock_id");
                int productId = rs.getInt("product_id");

                double purchasePrice = rs.getDouble("purchase_price");
                double mrp = rs.getDouble("product_mrp");
                double minSellPrice = rs.getDouble("min_sellingprice");

                double height = rs.getDouble("height");
                double width = rs.getDouble("width");

                int quantity = rs.getInt("quantity");

                String sizeUnit = rs.getString("size_unit");
                String quantityUnit = rs.getString("quantity_unit");

                // int isActive = rs.getInt("is_active");

                stock = new Stock(stockID, productId, purchasePrice, mrp, minSellPrice,
                        height, width, sizeUnit, quantityUnit, quantity);
            }

            return stock;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            DBConnection.closeConnection(connection, ps, rs);
        }
    }

    public ObservableList<Stock> getStockList(int productID) {

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ObservableList<Stock> stocks = FXCollections.observableArrayList();

        try {

            connection = new DBConnection().getConnection();

            if (null == connection) {
                System.out.println("connection failed");
                return null;
            }

            ps = connection.prepareStatement(new Method().getProperties("query.properties")
                    .getProperty("GET_STOCK"));
            ps.setInt(1, productID);
            rs = ps.executeQuery();


            while (rs.next()) {

                int stockID = rs.getInt("stock_id");
                int productId = rs.getInt("product_id");

                double purchasePrice = rs.getDouble("purchase_price");
                double mrp = rs.getDouble("product_mrp");
                double minSellPrice = rs.getDouble("min_sellingprice");

                double height = rs.getDouble("height");
                double width = rs.getDouble("width");

                int quantity = rs.getInt("quantity");

                String sizeUnit = rs.getString("size_unit");
                String quantityUnit = rs.getString("quantity_unit");

                int isActive = rs.getInt("is_active");

                String fullQuantity = quantity+" - "+quantityUnit;

                BigDecimal h = BigDecimal.valueOf(height);
                BigDecimal w = BigDecimal.valueOf(width);

               String fullSize =  h.stripTrailingZeros().toPlainString()+" x "
                       +w.stripTrailingZeros().toPlainString()+" "+sizeUnit;


                stocks.add(new Stock(stockID, productId, purchasePrice, mrp, minSellPrice,
                        height, width, sizeUnit, quantityUnit, quantity,fullSize,fullQuantity));
            }

            return stocks;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            DBConnection.closeConnection(connection, ps, rs);
        }
    }
}
