module com.shop.management {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires java.logging;
    requires java.sql;


    opens com.shop.management to javafx.fxml;
    exports com.shop.management;
    exports com.shop.management.Controller;
    opens com.shop.management.Controller to javafx.fxml;
}