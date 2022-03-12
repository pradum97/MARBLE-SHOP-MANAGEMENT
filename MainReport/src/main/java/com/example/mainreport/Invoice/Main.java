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

        taxDetail = new TaxDetails(9,9,0 , 300 , 20 , 20 ,0 , 32001);
        taxDetail.setCgst(9);
        taxDetail.setSgst(9);
        taxDetail.setIgst(0);
        taxDetail.setHsn(32001);
        taxDetail.setCgstAmount(50.0);
        taxDetail.setSgstAmount(50.0);
        taxDetail.setTaxableAmount(1000);
        taxDetail.setIgstAmount(0);
        taxDetails.add(taxDetail);

        List<CartModel> cartModels = new ArrayList<>();

        CartModel cartModel1 = new CartModel(1,1,1,1,1,1 ,"Marble Indian","wall","Marble",
                400,700,600,700,200,200,"MM","PCS" , null,18,
                2,"WHITE","SPECIAL OFFER",140,30002,1260 , 180 ,9,9,0,null );

        CartModel cartModel2 = new CartModel(1,1,1,1,1,1 ,"Marble Indian","wall","Marble",
                500,700,600,700,200,200,"MM","PCS" , null,18,
                2,"WHITE","SPECIAL OFFER",140,30003,1260 , 180 ,9,9,0,null );

        CartModel cartModel3 = new CartModel(1,1,1,1,1,1 ,"Marble Indian","wall","Marble",
                600,700,600,700,200,200,"MM","PCS" , null,18,
                2,"WHITE","SPECIAL OFFER",140,30002,1260 , 180 ,9,9,0,null );

        CartModel cartModel4 = new CartModel(1,1,1,1,1,1 ,"Marble Indian","wall","Marble",
                700,700,600,800,200,200,"MM","PCS" , null,18,
                2,"WHITE","SPECIAL OFFER",140,30004,1260 , 180 ,9,9,0,null );

        cartModels.add(cartModel1);
        cartModels.add(cartModel2);
        cartModels.add(cartModel3);
        cartModels.add(cartModel4);


        Map<Long,TaxDetails> map = new HashMap<>();

        for (CartModel cm : cartModels) {

            long key = cm.getHsn();

            double sgstAmount = cm.getPurchasePrice()*cm.getSgst()/100;
            double cgstAmount = cm.getPurchasePrice()*cm.getCsgt()/100;
            double igstAmount = cm.getPurchasePrice()*cm.getIgst()/100;
            double totalGstAmount = sgstAmount+cgstAmount+igstAmount;

            double netAmount = (cm.getSellingPrice()*cm.getQuantity())-cm.getDiscountAmount();
            double taxableAmount =  netAmount-totalGstAmount;

            System.out.println(netAmount);

            if (map.containsKey(key)) {
                // update value
                TaxDetails td = new TaxDetails(cm.getSgst(),cm.getCsgt(),cm.getIgst()  ,
                        map.get(key).getTaxableAmount()+taxableAmount,
                         map.get(key).getSgstAmount()+sgstAmount ,
                        map.get(key).getCgstAmount()+cgstAmount ,
                        map.get(key).getIgstAmount()+igstAmount, cm.getHsn());

                map.put(key, td);

            } else {
                TaxDetails td = new TaxDetails(cm.getSgst(),cm.getCsgt(),cm.getIgst()  , taxableAmount,sgstAmount , cgstAmount ,igstAmount, cm.getHsn());
                map.put(key, td);

            }
        }
      /* Map<String, Object> param = new HashMap<>();
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

        File file = new File("D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\iReport\\Gst_Invoice.jrxml");

        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            //   JasperExportManager.exportReportToPdfFile(print, "D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\iReport\\rr.pdf");

            JasperViewer.viewReport(print);
            System.out.println("successful");

        } catch (JRException e) {
            e.printStackTrace();
        }*/


      
    }
}
