package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Design;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ExportController {

    @FXML
    private TableView<Invoice> tableViewExport;
    @FXML
    private TableColumn<Invoice, String> tableColumnCode;
    @FXML
    private TableColumn<Invoice, String> tableColumnDesc;
    @FXML
    private TableColumn<Invoice, String> tableColumnCustomer;
    @FXML
    private TableColumn<Invoice, String> tableColumnTotalItem;
    @FXML
    private Pane export;
    @FXML
    private Label total;

    public ObservableList<Invoice> dataExport = FXCollections.observableArrayList();
    public String message;

    public void setDataExport(ObservableList<Invoice> dataExport) {
        this.dataExport = dataExport;
    }

    public ObservableList<Invoice> getDataExport() {
        return dataExport;
    }

    public void initialize(){
        export.setOnMouseClicked(e -> {
            try {
                toPDF();
            } catch (JRException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    public void customInitialize(){
        loadTableView();
        total.setText(String.valueOf(dataExport.size()));
        System.out.println("hasil pesan dari hello : " + message);
//        insertDesign();
    }

    public void loadTableView() {
        tableViewExport.setEditable(true);
        tableViewExport.setItems(FXCollections.observableArrayList(dataExport));
        tableColumnCode.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceCode()));
        tableColumnDesc.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getDescription()));
        tableColumnCustomer.setCellValueFactory(e -> new SimpleStringProperty(Long.toString(e.getValue().getCustomer_id())));
        tableColumnTotalItem.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getInvoiceMarkText()));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void toPDF() throws JRException {
        String cek = "1724503671986", cek2 = "1724656789071";
        for (Invoice invoice : dataExport) {
            long id = invoice.getId(); // Assuming the id is an int and there's a getId() method
            if(id == Long.parseLong(cek2)){
                System.out.println("Invoice ID: " + id);
                System.out.println("with jxrml : " + Design.getOneData(invoice.getJrxml_id()));
                Design design = Design.getOneData(invoice.getJrxml_id());
                String jrxmlContent = design.getJrxml();
                ByteArrayInputStream jrxmlInputStream = new ByteArrayInputStream(jrxmlContent.getBytes(StandardCharsets.UTF_8));
                JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlInputStream);
                // Prepare a Map-based data source, ensuring all required fields are present
                Map<String, Object> data = new HashMap<>();
//                data.put("nomorSurat", null);
//                data.put("tanggal", null);
//                data.put("tujuan", null);
//                data.put("keterangan", null);
//                data.put("totalHarga", null);

                JRDataSource dataSource = new JRMapArrayDataSource(new Map[] { data });
                Map<String, Object> parameters = new HashMap<>();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                JasperViewer viewer = new JasperViewer(jasperPrint, false);
                viewer.setVisible(true);
            }
        }
        dataExport.clear();
        total.setText(String.valueOf(dataExport.size()));
        loadTableView();
    }

    private void insertDesign(){
        Design.addToDB(new Design(

        ));
    }
}
