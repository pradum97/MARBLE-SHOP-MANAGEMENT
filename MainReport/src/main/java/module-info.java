module com.example.mainreport {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires java.sql;


    exports com.example.mainreport.Invoice;
    opens com.example.mainreport.Invoice to javafx.fxml;
}