package com.andre.dojo.invoicemanager;

import com.andre.dojo.Models.Customer;
import com.andre.dojo.Models.Invoice;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

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

    public ObservableList<Invoice> dataExport = FXCollections.observableArrayList();
    public String message;

    public void setDataExport(ObservableList<Invoice> dataExport) {
        this.dataExport = dataExport;
    }

    public ObservableList<Invoice> getDataExport() {
        return dataExport;
    }

    public void customInitialize(){
        loadTableView();
        System.out.println("hasil pesan dari hello : " + message);
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
}
