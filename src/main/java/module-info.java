module com.andre.dojo.invoicemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jasperreports;
    requires java.desktop;


    opens com.andre.dojo.invoicemanager to javafx.fxml;
    exports com.andre.dojo.invoicemanager;
}