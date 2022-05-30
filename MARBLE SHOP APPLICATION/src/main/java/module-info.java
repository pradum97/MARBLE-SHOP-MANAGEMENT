module com.shop.management {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires java.logging;
    requires java.sql;
    requires org.apache.commons.io;
    requires org.controlsfx.controls;
    requires jasperreports;
    requires java.desktop;
    requires javafx.web;
    requires org.json;
    requires httpmime;
    requires httpcore;
    requires httpclient;



    exports com.shop.management;

    opens com.shop.management.Model to javafx.fxml;
    exports com.shop.management.Model;
    exports com.shop.management.Controller ;
    opens com.shop.management.Controller to javafx.fxml;
    exports com.shop.management.Method ;
    opens com.shop.management.Method to javafx.fxml;
    exports com.shop.management.Controller.Update;
    opens com.shop.management.Controller.Update to javafx.fxml;
    exports com.shop.management.Controller.SettingController;
    opens com.shop.management.Controller.SettingController to javafx.fxml;

    exports com.shop.management.Controller.SellItems;
    opens com.shop.management.Controller.SellItems to javafx.fxml;

    opens com.shop.management;
    exports com.shop.management.Controller.ReturnItems;
    opens com.shop.management.Controller.ReturnItems to javafx.fxml;
    exports com.shop.management.Controller.Proposal;
    opens com.shop.management.Controller.Proposal to javafx.fxml;
    exports com.shop.management.Controller.License;
    opens com.shop.management.Controller.License to javafx.fxml;
}