package com.example.mainreport.Invoice;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {


    public static void main(String[] args) {


        List<TaxDetails> taxDetails = new ArrayList<>();

        TaxDetails taxDetail;

        taxDetail = new TaxDetails();
        taxDetail.setCgst(9);
        taxDetail.setSgst(9);
        taxDetail.setIgst(0);
        taxDetail.setHsn("32004");
        taxDetail.setCgstAmount(50.0);
        taxDetail.setSgstAmount(50.0);
        taxDetail.setTaxableAmount(1000);
        taxDetail.setIgstAmount(0);
        taxDetails.add(taxDetail);

        taxDetail = new TaxDetails();
        taxDetail.setCgst(9);
        taxDetail.setSgst(0);
        taxDetail.setIgst(0);
        taxDetail.setHsn("32004");
        taxDetail.setCgstAmount(60.0);
        taxDetail.setSgstAmount(60.0);
        taxDetail.setTaxableAmount(2000);
        taxDetail.setIgstAmount(0);
        taxDetails.add(taxDetail);


        taxDetail = new TaxDetails();
        taxDetail.setCgst(9);
        taxDetail.setSgst(0);
        taxDetail.setIgst(0);
        taxDetail.setHsn("32004");
        taxDetail.setCgstAmount(60.0);
        taxDetail.setSgstAmount(60.0);
        taxDetail.setTaxableAmount(2000);
        taxDetail.setIgstAmount(0);
        taxDetails.add(taxDetail);


        taxDetail = new TaxDetails();
        taxDetail.setCgst(9);
        taxDetail.setSgst(0);
        taxDetail.setIgst(0);
        taxDetail.setHsn("32004");
        taxDetail.setCgstAmount(60.0);
        taxDetail.setSgstAmount(60.0);
        taxDetail.setTaxableAmount(2000);
        taxDetail.setIgstAmount(0);
        taxDetails.add(taxDetail);
        List<ProductDetails> productDetails = new ArrayList<>();

        ProductDetails product;

        //TODO, ONLY FOR GST

        int igst = 0;
        int cgst = 9;
        int sgst = 9;

        double mrp = 500;
        int totalTax = (igst + cgst + sgst);
        double taxAmount  = 0;

        String  billType = "GST";

        if ("GST".equals(billType)) {
            taxAmount = (mrp * totalTax) / 100;
        }

        product = new ProductDetails();
        product.setProductName("Indian Marble");
        product.setProductSize("8888x8888-pcs");
        product.setQuantity("10-PKT");
        product.setDiscountName("Special Offer");
        product.setDiscountAmount(100);
        product.setHsn(32004);
        // count tax Amount
        product.setMrp(mrp);
        product.setTaxAmount(taxAmount);
        productDetails.add(product);


        product = new ProductDetails();
        product.setProductName("Indian Marble");
        product.setProductSize("200x200 -pcs");
        product.setQuantity("20-PKT");
        product.setDiscountName("Special Offer");
        product.setDiscountAmount(200);
        product.setHsn(32004);
        product.setMrp(mrp); // mrp with gst
        product.setTaxAmount(taxAmount);
        productDetails.add(product);

        product = new ProductDetails();
        product.setProductName("Indian Marble");
        product.setProductSize("200x200 -pcs");
        product.setQuantity("10-PKT");
        product.setDiscountName("Special Offer");
        product.setDiscountAmount(100);
        product.setHsn(32004);
        // count tax Amount
        product.setMrp(mrp);
        product.setTaxAmount(taxAmount);
        productDetails.add(product);






        Map<String, Object> param = new HashMap<>();
        // SHOP DETAILS
        param.put("SHOP_NAME", "SUMA MARBLE AND TILES");
        param.put("SHOP_PHONE_1", "9570294565");
        param.put("SHOP_PHONE_2", "9122383010");
        param.put("SHOP_EMAIL", "devrajimas@gmail.com");
        param.put("SHOP_GST_NUMBER", "10DGBPK6505G1ZY");
        param.put("SHOP_ADDRESS", "CHHOTI BALLIA, NH-31, EAST OF WATER TANK , BEGUSARAI, PINCODE - 851211, BIHAR");
        param.put("SHOP_OWNER_NAME", "RAJ KUMAR");

        // INVOICE DETAILS

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();

        param.put("INVOICE_NUMBER", "SUMA000001");
        param.put("INVOICE_DATE", dtf.format(now));

        // CUSTOMER DEATILS
        param.put("CUSTOMER_NAME", "PRADUM KUMAR");
        param.put("CUSTOMER_PHONE", "123456789");
        param.put("CUSTOMER_ADDRESS", "LAKHISARAI");

        JRBeanCollectionDataSource jrProductbean = new JRBeanCollectionDataSource(productDetails);
        JRBeanCollectionDataSource tax = new JRBeanCollectionDataSource(taxDetails);


        param.put("productDetails", jrProductbean);
        param.put("tax", tax);

        File file = new File("D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\iReport\\invoiceMain.jrxml");

        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            //   JasperExportManager.exportReportToPdfFile(print, "D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\iReport\\rr.pdf");

            JasperViewer.viewReport(print);
            System.out.println("successful");

        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
