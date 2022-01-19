module com.shop.management {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;

    opens com.shop.management to javafx.fxml;
    exports com.shop.management;
}