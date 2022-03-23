package com.shop.management.Method;

import com.shop.management.Model.GstInvoiceModel;
import com.shop.management.Model.RegularInvoiceModel;
import com.shop.management.Model.TaxDetails;
import com.shop.management.util.DBConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateInvoice {

    public static void main(String[] args) {

      // new GenerateInvoice(). gstInvoice(7);

    }


    public void gstInvoice(int saleMainId, boolean isDownLoad , String downloadPath) {

        List<GstInvoiceModel> modelList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println("Connection Failed");
                return;
            }

            String query = "select tsi.product_name , tsi.product_size  , tsi.discount_name  , tsi.sell_price ,\n" +
                    "       tsi.discountper , tsi.discount_amount, tc.customer_name , tc.customer_phone , tc.customer_address, tsm.invoice_number , tsm.sale_date,\n" +
                    "       tsi.product_quantity as fullQuantity ,(SPLIT_PART(tsi.product_quantity, ' -', 1)) as quantity,\n" +
                    "       tsd.shop_name , tsd.shop_address , tsd.shop_email , tsd.shop_gst_number , tsd.shop_phone_1 , tsd.shop_phone_2 , tsd.shop_prop,\n" +
                    "       tsi.sgst , tsi.cgst,tsi.igst , tsi.hsn_sac  , tsm.additional_discount\n" +
                    "       from tbl_sale_main tsm\n" +
                    "       Left Join tbl_saleitems tsi on tsm.sale_main_id = tsi.sale_main_id\n" +
                    "       LEFT JOIN tbl_customer tc on tsm.customer_id = tc.customer_id\n" +
                    "       CROSS JOIN tbl_shop_details tsd\n" +
                    "       where tsm.sale_main_id = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, saleMainId);

            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productSize = rs.getString("product_size");
                String discountName = rs.getString("discount_name");
                String fullQuantity = rs.getString("fullQuantity");

                double salePrice = rs.getDouble("sell_price");
                double discountPer = rs.getDouble("discountper");
                double discountAmount = rs.getDouble("discount_amount");

                int quantity = rs.getInt("quantity");

                long hsn = rs.getLong("hsn_sac");

                double sgst = rs.getDouble("sgst");
                double cgst = rs.getDouble("cgst");
                double igst = rs.getDouble("igst");

                String customerName = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerAddress = rs.getString("customer_address");
                String invoiceNum = rs.getString("invoice_number");
                String saleDate = rs.getString("sale_date");

                String shopName = rs.getString("shop_name");
                String shopAddress = rs.getString("shop_address");
                String shopEmail = rs.getString("shop_email");
                String shopPhone1 = rs.getString("shop_phone_1");
                String shopPhone2 = rs.getString("shop_phone_2");
                String shopGstNum = rs.getString("shop_gst_number");
                String shopProp = rs.getString("shop_prop");

                double additional_discount = rs.getDouble("additional_discount");

                modelList.add(new GstInvoiceModel(productName,fullQuantity,discountName,salePrice,discountAmount,productSize , quantity,hsn , sgst , cgst , igst));

                shop_customer_details(param, rs, customerName, customerPhone, customerAddress, invoiceNum, saleDate, shopName, shopAddress, shopEmail, shopPhone1, shopPhone2, shopGstNum, shopProp, additional_discount);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnection.closeConnection(connection,ps,rs );
        }

        Map<Long, TaxDetails> map = new HashMap<>();

        for (GstInvoiceModel cm : modelList) {
            long key = cm.getHsn();

            double netAmount = ((cm.getSellingPrice()* cm.getQuantity())-cm.getDiscountAmount());
            double taxableAmount = (netAmount * 100) / (100 + (cm.getSgst() + cm.getCgst() + cm.getIgst()));

            if (map.containsKey(key)) {
                // update value
                TaxDetails td = new TaxDetails(cm.getSgst(), cm.getCgst(), cm.getIgst(),
                        map.get(key).getTaxableAmount() + taxableAmount,
                        cm.getHsn());

                map.put(key, td);

            } else {

                TaxDetails td = new TaxDetails(cm.getSgst(), cm.getCgst(), cm.getIgst(), taxableAmount,  cm.getHsn());
                map.put(key, td);

            }
        }
        List<TaxDetails> taxList = new ArrayList<>(map.values());

        JRBeanCollectionDataSource productBean = new JRBeanCollectionDataSource(modelList);
        JRBeanCollectionDataSource taxBean = new JRBeanCollectionDataSource(taxList);

        param.put("productDetails", productBean);
        param.put("tax", taxBean);
        param.put("SUBREPORT_DIR", "src/main/resources/com/shop/management/invoice/");

        File file = new File("src/main/resources/com/shop/management/invoice/Gst_Invoice.jrxml");

        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());


            if (isDownLoad && null != downloadPath){
                JasperExportManager.exportReportToPdfFile(print,downloadPath);

            }else{
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.setZoomRatio((0.533f));
                viewer.setVisible(true);
            }

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private void shop_customer_details(Map<String, Object> param, ResultSet rs, String customerName,
                                       String customerPhone, String customerAddress, String invoiceNum, String saleDate,
                                       String shopName, String shopAddress, String shopEmail, String shopPhone1, String shopPhone2, String shopGstNum,
                                       String shopProp, double additional_discount) throws SQLException {
        if (rs.isLast()){

            // SHOP DETAILS
            param.put("SHOP_NAME", shopName);
            param.put("SHOP_PHONE_1", shopPhone1);
            param.put("SHOP_PHONE_2", shopPhone2);
            param.put("SHOP_EMAIL", shopEmail);
            param.put("SHOP_GST_NUMBER", shopGstNum);
            param.put("SHOP_ADDRESS", shopAddress);
            param.put("SHOP_OWNER_NAME", shopProp);

            param.put("INVOICE_NUMBER",invoiceNum);
            param.put("INVOICE_DATE", saleDate);

            // CUSTOMER DETAILS
            param.put("CUSTOMER_NAME", customerName);
            param.put("CUSTOMER_PHONE", customerPhone);
            param.put("CUSTOMER_ADDRESS", customerAddress);
            param.put("ADDITIONAL_DISCOUNT", additional_discount);
        }
    }

    public void regularInvoice(int saleMainId , boolean isDownLoad , String downloadPath) {

        List<RegularInvoiceModel> modelList = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            connection = new DBConnection().getConnection();
            if (null == connection) {
                System.out.println("Connection Failed");
                return;
            }

            String query = "select tsi.product_name , tsi.product_size  , tsi.discount_name  , tsi.sell_price ,\n" +
                    "       tsi.discountper , tsi.discount_amount, tc.customer_name , tc.customer_phone , tc.customer_address, tsm.invoice_number , tsm.sale_date,\n" +
                    "       tsi.product_quantity as fullQuantity ,(SPLIT_PART(tsi.product_quantity, ' -', 1)) as quantity,\n" +
                    "       tsd.shop_name , tsd.shop_address , tsd.shop_email , tsd.shop_gst_number , tsd.shop_phone_1 ,tsm.additional_discount, tsd.shop_phone_2 , tsd.shop_prop\n" +
                    "       from tbl_sale_main tsm\n" +
                    "       Left Join tbl_saleitems tsi on tsm.sale_main_id = tsi.sale_main_id\n" +
                    "       LEFT JOIN tbl_customer tc on tsm.customer_id = tc.customer_id\n" +
                    "       CROSS JOIN tbl_shop_details tsd\n" +
                    "       where tsm.sale_main_id = ?";

            ps = connection.prepareStatement(query);
            ps.setInt(1, saleMainId);

            rs = ps.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("product_name");
                String productSize = rs.getString("product_size");
                String discountName = rs.getString("discount_name");
                String fullQuantity = rs.getString("fullQuantity");

                double salePrice = rs.getDouble("sell_price");
                double discountPer = rs.getDouble("discountper");
                double discountAmount = rs.getDouble("discount_amount");

                int quantity = rs.getInt("quantity");
                modelList.add(new RegularInvoiceModel(productName,fullQuantity,discountName,salePrice,discountAmount,productSize , quantity));

                String customerName = rs.getString("customer_name");
                String customerPhone = rs.getString("customer_phone");
                String customerAddress = rs.getString("customer_address");
                String invoiceNum = rs.getString("invoice_number");
                String saleDate = rs.getString("sale_date");

                String shopName = rs.getString("shop_name");
                String shopAddress = rs.getString("shop_address");
                String shopEmail = rs.getString("shop_email");
                String shopPhone1 = rs.getString("shop_phone_1");
                String shopPhone2 = rs.getString("shop_phone_2");
                String shopGstNum = rs.getString("shop_gst_number");
                String shopProp = rs.getString("shop_prop");

                double additional_discount = rs.getDouble("additional_discount");

                shop_customer_details(param, rs, customerName, customerPhone, customerAddress, invoiceNum, saleDate, shopName, shopAddress, shopEmail, shopPhone1, shopPhone2, shopGstNum, shopProp, additional_discount);
            }

            JRBeanCollectionDataSource cartBean = new JRBeanCollectionDataSource(modelList);

            param.put("productDetails", cartBean);
            File file = new File("src/main/resources/com/shop/management/invoice/Regular_Invoice.jrxml");

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());

            if (isDownLoad && null != downloadPath){
                JasperExportManager.exportReportToPdfFile(print,downloadPath);

            }else{
                JasperViewer viewer = new JasperViewer(print, false);
                viewer.setZoomRatio((0.533f));
                viewer.setVisible(true);
            }


        } catch (SQLException | JRException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeConnection(connection,ps,rs );
        }

    }


}
