module com.andre.dojo.invoicemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires jasperreports;
    requires java.desktop;
    requires java.sql;
    requires org.apache.pdfbox.io;
    requires javafx.swing;
    requires javafx.web;
    requires com.dlsc.pdfviewfx;
    requires org.xerial.sqlitejdbc;
    requires sql2o;
    requires org.slf4j;


    opens com.andre.dojo.invoicemanager to javafx.fxml;
    opens com.andre.dojo.Models;
    exports com.andre.dojo.invoicemanager;
    exports com.andre.dojo.Models;
}