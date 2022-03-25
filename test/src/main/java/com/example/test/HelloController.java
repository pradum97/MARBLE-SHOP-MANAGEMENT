package com.example.test;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        //  welcomeText.setText("Welcome to JavaFX Application!");
        regularInvoice();
    }

    private static void gstInvoice() {

        Map<String, Object> param = new HashMap<>();

        // File file = new File("");
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

        // CUSTOMER DETAILS
        param.put("CUSTOMER_NAME", "PRADUM KUMAR");
        param.put("CUSTOMER_PHONE", "123456789");
        param.put("CUSTOMER_ADDRESS", "LAKHISARAI");

        param.put("ADDITIONAL_DISCOUNT", 100d);

        // ADDITIONAL DISCOUNT ADD IN BOTH INVOICE

        Map<Long, TaxDetails> map = new HashMap<>();

        for (CartModel cm : getCartModels()) {
            long key = cm.getHsn();

            double netAmount = ((cm.getSellingPrice() * cm.getQuantity()) - cm.getDiscountAmount());
            double taxableAmount = (netAmount * 100) / (100 + (cm.getSgst() + cm.getCgst() + cm.getIgst()));

            if (map.containsKey(key)) {
                // update value
                TaxDetails td = new TaxDetails(cm.getSgst(), cm.getCgst(), cm.getIgst(),
                        map.get(key).getTaxableAmount() + taxableAmount,
                        cm.getHsn());

                map.put(key, td);

            } else {

                TaxDetails td = new TaxDetails(cm.getSgst(), cm.getCgst(), cm.getIgst(), taxableAmount, cm.getHsn());
                map.put(key, td);

            }
        }
        List<TaxDetails> taxList = new ArrayList<>(map.values());

        JRBeanCollectionDataSource jrProductbean = new JRBeanCollectionDataSource(getCartModels());
        JRBeanCollectionDataSource taxBean = new JRBeanCollectionDataSource(taxList);

        param.put("productDetails", jrProductbean);
        param.put("tax", taxBean);

        File file = new File("D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\MARBLE SHOP APPLICATION\\src\\main\\resources\\com\\shop\\management\\invoice\\Gst_Invoice.jrxml");
        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            JasperViewer viewer = new JasperViewer(print, false);
            //    viewer.setZoomRatio((0.533f));
            viewer.setZoomRatio((0.7f));
            viewer.setVisible(true);
            System.out.println("successful");

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private static void regularInvoice() {


        Map<String, Object> param = new HashMap<>();
        // SHOP DETAILS
        param.put("SHOP_NAME", "SUMA MARBLE AND TILES");
        param.put("SHOP_PHONE_1", "9570294565");
        param.put("SHOP_PHONE_2", "9122383010");
        param.put("SHOP_EMAIL", "devrajimas@gmail.com");
        param.put("SHOP_GST_NUMBER", "10DGBPK6505G1ZY");
        param.put("SHOP_ADDRESS", "CHHOTI BALLIA, NH-31, EAST OF WATER TANK , BEGUSARAI, PINCODE - 851211, BIHAR");
        param.put("SHOP_OWNER_NAME", "RAJ KUMAR");


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();

        param.put("INVOICE_NUMBER", "SUMA000001");
        param.put("INVOICE_DATE", dtf.format(now));

        // CUSTOMER DEATILS
        param.put("CUSTOMER_NAME", "PRADUM KUMAR");
        param.put("CUSTOMER_PHONE", "123456789");
        param.put("CUSTOMER_ADDRESS", "LAKHISARAI");

        param.put("ADDITIONAL_DISCOUNT", 100d);

        JRBeanCollectionDataSource cartBean = new JRBeanCollectionDataSource(getCartModels());

        param.put("productDetails", cartBean);
        //  File file = new File("D:\\Desktop Application\\MARBLE-SHOP-MANAGEMENT\\MARBLE SHOP APPLICATION\\src\\main\\resources\\com\\shop\\management\\invoice\\Regular_Invoice.jrxml");

        try {

            JasperReport jasperReport = JasperCompileManager.compileReport(HelloApplication.class.getResourceAsStream("invoice/Regular_Invoice.jrxml"));
            JasperPrint print = JasperFillManager.fillReport(jasperReport, param, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfFile(print, "C:\\Users\\pradu\\OneDrive\\Documents\\t.pdf");
            JasperViewer viewer = new JasperViewer(print, false);
            viewer.setZoomRatio((0.533f));
            viewer.setVisible(true);
            System.out.println("successful");

        } catch (JRException e) {
            e.printStackTrace();
        }
    }

    private static List<CartModel> getCartModels() {

        List<CartModel> cartList = new ArrayList<>();

        CartModel cartModel;

        cartModel = new CartModel(1, 1, 1, 1, 1, 1, "Marble Indian", "wall", "Marble",
                400, 700, 600, 700, 200, 200, "MM", "PCS", null, 18,
                2, "WHITE", "SPECIAL OFFER", 140, 30002, 1260, 144, 9, 9, 0, null, 10);
        cartList.add(cartModel);
        cartModel = new CartModel(1, 1, 1, 1, 1, 1, "Marble Indian", "wall", "Marble",
                400, 700, 600, 700, 200, 200, "MM", "PCS", null, 18,
                2, "WHITE", "SPECIAL OFFER", 140, 30003, 1260, 144, 9, 9, 0, null, 10);
        cartList.add(cartModel);

       /* cartModel = new CartModel(1, 1, 1, 1, 1, 1, "Marble Indian", "wall", "Marble",
                500, 700, 600, 700, 200, 200, "MM", "PCS", null, 18,
                2, "WHITE", "SPECIAL OFFER", 140, 30003, 1260, 180, 9, 9, 0, null,10);
        cartList.add(cartModel);


        cartModel = new CartModel(1, 1, 1, 1, 1, 1, "Marble Indian", "wall", "Marble",
                600, 700, 600, 700, 200, 200, "MM", "PCS", null, 18,
                2, "WHITE", "SPECIAL OFFER", 140, 30002, 1260, 216, 9, 9, 0, null,10);
        cartList.add(cartModel);

        cartModel = new CartModel(1, 1, 1, 1, 1, 1, "Marble Indian", "wall", "Marble",
                700, 700, 600, 800, 200, 200, "MM", "PCS", null, 18,
                2, "WHITE", "SPECIAL OFFER", 160, 30004, 1260, 252, 9, 9, 0, null,10);
        cartList.add(cartModel);*/

        return cartList;

    }

}